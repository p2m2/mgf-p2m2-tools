package fr.inrae.p2m2.parser

import fr.inrae.p2m2.format.MGFFeaturesIon

import scala.util.matching.Regex

case object MGF {
  /**
   * Arg 1 : Option[String] => current key (Features) to fill the Map
   * Arg 2 : Map[String, Map[String,String] ] Map of properties for each features of the MGF
   * Arg 3 : List of Fragment ions
   */
  private type FeaturesBufferWithCurrentKey = (Option[String], Map[String, Map[String, String]], Map[String, Seq[(Double, Double)]])

  /**
   *
   * @param lines : MGF lines to parse
   * @param listener : Function listener event that take (number of read line )
   * @return
   */
  def parse(lines: Seq[String], listener : ((Int) => Unit) = ( _: Int) => {} ): Seq[MGFFeaturesIon] = {
    // buffer key to store properties of the current features during parsing
    val stringIdCurrentFeature = "current_feature"

    val initParsing: FeaturesBufferWithCurrentKey = (None, Map(), Map())

    val msAndIntensity: Regex = "([0-9.]+)\\s([0-9.E]+)".r

    val parseResults = lines.
      zipWithIndex.
      foldLeft(initParsing)(
        (currentKeyAndFeatures: FeaturesBufferWithCurrentKey, lineAndIdx: (String, Int) ) => {
          {
            val line = lineAndIdx._1
            val idx = lineAndIdx._2

            currentKeyAndFeatures match {

              case (v, mapPropertiesByIDFeatures , f) if line.trim.toLowerCase.contains("begin ions") =>
                (v,(mapPropertiesByIDFeatures - stringIdCurrentFeature) + (stringIdCurrentFeature -> Map()),f)

              case (Some(feature), mapPropertiesByIDFeatures, f) if line.trim.toLowerCase.contains("end ions") =>
                listener(idx)
                (None,
                  (mapPropertiesByIDFeatures - stringIdCurrentFeature) +
                    (feature -> mapPropertiesByIDFeatures(stringIdCurrentFeature)), f)

              case (_, mapPropertiesByIDFeatures , fragIons) if line.trim.toLowerCase.contains("feature_id=") =>
                val feature = line.split("=")(1).trim
                (Some(feature), mapPropertiesByIDFeatures , fragIons + (feature -> Seq()))

              case (opt, mapPropertiesByIDFeatures, fragIons) if line.trim.contains("=") =>

                val k = line.split("=")(0).trim.toLowerCase
                val v = line.split("=")(1).trim
                (opt,
                  (mapPropertiesByIDFeatures - stringIdCurrentFeature) +
                    (stringIdCurrentFeature -> (mapPropertiesByIDFeatures(stringIdCurrentFeature) + (k -> v))),
                  fragIons)

              case (Some(feature), mapPropertiesByIDFeatures, fragIons) if msAndIntensity.matches(line.trim) =>
                try {
                  line.trim match {
                    case s"${ms} ${intensity}" =>
                      (Some(feature),
                        mapPropertiesByIDFeatures,
                        (fragIons - feature) + (feature -> (fragIons(feature) :+ (ms.toDouble, intensity.toDouble))))
                    case s"${ms}\n${intensity}" =>
                      (Some(feature),
                        mapPropertiesByIDFeatures,
                        (fragIons - feature) + (feature -> (fragIons(feature) :+ (ms.toDouble, intensity.toDouble))))
                    case _ => (Some(feature), mapPropertiesByIDFeatures, fragIons)
                  }
                } catch {
                  case e: Throwable => System.err.println(s"[mass and intensity fragment ions] parse error with line : ${line.trim} error:${e.getMessage}")
                    (Some(feature), mapPropertiesByIDFeatures, fragIons)
                }

              case (_, _, _) => currentKeyAndFeatures
            }
          }
        })
    (parseResults._2- stringIdCurrentFeature).map {
      case (feature, m) => MGFFeaturesIon(feature, m, parseResults._3(feature))
    }.toSeq
  }
}
