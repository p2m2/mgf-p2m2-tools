package fr.inrae.p2m2.visitor

import fr.inrae.p2m2.format.MGFFeaturesIon

case object StatsMGFFeaturesIon {
  def meanNumberOfIonsDiagnostics(list : Seq[MGFFeaturesIon] ): Double = list.size match {
    case s if s>0 =>
      list.map {
        case featureIon : MGFFeaturesIon => featureIon.fragmentIons.size.toDouble
      }.sum / list.size.toDouble
    case _ => 0
  }
}
