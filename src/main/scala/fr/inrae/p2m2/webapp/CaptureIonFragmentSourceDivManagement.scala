package fr.inrae.p2m2.webapp

import fr.inrae.p2m2.format.MGFFeaturesIon
import fr.inrae.p2m2.visitor.{CaptureIonFragmentSource, PropertyIon}
import org.scalajs.dom
import org.scalajs.dom.html.Input
import scalatags.JsDom.all._

import scala.scalajs.js.URIUtils.encodeURIComponent

case object CaptureIonFragmentSourceDivManagement {
  def setup() {
    dom
      .document
      .getElementById("minAb").asInstanceOf[Input].value = (5000.0).toString

    dom
      .document
      .getElementById("minAb").asInstanceOf[Input].onchange = (_: dom.Event) => {clean()}

    dom
      .document
      .getElementById("tolMz").asInstanceOf[Input].value = (0.001).toString

    dom
      .document
      .getElementById("tolMz").asInstanceOf[Input].onchange = (_: dom.Event) => {clean()}

    dom
      .document
      .getElementById("tolRt").asInstanceOf[Input].value = (0.5).toString

    dom
      .document
      .getElementById("tolRt").asInstanceOf[Input].onchange = (_: dom.Event) => {clean()}
  }

  def clean(): Unit = {
    dom
      .document
      .getElementById("fragmentSourceDetectionResults")
      .remove()
    dom
      .document
      .getElementById("fragmentSourceDetection")
      .append(div(id := "fragmentSourceDetectionResults").render)
  }


}

case class  CaptureIonFragmentSourceDivManagement(listFeatures: Seq[MGFFeaturesIon]) {
  def setClickSubmitEvent(): Unit = {
    val header="FEATURE1;RT1;MZ1;FEATURE2;RT2;MZ2;"
    dom
      .document
      .getElementById("fragmentSourceDetectionSubmit").asInstanceOf[Input].onclick =
      (_: dom.Event) => {

        /* get parameters */
        val minAb = dom
          .document
          .getElementById("minAb").asInstanceOf[Input].value.toDouble
        val tolMz = dom
          .document
          .getElementById("tolMz").asInstanceOf[Input].value.toDouble
        val tolRt = dom
          .document
          .getElementById("tolRt").asInstanceOf[Input].value.toDouble

        println(minAb,tolMz)

        val listFS : Seq[String] = listFeatures.flatMap {
          feature =>
            CaptureIonFragmentSource.getFragmentSourcesFromFeature(
              feature,
              listFeatures,
              minAbundance=minAb,
              toleranceMz=tolMz,
              toleranceRt=tolRt
            ).map {
              x => s"${feature.id};" +
                s"${PropertyIon.retentionTime(feature).getOrElse(-1.0)};" +
                s"${PropertyIon.pepMass(feature).getOrElse(-1.0)};" +
                s"${x.id};" +
                s"${PropertyIon.retentionTime(x).getOrElse(-1.0)};" +
                s"${PropertyIon.pepMass(x).getOrElse(-1.0)}"
            }
        }

        dom
          .document
          .getElementById("fragmentSourceDetectionResults")
          .remove()
        dom
          .document
          .getElementById("fragmentSourceDetection")
          .append(
            div(
              id := "fragmentSourceDetectionResults",
              a(
                "MS_features_co_eluted", download := "co-eluted_features.tsv",
                href := "data:text/tsv;charset=ISO-8859-1,"
                  + encodeURIComponent(header+"\n"+listFS.mkString("\n")))
            ).render)
      }

  }
}
