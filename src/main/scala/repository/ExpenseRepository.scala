package repository

import doobie._
import model.db.Expense

trait ExpenseRepository {
  def create(expense: Expense): ConnectionIO[Int]
  def getAll: ConnectionIO[List[Expense]]
  def getById(id: Int): ConnectionIO[Option[Expense]]
  def update(expense: Expense): ConnectionIO[Int]
  def delete(id: Int): ConnectionIO[Int]
}
