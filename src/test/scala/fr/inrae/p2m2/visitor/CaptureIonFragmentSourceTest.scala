package fr.inrae.p2m2.visitor

import fr.inrae.p2m2.ReadFile.rsc
import fr.inrae.p2m2.format.MGFFeaturesIon
import fr.inrae.p2m2.parser.MGF
import utest.{TestSuite, Tests, test}

object CaptureIonFragmentSourceTest extends TestSuite {
  def tests: Tests = Tests {

    test("getFragmentSourcesFromFeature") {
      val resource = rsc("src/test/resources/ex3.mgf")
      val lines: Seq[String] = resource.split("\n")
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
