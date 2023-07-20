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
      println("=================")
      println(f.id)
      println(PropertyIon.retentionTime(f))
      println(PropertyIon.pepMass(f))
      println("=================")

      val topFragSize = 300
      val tolMz = 0.05
      val tolRt = 2.0

      val r = CaptureIonFragmentSource.
        getFragmentSourcesFromFeature(f,l,topSizeFragmentation=topFragSize,toleranceMz = tolMz,toleranceRt = tolRt)
        r foreach {
          u => println(u.id,PropertyIon.pepMass(u),PropertyIon.retentionTime(u))
        }
      assert( r.nonEmpty)
      assert( r.filter( x =>
        (PropertyIon.pepMass(x).getOrElse(1000.0) - PropertyIon.pepMass(f).getOrElse(1.0)) <  tolMz ) == r
      )
      assert(r.filter(x =>
        (PropertyIon.retentionTime(x).getOrElse(1000.0) - PropertyIon.retentionTime(f).getOrElse(1.0)) < tolRt) == r
      )
    }
  }
}
