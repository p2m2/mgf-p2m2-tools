package fr.inrae.p2m2.visitor

import fr.inrae.p2m2.format.MGFFeaturesIon
import utest.{TestSuite, Tests, test}

object PropertyIonTest extends TestSuite {
  def tests: Tests = Tests {
    test("PropertyIon with empty MGFFeaturesIon") {
      assert(PropertyIon.retentionTime(MGFFeaturesIon("some", Map(), Seq())).isEmpty)
      assert(PropertyIon.scans(MGFFeaturesIon("some", Map(), Seq())).isEmpty)
      assert(PropertyIon.charge(MGFFeaturesIon("some", Map(), Seq())).isEmpty)
      assert(PropertyIon.msLevel(MGFFeaturesIon("some", Map(), Seq())).isEmpty)
      assert(PropertyIon.pepMass(MGFFeaturesIon("some", Map(), Seq())).isEmpty)
    }
    test("PropertyIon with MGFFeaturesIon retentionTime") {
      assert(PropertyIon.retentionTime(MGFFeaturesIon("some", Map("RTINSECONDS".toLowerCase -> "0.5"), Seq())).contains(0.5))
    }
    test("PropertyIon with MGFFeaturesIon scans") {
      assert(PropertyIon.scans(MGFFeaturesIon("some", Map("SCANS".toLowerCase -> "10"), Seq())).contains(10))
    }
    test("PropertyIon with MGFFeaturesIon charge") {
      assert(PropertyIon.charge(MGFFeaturesIon("some", Map("CHARGE".toLowerCase -> "1-"), Seq())).contains("1-"))
    }
    test("PropertyIon with MGFFeaturesIon msLevel") {
      assert(PropertyIon.msLevel(MGFFeaturesIon("some", Map("MSLEVEL".toLowerCase -> "1"), Seq())).contains(1))
    }
    test("PropertyIon with MGFFeaturesIon pepMass") {
      assert(PropertyIon.pepMass(MGFFeaturesIon("some", Map("PEPMASS".toLowerCase -> "110.1"), Seq())).contains(110.1))
    }
  }
}
