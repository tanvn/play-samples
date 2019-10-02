package v1.post

import com.google.inject.Inject
import models.{Goals, ScoreItems}
import play.api.db.Database
import play.api.libs.json.{Json, OWrites}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import scalikejdbc._

class DbController @Inject()(db: Database, val controllerComponents: ControllerComponents)
  extends BaseController {
  import  DbController._

  val si = ScoreItems.si
  val g = Goals.g

  def index = Action {
    var outString = "Number is "
    val conn      = db.getConnection()

    try {
      val stmt = conn.createStatement
      val rs   = stmt.executeQuery("SELECT count(*) as testkey from users ")

      while (rs.next()) {
        outString += rs.getString("testkey")
      }
    } finally {
      println("close connection 2222")
      conn.close()
    }
    Ok(outString)
  }

  def users = Action {
    var outString = "Number is "
    val conn      = db.getConnection()
    println(s"datasource = ${db.dataSource}")

    try {
      val sql = "SELECT count(*) as total FROM users where user_id > ?"
      val stmt = conn.prepareStatement(sql)
      stmt.setInt(1, 100)
      val rs   = stmt.executeQuery()
      while (rs.next()) {
        outString += rs.getString("total")
      }
    } finally {
      conn.close()
    }
    Ok(outString)
  }



  def findGoal(id: Long) : Action[AnyContent] = Action { implicit req =>
    using(DB(ConnectionPool.borrow())) { db =>
      db.localTx { implicit session =>
        val goal = findGoalById(id) // Not found!
        println(goal)
        Ok(Json.toJson(goal))
      }
    }
  }

  def find(id: Long) : Action[AnyContent] = Action { implicit req =>
    using(DB(ConnectionPool.borrow())) { db =>
      db.localTx { implicit session =>
        val scoreItem = findById(id) // Not found!
        println(scoreItem)
        Ok(Json.toJson(scoreItem))
      }
    }
  }

  def findGoalById(id: Long)(implicit session: DBSession): Option[Goals] = {

    val sql: SQL[Goals, HasExtractor] = sql"select ${g.result.*} from ${Goals.as(g)} where ${g.goalId} = $id".map(Goals(g))
    sql.first().apply()//first().apply()
  }


  def findById(id: Long)(implicit session: DBSession): Option[ScoreItems] =
    withSQL {
      select
        .from(ScoreItems.as(si))
        .where
        .eq(si.scoreItemId, id)
    }.map(ScoreItems(si)(_)).first().apply()

}

object DbController{
  implicit val scoreViewWrites: OWrites[ScoreItems] = Json.writes[ScoreItems]
  implicit val goalWrites: OWrites[Goals] = Json.writes[Goals]

}
