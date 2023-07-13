package fr.inrae.p2m2.parser
import utest.{TestSuite, Tests, test}
import scala.io.Source
object MGFTest extends TestSuite {

  def tests: Tests = Tests {
    val resource = Source.fromFile("src/test/resources/ex1.mgf")
    val lines : Iterator[String] = resource.getLines
    println(lines)
  }
}