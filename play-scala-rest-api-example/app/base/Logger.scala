package base

//import scala.collection.JavaConverters._
import scala.jdk.CollectionConverters._
import net.logstash.logback.marker.{LogstashMarker, Markers}
import org.slf4j.{LoggerFactory, Logger => Slf4jLogger}
/**
 * ロガー
 */
class Logger private (val logger: Slf4jLogger) {

  def trace(message: => String)(implicit context: LoggingContext): Unit =
    if (logger.isTraceEnabled) {
      logger.trace(toLogstashMarker(context), message)
    }

  def trace(message: => String, error: => Throwable)(implicit context: LoggingContext): Unit =
    if (logger.isTraceEnabled) {
      logger.trace(toLogstashMarker(context), message, error)
    }

  def debug(message: => String)(implicit context: LoggingContext): Unit =
    if (logger.isDebugEnabled) {
      logger.debug(toLogstashMarker(context), message)
    }

  def debug(message: => String, error: => Throwable)(implicit context: LoggingContext): Unit =
    if (logger.isDebugEnabled) {
      logger.debug(toLogstashMarker(context), message, error)
    }

  def info(message: => String)(implicit context: LoggingContext): Unit =
    if (logger.isInfoEnabled) {
      logger.info(toLogstashMarker(context), message)
    }

  def info(message: => String, error: => Throwable)(implicit context: LoggingContext): Unit =
    if (logger.isInfoEnabled) {
      logger.info(toLogstashMarker(context), message, error)
    }

  def warn(message: => String)(implicit context: LoggingContext): Unit =
    if (logger.isWarnEnabled) {
      logger.warn(toLogstashMarker(context), message)
    }

  def warn(message: => String, error: => Throwable)(implicit context: LoggingContext): Unit =
    if (logger.isWarnEnabled) {
      logger.warn(toLogstashMarker(context), message, error)
    }

  def error(message: => String)(implicit context: LoggingContext): Unit =
    if (logger.isErrorEnabled) {
      logger.error(toLogstashMarker(context), message)
    }

  def error(message: => String, error: => Throwable)(implicit context: LoggingContext): Unit =
    if (logger.isErrorEnabled) {
      logger.error(toLogstashMarker(context), message, error)
    }

  private def toLogstashMarker(context: LoggingContext): LogstashMarker =
    Markers.appendEntries(context.values.asJava)

}

object Logger {

  def apply(name: String): Logger =
    new Logger(LoggerFactory.getLogger(name))

  def apply(clazz: Class[_]): Logger =
    new Logger(LoggerFactory.getLogger(clazz.getName.stripSuffix("$")))

}
