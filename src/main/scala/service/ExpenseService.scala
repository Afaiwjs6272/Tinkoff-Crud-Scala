package service

import cats.effect.IO
import model.db.Expense

trait ExpenseService {
  def createExpense(expense: Expense): IO[Int]
  def getAllExpenses(): IO[List[Expense]]
  def getExpenseById(id: Int): IO[Option[Expense]]
  def updateExpense(expense: Expense): IO[Int]
  def deleteExpense(id: Int): IO[Int]
}
