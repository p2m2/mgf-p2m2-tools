package fr.inrae.p2m2.parser
import fr.inrae.p2m2.ReadFile.rsc
import fr.inrae.p2m2.format.MGFFeaturesIon
import utest.{TestSuite, Tests, test}

object MGFTest extends TestSuite {

  def tests: Tests = Tests {
    test("ex1Parse") {
      val resource = rsc("src/test/resources/ex1.mgf")
      val lines: Seq[String] = resource.split("\n")
      val l : Seq[MGFFeaturesIon] = MGF.parse(lines)
      assert(l.size==1)
    }
    test("ex2Parse") {
      val resource = rsc("src/test/resources/ex2.mgf")
      val lines: Seq[String] = resource.split("\n")
      val l : Seq[MGFFeaturesIon] = MGF.parse(lines)
      assert(l.size==11)
    }
  }
}