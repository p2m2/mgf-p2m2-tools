package fr.inrae.p2m2.format

case class MGFFeaturesIon(id : String, properties : Map[String, String], fragmentIons : Seq[(Double,Double)])
