package controller

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.implicits._
import org.scalatest.funsuite.AnyFunSuite
import service.ExpenseService
import io.circe.generic.auto._
import io.circe.syntax._
import model.db.Expense

class ExpenseControllerSpec extends AnyFunSuite {

  val mockService = new ExpenseService() {
    override def getAllExpenses(): IO[List[Expense]] =
      IO.pure(List(Expense(1, "Test Expense", 100.50)))

    override def getExpenseById(id: Int): IO[Option[Expense]] =
      IO.pure(Some(Expense(id, "Test Expense", 100.50)))

    override def createExpense(expense: Expense): IO[Int] =
      IO.pure(1)

    override def updateExpense(expense: Expense): IO[Int] =
      IO.pure(1)

    override def deleteExpense(id: Int): IO[Int] =
      IO.pure(1)
  }

  val controller = new ExpenseController(mockService)
  val routes = controller.routes.orNotFound

  test("GET /expense returns all expenses") {
    val request = Request[IO](Method.GET, uri"/expense")
    val response = routes.run(request).unsafeRunSync()

    assert(response.status == Status.Ok)

    val expenses = response.as[List[Expense]].unsafeRunSync()
    assert(expenses.nonEmpty)
    assert(expenses.head == Expense(1, "Test Expense", 100.50))
  }

  test("GET /expense/:id returns specific expense") {
    val request = Request[IO](Method.GET, uri"/expense/1")
    val response = routes.run(request).unsafeRunSync()

    assert(response.status == Status.Ok)

    val expense = response.as[Expense].unsafeRunSync()
    assert(expense.name == "Test Expense")
    assert(expense.amount == 100.50)
  }

  test("POST /expense creates a new expense") {
    val newExpense = Expense(1, "New Expense", 200.75)
    val request = Request[IO](Method.POST, uri"/expense").withEntity(newExpense.asJson)
    val response = routes.run(request).unsafeRunSync()

    assert(response.status == Status.Ok)
    val responseBody = response.as[String].unsafeRunSync()
    assert(responseBody.contains("created"))
  }

  test("PUT /expense/:id updates an expense") {
    val updatedExpense = Expense(1, "Updated Expense", 150.00)
    val request = Request[IO](Method.PUT, uri"/expense/1").withEntity(updatedExpense.asJson)
    val response = routes.run(request).unsafeRunSync()

    assert(response.status == Status.Ok)
    val responseBody = response.as[String].unsafeRunSync()
    assert(responseBody.contains("updated"))
  }

  test("DELETE /expense/:id deletes an expense") {
    val request = Request[IO](Method.DELETE, uri"/expense/1")
    val response = routes.run(request).unsafeRunSync()

    assert(response.status == Status.Ok)
    val responseBody = response.as[String].unsafeRunSync()
    assert(responseBody.contains("deleted"))
  }
}
