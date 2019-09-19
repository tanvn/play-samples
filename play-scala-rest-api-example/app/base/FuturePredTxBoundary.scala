package base

import scalikejdbc.{Tx, TxBoundary}

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success, Try}

/**
 * @see scalikejdbc.TxBoundary.Future
 */
object FuturePredTxBoundary {

  private def doFinishTx[A](result: Try[A])(doFinish: Try[A] => Unit): Try[A] =
    scala.util
      .Try(doFinish(result))
      .transform(
        _ => result,
        finishError =>
          Failure(result match {
            case Success(_) => finishError
            case Failure(resultError) =>
              resultError.addSuppressed(finishError)
              resultError
          })
      )

  private def onFinishTx[A](resultF: Future[A])(doFinish: Try[A] => Unit)(
    implicit ec: ExecutionContext): Future[A] = {
    val p = Promise[A]
    resultF.onComplete(result => p.complete(doFinishTx(result)(doFinish)))
    p.future
  }

  def futurePredTxBoundary[A](pred: A => Boolean)(
    implicit ec: ExecutionContext): TxBoundary[Future[A]] =
    new TxBoundary[Future[A]] {

      def finishTx(result: Future[A], tx: Tx): Future[A] =
        onFinishTx(result) {
          case Success(a) => if (pred(a)) tx.commit() else tx.rollback()
          case Failure(_) => tx.rollback()
        }

      override def closeConnection(result: Future[A], doClose: () => Unit): Future[A] =
        onFinishTx(result)(_ => doClose())
    }
}
