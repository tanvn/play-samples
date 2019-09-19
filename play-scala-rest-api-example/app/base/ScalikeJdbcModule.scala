package base

import java.time.LocalDateTime

import com.google.inject.{AbstractModule, Provides}
import io.methvin.play.autoconfig.AutoConfig
import javax.inject.{Inject, Singleton}
import javax.sql.DataSource
import net.codingwell.scalaguice.ScalaModule
import play.api.db.{DatabaseConfig, HikariCPConnectionPool}
import play.api.inject.ApplicationLifecycle
import play.api.{ConfigLoader, Configuration, Environment, Logger}
import scalikejdbc.{ConnectionPool, DataSourceCloser, DataSourceConnectionPool, GlobalSettings, LoggingSQLAndTimeSettings}

import scala.concurrent.Future
import scala.util.control.NonFatal

class ScalikeJdbcModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[ScalikeJdbcModule.ScalikeJdbcWithHikari].asEagerSingleton()
    bind[IOContextFactory].to[ScalikeJdbcIOContextFactory].asEagerSingleton()
  }

  @Provides
  def provideScalikeJdbcInitialized(
                                     scalikejdbc: ScalikeJdbcModule.ScalikeJdbcWithHikari
                                   ): ScalikeJdbcInitialized =
    scalikejdbc.initialized

}

object ScalikeJdbcModule {

  final val basePath: String = "hrmony.prf.scalikejdbc."

  final val logPath: String = "loggingSQLAndTime"

  implicit val symbolLoader: ConfigLoader[Symbol] = ConfigLoader.stringLoader.map(Symbol.apply)

  implicit val loggingSQLAndTimeSettingsLoader: ConfigLoader[LoggingSQLAndTimeSettings] =
    AutoConfig.loader

  /**
   * scalikejdbc + HikariCP でのデータソース設定
   */
  @Singleton
  class ScalikeJdbcWithHikari @Inject()(
                                         val conf: Configuration,
                                         env: Environment,
                                         hikari: HikariCPConnectionPool,
                                         lifecycle: ApplicationLifecycle
                                       ) {

    private[this] val log = Logger(getClass)

    val initialized: ScalikeJdbcInitialized = {
      lifecycle.addStopHook(() => Future.successful(onStop()))
      val databases = onStart()
      ScalikeJdbcInitialized(
        databases = databases,
        initializedAt = LocalDateTime.now
      )
    }

    /**
     * Start !
     *
     */
    private def onStart(): Seq[String] = {
      loadGlobalSettings()

      println("ScalikeJdbcModule start !!!!")

      val dbConfigs: Map[String, Configuration] = conf.getPrototypedMap("hrmony.prf.db", "play.db.prototype")
      println(s"$dbConfigs")

      dbConfigs.map {
        case (name, partialConf) =>
          val url = partialConf.get[String]("url")
          log.info(s"Creating data source: $name, $url")
          val dbConfig = DatabaseConfig.fromConfig(partialConf, env)
          val dataSource: DataSource = hikari.create(name, dbConfig, partialConf.underlying)
          val closer = new DataSourceCloser {
            override def close(): Unit = hikari.close(dataSource)
          }
          val pool = new DataSourceConnectionPool(dataSource, closer = closer)
          ConnectionPool.add(Symbol(name), pool)
          name
      }.toSeq
    }

    /**
     * Stop !
     *
     */
    private def onStop(): Unit = {
      log.info("Closing all connections...")
      try {
        ConnectionPool.closeAll()
      } catch {
        case NonFatal(e) =>
          log.error(s"Failed to close connections.", e)
      }
    }

    /**
     * TypesafeConfigReader#loadGlobalSettings より拝借
     *
     * Ref. https://github.com/scalikejdbc/scalikejdbc/blob/master/scalikejdbc-config/src/main/scala/scalikejdbc/config/TypesafeConfigReader.scala
     *
     */
    private def loadGlobalSettings(): Unit = {
      GlobalSettings.loggingSQLErrors = conf.get[Boolean](s"${basePath}loggingSQLErrors")
      GlobalSettings.loggingSQLAndTime = conf.get[LoggingSQLAndTimeSettings](s"$basePath$logPath")
      log.info(s"scalikejdbc.loggingSQLAndTime: ${GlobalSettings.loggingSQLAndTime}")
      log.info(s"scalikejdbc.loggingSQLErrors: ${GlobalSettings.loggingSQLErrors}")
    }

  }

}
