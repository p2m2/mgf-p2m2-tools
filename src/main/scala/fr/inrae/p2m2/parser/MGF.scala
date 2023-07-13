package fr.inrae.p2m2.parser

import fr.inrae.p2m2.format.MGFFeaturesIon

import scala.collection.Iterator

case object MGF {

  def parse(lines : Iterator[String]) : Seq[MGFFeaturesIon] = {
    lines.
      zipWithIndex.
      filter {
        case (elt,_) => elt.trim.toLowerCase.contains("begin ions")
      } map {
      case (_,idx) => idx // get All idx line
    }
    Seq()
  }
}
