package controller

import cats.effect._
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.io._
import service.ExpenseService
import model.http.ExpenseDTO
import model.db.Expense

class ExpenseController(expenseService: ExpenseService) {

  val serviceRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {

    case req @ POST -> Root / "expense" =>
      for {
        expenseDTO <- req.as[ExpenseDTO]
        expense = Expense(0, expenseDTO.name, expenseDTO.amount)
        _ <- expenseService.createExpense(expense)
        resp <- Ok(s"Expense created: ${expense.name}")
      } yield resp

    case GET -> Root / "expense" =>
      for {
        expenses <- expenseService.getAllExpenses()
        resp <- Ok(expenses)
      } yield resp

    case GET -> Root / "expense" / IntVar(id) =>
      expenseService.getExpenseById(id).flatMap {
        case Some(expense) => Ok(expense)
        case None          => NotFound(s"Expense with ID $id not found")
      }

    case req @ PUT -> Root / "expense" / IntVar(id) =>
      for {
        expenseDTO <- req.as[ExpenseDTO]
        expense = Expense(id, expenseDTO.name, expenseDTO.amount)
        updated <- expenseService.updateExpense(expense)
        resp <-
          if (updated > 0) Ok(s"Expense updated: ${expense.name}")
          else NotFound(s"Expense with ID $id not found")
      } yield resp

    case DELETE -> Root / "expense" / IntVar(id) =>
      for {
        deleted <- expenseService.deleteExpense(id)
        resp <-
          if (deleted > 0) Ok(s"Expense with ID $id deleted")
          else NotFound(s"Expense with ID $id not found")
      } yield resp
  }

  def routes: HttpRoutes[IO] = serviceRoutes
}
