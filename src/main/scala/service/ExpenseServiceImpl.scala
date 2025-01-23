package service

import cats.effect.IO
import doobie.Transactor
import doobie.implicits._
import model.db.Expense
import repository.ExpenseRepository

class ExpenseServiceImpl(expenseRepo: ExpenseRepository, xa: Transactor[IO]) extends ExpenseService {
  override def createExpense(expense: Expense): IO[Int] =
    expenseRepo.create(expense).transact(xa)

  override def getAllExpenses(): IO[List[Expense]] =
    expenseRepo.getAll.transact(xa)

  override def getExpenseById(id: Int): IO[Option[Expense]] =
    expenseRepo.getById(id).transact(xa)

  override def updateExpense(expense: Expense): IO[Int] =
    expenseRepo.update(expense).transact(xa)

  override def deleteExpense(id: Int): IO[Int] =
    expenseRepo.delete(id).transact(xa)
}
