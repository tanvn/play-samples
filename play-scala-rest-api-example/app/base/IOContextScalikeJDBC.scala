package base

import scalikejdbc.DBSession

final case class IOContextScalikeJDBC(value: DBSession) extends IOContext {
  override type ContextType = DBSession
}
