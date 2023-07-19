package fr.inrae.p2m2.visitor

import fr.inrae.p2m2.format.MGFFeaturesIon
import fr.inrae.p2m2.parser.MGF
import utest.{TestSuite, Tests, test}

import scala.io.Source

object CaptureIonFragmentSourceTest extends TestSuite {
  def tests: Tests = Tests {
    test("getFragmentSourcesFromFeature") {
      val resource = Source.fromFile("src/test/resources/ex3.mgf")
      val lines: Iterator[String] = resource.getLines
      val l: Seq[MGFFeaturesIon] = (MGF.parse(lines))

      val f = l.filter(_.id=="Pos_1673").head
      println(f)
      println(PropertyIon.retentionTime(f))
      println(PropertyIon.pepMass(f))

      val r = CaptureIonFragmentSource.getFragmentSourcesFromFeature(f,l)
        r foreach {
          u => println(u.id,PropertyIon.pepMass(u),PropertyIon.retentionTime(u))
        }
    }
  }
}
