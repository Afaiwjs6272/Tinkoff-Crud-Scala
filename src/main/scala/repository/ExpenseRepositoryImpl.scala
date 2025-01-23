package repository

import doobie.ConnectionIO
import doobie.implicits.toSqlInterpolator
import model.db.Expense

class ExpenseRepositoryImpl extends ExpenseRepository {
  override def create(expense: Expense): ConnectionIO[Int] =
    sql"INSERT INTO expenses (name, amount) VALUES (${expense.name}, ${expense.amount})".update.run

  override def getAll: ConnectionIO[List[Expense]] =
    sql"SELECT id, name, amount FROM expenses".query[Expense].to[List]

  override def getById(id: Int): ConnectionIO[Option[Expense]] =
    sql"SELECT id, name, amount FROM expenses WHERE id = $id".query[Expense].option

  override def update(expense: Expense): ConnectionIO[Int] =
    sql"UPDATE expenses SET name = ${expense.name}, amount = ${expense.amount} WHERE id = ${expense.id}".update.run

  override def delete(id: Int): ConnectionIO[Int] =
    sql"DELETE FROM expenses WHERE id = $id".update.run
}
