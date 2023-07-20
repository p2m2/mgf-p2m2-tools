package fr.inrae.p2m2.visitor

import fr.inrae.p2m2.format.MGFFeaturesIon

import scala.math.abs

case object CaptureIonFragmentSource {
  /**
   * Returns MGF Features corresponding to the top 20 MGF fragment ions of interest
   *
   * Carateristic :
   * - reference feature : MSLEVEL=1 => to compare with Ion Fragment Features with MSLEVEL=2
   * - same charge
   * @param feature Feature of interest (reference)
   * @param allFeatures List of Features to compare
   * @param topSizeFragmentation compare with the top X of fragment ion of the feature reference
   * @param toleranceMz tolerance mz when comparing rt of fragment ion and the feature reference
   * @return the list of Source Fragment
   */
  def getFragmentSourcesFromFeature(
                        feature : MGFFeaturesIon,
                        allFeatures : Seq[MGFFeaturesIon],
                        minAbundance : Double = 2000.0,
                        toleranceMz : Double = 0.05,
                        toleranceRt : Double = 2.0, //1 second
                       ) : Seq[MGFFeaturesIon] = {

    // sort by intensity
    feature.fragmentIons.filter(_._2>=minAbundance) flatMap {
      case ( mz: Double, _ : Double) =>
        allFeatures
          .filter( curFeat => PropertyIon.charge(curFeat) == PropertyIon.charge(feature))
          .filter( curFeat => PropertyIon.pepMass(curFeat).getOrElse(100000.0) < PropertyIon.pepMass(feature).getOrElse(1.0))
          .filter( curFeat => PropertyIon.msLevel(curFeat).contains(2))
          .filter( curFeat =>
            abs(PropertyIon.retentionTime(curFeat).getOrElse(-1.0) - PropertyIon.retentionTime(feature).getOrElse(-100.0))<toleranceRt )
          .filter(curFeat => abs( PropertyIon.pepMass(curFeat).getOrElse(-1000.0) - mz ) < toleranceMz)
    }
  }
}
