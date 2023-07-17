package fr.inrae.p2m2.parser

import fr.inrae.p2m2.format.MGFFeaturesIon
import fr.inrae.p2m2.parser.MGF.FeaturesBufferWithCurrentKey

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
  private type FeaturesBufferWithCurrentKey = (Option[String], Map[String, Map[String, String]])
  def parse2(lines: Iterator[String]): Seq[MGFFeaturesIon] = {
    val initParsing : FeaturesBufferWithCurrentKey = (None, Map())

    lines.
      foldLeft ( initParsing ) (
        (currentKeyAndFeatures : FeaturesBufferWithCurrentKey, line : String)  => {

        val r = { currentKeyAndFeatures match {
              case (None,l) if line.contains("FEATURE_ID=") => (Some(line.split("=")(1)),l)
              case _ =>  currentKeyAndFeatures
            }
          }
          r
        })
    Seq()
  }
}
