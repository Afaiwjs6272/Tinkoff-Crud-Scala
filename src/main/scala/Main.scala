import cats.effect.{ExitCode, IO, IOApp, Resource}
import controller.ExpenseController
import doobie.hikari.HikariTransactor
import service.ExpenseServiceImpl
import repository.ExpenseRepositoryImpl
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import org.http4s.HttpApp
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Server
import org.http4s.implicits._

object Main extends IOApp {

  def transactorResource: Resource[IO, HikariTransactor[IO]] =
    for {
      connectEC <- ExecutionContexts.fixedThreadPool[IO](size = 32)
      transactor <- HikariTransactor.newHikariTransactor[IO](
        "org.postgresql.Driver",
        "jdbc:postgresql://localhost:5433/expense",
        "postgres",
        "password",
        connectEC
      )
    } yield transactor

  def serverResource(xa: Transactor[IO]): Resource[IO, Server] = {
    val expenseRepo = new ExpenseRepositoryImpl
    val expenseService = new ExpenseServiceImpl(expenseRepo, xa)
    val expenseController = new ExpenseController(expenseService)
    val httpApp: HttpApp[IO] = expenseController.routes.orNotFound

    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(httpApp)
      .resource
  }

  override def run(args: List[String]): IO[ExitCode] =
    transactorResource
      .use { xa =>
        serverResource(xa).use(_ => IO.never)
      }
      .as(ExitCode.Success)
}
