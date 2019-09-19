package base

import scala.concurrent.{ExecutionContext, Future}


trait IOContext {
  type ContextType
  def value: ContextType
}

trait IOContextFactory {

  def localTx[A](f: IOContext => A): A

  def localTx[A](f: IOContext => A, pred: A => Boolean): A

  def readOnly[A](f: IOContext => A): A

  def localFutureTx[A](f: IOContext => Future[A])(implicit ec: ExecutionContext): Future[A]

  def localFutureTx[A](f: IOContext => Future[A], pred: A => Boolean)(
    implicit ec: ExecutionContext
  ): Future[A]
}
