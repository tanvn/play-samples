package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import java.time.{LocalDateTime}


class GoalsSpec extends Specification {

  "Goals" should {

    val g = Goals.syntax("g")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Goals.find(1L)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Goals.findBy(sqls.eq(g.goalId, 1L))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Goals.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Goals.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Goals.findAllBy(sqls.eq(g.goalId, 1L))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Goals.countBy(sqls.eq(g.goalId, 1L))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Goals.create(goalId = 1L, corporateId = 1L, periodId = 1L, sheetId = 1L, ownerId = 1L, versionNo = 123, createdBy = 1L, createdAt = null, updatedBy = 1L, updatedAt = null)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Goals.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Goals.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Goals.findAll().head
      val deleted = Goals.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Goals.find(1L)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Goals.findAll()
      entities.foreach(e => Goals.destroy(e))
      val batchInserted = Goals.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
