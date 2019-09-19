package models

import scalikejdbc._
import java.time.{LocalDateTime}

case class ScoreItems(
  scoreItemId: Long,
  corporateId: Long,
  scoreId: Long,
  scoreItemName: String,
  description: Option[String] = None,
  scoreItemValue: BigDecimal,
  sortNo: Int,
  createdBy: Long,
  createdAt: LocalDateTime,
  updatedBy: Long,
  updatedAt: LocalDateTime) {

  def save()(implicit session: DBSession = ScoreItems.autoSession): ScoreItems = ScoreItems.save(this)(session)

  def destroy()(implicit session: DBSession = ScoreItems.autoSession): Int = ScoreItems.destroy(this)(session)

}


object ScoreItems extends SQLSyntaxSupport[ScoreItems] {

  override val tableName = "score_items"

  override val columns = Seq("score_item_id", "corporate_id", "score_id", "score_item_name", "description", "score_item_value", "sort_no", "created_by", "created_at", "updated_by", "updated_at")

  def apply(si: SyntaxProvider[ScoreItems])(rs: WrappedResultSet): ScoreItems = apply(si.resultName)(rs)
  def apply(si: ResultName[ScoreItems])(rs: WrappedResultSet): ScoreItems = new ScoreItems(
    scoreItemId = rs.get(si.scoreItemId),
    corporateId = rs.get(si.corporateId),
    scoreId = rs.get(si.scoreId),
    scoreItemName = rs.get(si.scoreItemName),
    description = rs.get(si.description),
    scoreItemValue = rs.get(si.scoreItemValue),
    sortNo = rs.get(si.sortNo),
    createdBy = rs.get(si.createdBy),
    createdAt = rs.get(si.createdAt),
    updatedBy = rs.get(si.updatedBy),
    updatedAt = rs.get(si.updatedAt)
  )

  val si = ScoreItems.syntax("si")

  override val autoSession = AutoSession

  def find(scoreItemId: Long)(implicit session: DBSession = autoSession): Option[ScoreItems] = {
    withSQL {
      select.from(ScoreItems as si).where.eq(si.scoreItemId, scoreItemId)
    }.map(ScoreItems(si.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[ScoreItems] = {
    withSQL(select.from(ScoreItems as si)).map(ScoreItems(si.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(ScoreItems as si)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[ScoreItems] = {
    withSQL {
      select.from(ScoreItems as si).where.append(where)
    }.map(ScoreItems(si.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[ScoreItems] = {
    withSQL {
      select.from(ScoreItems as si).where.append(where)
    }.map(ScoreItems(si.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(ScoreItems as si).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    scoreItemId: Long,
    corporateId: Long,
    scoreId: Long,
    scoreItemName: String,
    description: Option[String] = None,
    scoreItemValue: BigDecimal,
    sortNo: Int,
    createdBy: Long,
    createdAt: LocalDateTime,
    updatedBy: Long,
    updatedAt: LocalDateTime)(implicit session: DBSession = autoSession): ScoreItems = {
    withSQL {
      insert.into(ScoreItems).namedValues(
        column.scoreItemId -> scoreItemId,
        column.corporateId -> corporateId,
        column.scoreId -> scoreId,
        column.scoreItemName -> scoreItemName,
        column.description -> description,
        column.scoreItemValue -> scoreItemValue,
        column.sortNo -> sortNo,
        column.createdBy -> createdBy,
        column.createdAt -> createdAt,
        column.updatedBy -> updatedBy,
        column.updatedAt -> updatedAt
      )
    }.update.apply()

    ScoreItems(
      scoreItemId = scoreItemId,
      corporateId = corporateId,
      scoreId = scoreId,
      scoreItemName = scoreItemName,
      description = description,
      scoreItemValue = scoreItemValue,
      sortNo = sortNo,
      createdBy = createdBy,
      createdAt = createdAt,
      updatedBy = updatedBy,
      updatedAt = updatedAt)
  }

  def batchInsert(entities: collection.Seq[ScoreItems])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("scoreItemId") -> entity.scoreItemId,
        Symbol("corporateId") -> entity.corporateId,
        Symbol("scoreId") -> entity.scoreId,
        Symbol("scoreItemName") -> entity.scoreItemName,
        Symbol("description") -> entity.description,
        Symbol("scoreItemValue") -> entity.scoreItemValue,
        Symbol("sortNo") -> entity.sortNo,
        Symbol("createdBy") -> entity.createdBy,
        Symbol("createdAt") -> entity.createdAt,
        Symbol("updatedBy") -> entity.updatedBy,
        Symbol("updatedAt") -> entity.updatedAt))
    SQL("""insert into score_items(
      score_item_id,
      corporate_id,
      score_id,
      score_item_name,
      description,
      score_item_value,
      sort_no,
      created_by,
      created_at,
      updated_by,
      updated_at
    ) values (
      {scoreItemId},
      {corporateId},
      {scoreId},
      {scoreItemName},
      {description},
      {scoreItemValue},
      {sortNo},
      {createdBy},
      {createdAt},
      {updatedBy},
      {updatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: ScoreItems)(implicit session: DBSession = autoSession): ScoreItems = {
    withSQL {
      update(ScoreItems).set(
        column.scoreItemId -> entity.scoreItemId,
        column.corporateId -> entity.corporateId,
        column.scoreId -> entity.scoreId,
        column.scoreItemName -> entity.scoreItemName,
        column.description -> entity.description,
        column.scoreItemValue -> entity.scoreItemValue,
        column.sortNo -> entity.sortNo,
        column.createdBy -> entity.createdBy,
        column.createdAt -> entity.createdAt,
        column.updatedBy -> entity.updatedBy,
        column.updatedAt -> entity.updatedAt
      ).where.eq(column.scoreItemId, entity.scoreItemId)
    }.update.apply()
    entity
  }

  def destroy(entity: ScoreItems)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(ScoreItems).where.eq(column.scoreItemId, entity.scoreItemId) }.update.apply()
  }

}
