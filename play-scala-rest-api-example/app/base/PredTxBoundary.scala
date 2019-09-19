package base

import scalikejdbc.{Tx, TxBoundary}

object PredTxBoundary {

  def predTxBoundary[A](pred: A => Boolean): TxBoundary[A] = (result: A, tx: Tx) => {
    if (pred(result)) {
      tx.commit()
    } else {
      tx.rollback()
    }
    result
  }
}
