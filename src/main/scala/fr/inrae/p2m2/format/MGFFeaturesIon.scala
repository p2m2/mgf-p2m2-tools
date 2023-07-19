package fr.inrae.p2m2.format

case class MGFFeaturesIon(feature : String, properties : Map[String, String], fragmentIons : Seq[(Double,Double)])
