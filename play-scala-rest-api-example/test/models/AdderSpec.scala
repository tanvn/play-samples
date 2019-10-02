package models

import org.specs2.mutable._

class AdderSpec extends Specification{
  "add" should {
    val a = 2
    val b = 3
    "return correct result " in {
      val res = a + b
      res should_==(5)

    }
  }

}
