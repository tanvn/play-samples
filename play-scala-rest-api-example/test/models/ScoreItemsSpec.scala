package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import java.time.{LocalDateTime}


class ScoreItemsSpec extends Specification {

  "ScoreItems" should {

    val si = ScoreItems.syntax("si")

    "find by primary keys" in new AutoRollback {
      val maybeFound = ScoreItems.find(1L)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = ScoreItems.findBy(sqls.eq(si.scoreItemId, 1L))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = ScoreItems.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = ScoreItems.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = ScoreItems.findAllBy(sqls.eq(si.scoreItemId, 1L))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = ScoreItems.countBy(sqls.eq(si.scoreItemId, 1L))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = ScoreItems.create(scoreItemId = 1L, corporateId = 1L, scoreId = 1L, scoreItemName = "MyString", scoreItemValue = new java.math.BigDecimal("1"), sortNo = 123, createdBy = 1L, createdAt = null, updatedBy = 1L, updatedAt = null)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = ScoreItems.findAll().head
      // TODO modify something
      val modified = entity
      val updated = ScoreItems.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = ScoreItems.findAll().head
      val deleted = ScoreItems.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = ScoreItems.find(1L)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = ScoreItems.findAll()
      entities.foreach(e => ScoreItems.destroy(e))
      val batchInserted = ScoreItems.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
