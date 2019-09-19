package base

import java.time.LocalDateTime

import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}
import _root_.scalikejdbc.DB

class ScalikeJdbcIOContextFactory @Inject()(
                                             initialized: ScalikeJdbcInitialized
                                           ) extends IOContextFactory {

  private[this] val log = Logger(getClass)

  log.info(
    s"IOContextFactory will create db session with scalikejdbc that initialized at ${initialized.initializedAt}"
  )

  override def localTx[A](f: IOContext => A): A =
    DB.localTx { dbSession =>
      f(IOContextScalikeJDBC(dbSession))
    }

  override def localTx[A](f: IOContext => A, pred: A => Boolean): A =
    DB.localTx({ dbSession =>
      f(IOContextScalikeJDBC(dbSession))
    })(boundary = PredTxBoundary.predTxBoundary(pred))

  override def readOnly[A](f: IOContext => A): A =
    DB.readOnly { dbSession =>
      f(IOContextScalikeJDBC(dbSession))
    }

  override def localFutureTx[A](f: IOContext => Future[A])(
    implicit ec: ExecutionContext): Future[A] =
    DB.futureLocalTx { dbSesion =>
      f(IOContextScalikeJDBC(dbSesion))
    }

  override def localFutureTx[A](f: IOContext => Future[A], pred: A => Boolean)(
    implicit ec: ExecutionContext): Future[A] =
    DB.localTx({ dbSession =>
      f(IOContextScalikeJDBC(dbSession))
    })(boundary = FuturePredTxBoundary.futurePredTxBoundary(pred))

}

case class ScalikeJdbcInitialized(
                                   databases: Seq[String],
                                   initializedAt: LocalDateTime
                                 )
