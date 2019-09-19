package base


trait LoggingContext {

  def values: Map[String, String]

  def message: String = values.toSeq.map { case (k, v) => s"$k=${escape(v)}" }.mkString(",")

  private def escape(value: String): String = value.replaceAll("\t", "    ")

}

object LoggingContext extends LowPriorityLoggingContextImplicits {

  def apply(values: Map[String, String] = Map.empty): LoggingContext =
    new DefaultLoggingContext(values)
}

trait LowPriorityLoggingContextImplicits {

  implicit val NoLoggingContext: LoggingContext = LoggingContext()

}

private class DefaultLoggingContext(val values: Map[String, String]) extends LoggingContext
