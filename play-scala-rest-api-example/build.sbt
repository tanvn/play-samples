import sbt.Keys._
import play.sbt.PlaySettings
import com.lightbend.sbt.javaagent.JavaAgent
import com.lightbend.sbt.javaagent.JavaAgent.JavaAgentKeys.javaAgents
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging

lazy val root = (project in file("."))
  .enablePlugins(PlayService, PlayLayoutPlugin, Common, JavaAgent,
    JavaAppPackaging)
  .settings(
    name := "play-scala-rest-api-example",
    version := "2.7.x",
    scalaVersion := "2.13.0",
    libraryDependencies ++= Seq(
      guice,
      jdbc,
      "org.joda" % "joda-convert" % "2.1.2",
      "net.logstash.logback" % "logstash-logback-encoder" % "5.2",
      "io.lemonlabs" %% "scala-uri" % "1.4.10",
      "net.codingwell" %% "scala-guice" % "4.2.5",
      "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test,
      "com.datadoghq" % "dd-trace-api" % "0.33.0",
      "org.postgresql" % "postgresql" % "42.2.5",
      "org.scalikejdbc" %% "scalikejdbc" % "3.3.5",
//      "org.scalikejdbc" %% "scalikejdbc-test"   % "3.3.5"   % "test",
      "org.scalikejdbc" %% "scalikejdbc-config" % "3.3.5",
      "com.h2database" % "h2" % "1.4.199",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.hsqldb" % "hsqldb" % "2.3.2",
      "io.methvin.play" %% "autoconfig-macros" % "0.3.0",
      "org.specs2" %% "specs2-core" % "4.6.0" % "test"
    ),
  ).settings(
  javaAgents += "com.datadoghq" % "dd-java-agent" % "0.33.0"
).enablePlugins(ScalikejdbcPlugin)

lazy val gatlingVersion = "3.1.3"
lazy val gatling = (project in file("gatling"))
  .enablePlugins(GatlingPlugin)
  .settings(
    scalaVersion := "2.12.8",
    libraryDependencies ++= Seq(
      "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % Test,
      "io.gatling" % "gatling-test-framework" % gatlingVersion % Test
    )
  )

// Documentation for this project:
//    sbt "project docs" "~ paradox"
//    open docs/target/paradox/site/index.html
lazy val docs = (project in file("docs")).enablePlugins(ParadoxPlugin).
  settings(
    scalaVersion := "2.13.0",
    paradoxProperties += ("download_url" -> "https://example.lightbend.com/v1/download/play-rest-api")
  )
