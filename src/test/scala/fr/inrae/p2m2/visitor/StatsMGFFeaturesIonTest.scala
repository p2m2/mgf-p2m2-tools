package fr.inrae.p2m2.visitor

import fr.inrae.p2m2.format.MGFFeaturesIon
import utest.{TestSuite, Tests, test}

object StatsMGFFeaturesIonTest extends TestSuite {
  def tests: Tests = Tests {
    test("StatsMGFFeaturesIon size=0") {
      assert( StatsMGFFeaturesIon.meanNumberOfIonsDiagnostics(Seq()) == 0)
    }
    test("StatsMGFFeaturesIon size=0") {
      assert( StatsMGFFeaturesIon.meanNumberOfIonsDiagnostics(Seq(MGFFeaturesIon("test1", Map(), Seq()))) == 0.0)
    }
    test("StatsMGFFeaturesIon size=1 1") {
      assert( StatsMGFFeaturesIon.meanNumberOfIonsDiagnostics(Seq(MGFFeaturesIon("test1", Map(), Seq((2.0,2.0))))) == 1.0 )
    }
    test("StatsMGFFeaturesIon size=2 1,1") {
      assert( StatsMGFFeaturesIon.meanNumberOfIonsDiagnostics(Seq(MGFFeaturesIon("test1", Map(), Seq((2.0, 2.0))),
        MGFFeaturesIon("test2", Map(), Seq((2.0, 2.0))))) == 1.0 )
    }
    test("StatsMGFFeaturesIon size=2 2,1") {
      assert( StatsMGFFeaturesIon.meanNumberOfIonsDiagnostics(Seq(MGFFeaturesIon("test1", Map(),
        Seq((2.0, 2.0),(2.0, 2.0))), MGFFeaturesIon("test2", Map(), Seq((2.0, 2.0))))) == 1.5)
    }
  }

}
