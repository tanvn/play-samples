package models

import scalikejdbc._
import java.time.{LocalDateTime}

case class Goals(
  goalId: Long,
  corporateId: Long,
  periodId: Long,
  sheetId: Long,
  ownerId: Long,
  completedAt: Option[LocalDateTime] = None,
  versionNo: Int,
  createdBy: Long,
  createdAt: LocalDateTime,
  updatedBy: Long,
  updatedAt: LocalDateTime) {

  def save()(implicit session: DBSession = Goals.autoSession): Goals = Goals.save(this)(session)

  def destroy()(implicit session: DBSession = Goals.autoSession): Int = Goals.destroy(this)(session)

}


object Goals extends SQLSyntaxSupport[Goals] {

  override val tableName = "goals"

  override val columns = Seq("goal_id", "corporate_id", "period_id", "sheet_id", "owner_id", "completed_at", "version_no", "created_by", "created_at", "updated_by", "updated_at")

  def apply(g: SyntaxProvider[Goals])(rs: WrappedResultSet): Goals = apply(g.resultName)(rs)
  def apply(g: ResultName[Goals])(rs: WrappedResultSet): Goals = new Goals(
    goalId = rs.get(g.goalId),
    corporateId = rs.get(g.corporateId),
    periodId = rs.get(g.periodId),
    sheetId = rs.get(g.sheetId),
    ownerId = rs.get(g.ownerId),
    completedAt = rs.get(g.completedAt),
    versionNo = rs.get(g.versionNo),
    createdBy = rs.get(g.createdBy),
    createdAt = rs.get(g.createdAt),
    updatedBy = rs.get(g.updatedBy),
    updatedAt = rs.get(g.updatedAt)
  )

  val g = Goals.syntax("g")

  override val autoSession = AutoSession

  def find(goalId: Long)(implicit session: DBSession = autoSession): Option[Goals] = {
    withSQL {
      select.from(Goals as g).where.eq(g.goalId, goalId)
    }.map(Goals(g.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Goals] = {
    withSQL(select.from(Goals as g)).map(Goals(g.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Goals as g)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Goals] = {
    withSQL {
      select.from(Goals as g).where.append(where)
    }.map(Goals(g.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Goals] = {
    withSQL {
      select.from(Goals as g).where.append(where)
    }.map(Goals(g.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Goals as g).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    goalId: Long,
    corporateId: Long,
    periodId: Long,
    sheetId: Long,
    ownerId: Long,
    completedAt: Option[LocalDateTime] = None,
    versionNo: Int,
    createdBy: Long,
    createdAt: LocalDateTime,
    updatedBy: Long,
    updatedAt: LocalDateTime)(implicit session: DBSession = autoSession): Goals = {
    withSQL {
      insert.into(Goals).namedValues(
        column.goalId -> goalId,
        column.corporateId -> corporateId,
        column.periodId -> periodId,
        column.sheetId -> sheetId,
        column.ownerId -> ownerId,
        column.completedAt -> completedAt,
        column.versionNo -> versionNo,
        column.createdBy -> createdBy,
        column.createdAt -> createdAt,
        column.updatedBy -> updatedBy,
        column.updatedAt -> updatedAt
      )
    }.update.apply()

    Goals(
      goalId = goalId,
      corporateId = corporateId,
      periodId = periodId,
      sheetId = sheetId,
      ownerId = ownerId,
      completedAt = completedAt,
      versionNo = versionNo,
      createdBy = createdBy,
      createdAt = createdAt,
      updatedBy = updatedBy,
      updatedAt = updatedAt)
  }

  def batchInsert(entities: collection.Seq[Goals])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("goalId") -> entity.goalId,
        Symbol("corporateId") -> entity.corporateId,
        Symbol("periodId") -> entity.periodId,
        Symbol("sheetId") -> entity.sheetId,
        Symbol("ownerId") -> entity.ownerId,
        Symbol("completedAt") -> entity.completedAt,
        Symbol("versionNo") -> entity.versionNo,
        Symbol("createdBy") -> entity.createdBy,
        Symbol("createdAt") -> entity.createdAt,
        Symbol("updatedBy") -> entity.updatedBy,
        Symbol("updatedAt") -> entity.updatedAt))
    SQL("""insert into goals(
      goal_id,
      corporate_id,
      period_id,
      sheet_id,
      owner_id,
      completed_at,
      version_no,
      created_by,
      created_at,
      updated_by,
      updated_at
    ) values (
      {goalId},
      {corporateId},
      {periodId},
      {sheetId},
      {ownerId},
      {completedAt},
      {versionNo},
      {createdBy},
      {createdAt},
      {updatedBy},
      {updatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: Goals)(implicit session: DBSession = autoSession): Goals = {
    withSQL {
      update(Goals).set(
        column.goalId -> entity.goalId,
        column.corporateId -> entity.corporateId,
        column.periodId -> entity.periodId,
        column.sheetId -> entity.sheetId,
        column.ownerId -> entity.ownerId,
        column.completedAt -> entity.completedAt,
        column.versionNo -> entity.versionNo,
        column.createdBy -> entity.createdBy,
        column.createdAt -> entity.createdAt,
        column.updatedBy -> entity.updatedBy,
        column.updatedAt -> entity.updatedAt
      ).where.eq(column.goalId, entity.goalId)
    }.update.apply()
    entity
  }

  def destroy(entity: Goals)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Goals).where.eq(column.goalId, entity.goalId) }.update.apply()
  }

}
