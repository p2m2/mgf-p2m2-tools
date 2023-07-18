package fr.inrae.p2m2.parser

import fr.inrae.p2m2.format.MGFFeaturesIon
import fr.inrae.p2m2.parser.MGF.FeaturesBufferWithCurrentKey

import scala.collection.Iterator
import scala.util.matching.Regex

case object MGF {

  def parse(lines: Iterator[String]): Seq[MGFFeaturesIon] = {
    lines.
      zipWithIndex.
      filter {
        case (elt, _) => elt.trim.toLowerCase.contains("begin ions")
      } map {
      case (_, idx) => idx // get All idx line
    }
    Seq()
  }

  /**
   * Arg 1 : Option[String] => current key (Features) to fill the Map
   * Arg 2 : Map[String, Map[String,String] ] Map of properties for each features of the MGF
   * Arg 3 : List of Fragment ions
   */
  private type FeaturesBufferWithCurrentKey = (Option[String], Map[String, Map[String, String]], Map[String, Seq[(Double, Double)]])

  def parse2(lines: Iterator[String]): Seq[MGFFeaturesIon] = {
    val initParsing: FeaturesBufferWithCurrentKey = (None, Map(), Map())

    val msAndIntensity: Regex = "([0-9\\.]+)\\s([0-9\\.E]+)".r

    val parseResults = lines.
      foldLeft(initParsing)(
        (currentKeyAndFeatures: FeaturesBufferWithCurrentKey, line: String) => {
          {
            currentKeyAndFeatures match {
              case (None, l, fragIons) if line.trim.contains("FEATURE_ID=") =>
                val feature = line.split("=")(1)
                (Some(feature), l + (feature -> Map()), fragIons + (feature -> Seq()))

              case (Some(feature), l, fragIons) if line.trim.contains("=") =>

                val k = line.split("=")(0)
                val v = line.split("=")(1)
                (Some(feature), (l - feature) + (feature -> (l(feature) + (k -> v))), fragIons)

              case (Some(feature), l, fragIons) if msAndIntensity.matches(line.trim) =>
                try {
                  line.trim match {
                    case s"${ms} ${intensity}" => (Some(feature), l, (fragIons - feature) + (feature -> (fragIons(feature) :+ (ms.toDouble, intensity.toDouble))))
                    case s"${ms}\n${intensity}" => (Some(feature), l, (fragIons - feature) + (feature -> (fragIons(feature) :+ (ms.toDouble, intensity.toDouble))))
                    case _ => (Some(feature), l, fragIons)
                  }
                } catch {
                  case e: Throwable => System.err.println(s"[mass and intensity fragment ions] parse error with line : ${line.trim} error:${e.getMessage}")
                    (Some(feature), l, fragIons)
                }

              case (_, _, _) => currentKeyAndFeatures
            }
          }
        })
    parseResults._2.map {
      case (feature, m) => MGFFeaturesIon(feature, m, parseResults._3(feature))
    }.toSeq
  }
}
