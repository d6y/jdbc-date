import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.PostgresProfile.api._
import java.time._
import java.time.format._

/**
  *
  * An example of a corner-case of storing a LocalDate with Slick and Postgres.
  *
  * Assumes:
  * - a Postgress database defined in src/main/resources/application.conf
  * - a table:
  *   ```
  *   create table "foo" ( "dob" DATE );
  *   ```
  */
object Example extends App {

  // There is no year zero, however LocalDate supports it:
  val example = LocalDate.of(0, 12, 31)

  // We're going to write a row to the database and then read it back
  //
  // This code is adapted from
  // https://github.com/slick/slick/blob/v3.3.0/slick/src/main/scala/slick/jdbc/PostgresProfile.scala#L311-L330

  // Insert the example value to Postgres using the Slick formatter:
  val jdbcInsert = SimpleDBIO[Int](db => {
    val sql = """ insert into "foo" values (?) """
    val ps = db.connection.prepareStatement(sql)
    val v = DateTimeFormatter.ISO_LOCAL_DATE.format(example)
    println(s"LocalDate formatted value is: $v")
    ps.setObject(1, v, java.sql.Types.DATE)
    println(s"SQL insert is: $ps")
    ps.executeUpdate()
  })

  // Read back the value as a string:
  val jdbcQuery = SimpleDBIO[String](database => {
    val sql = """ select "dob" from "foo" """
    val ps = database.connection.prepareStatement(sql)
    try {
      val rs = ps.executeQuery()
      if (rs.next) rs.getString(1) else "no value"
    } finally ps.close
  })

  val program = for {
    _ <- sqlu""" delete from "foo" """
    _ <- jdbcInsert
    read <- jdbcQuery
  } yield read

  val db = Database.forConfig("example")
  try {
    val readBackValue: String = Await.result(db.run(program), 2.seconds)
    println(s"The value read back is: $readBackValue")
    val parsed = scala.util.Try {
      DateTimeFormatter.ISO_LOCAL_DATE.parse(readBackValue)
    }
    println(s"Parsing value produces: $parsed")
  } finally db.close
}
