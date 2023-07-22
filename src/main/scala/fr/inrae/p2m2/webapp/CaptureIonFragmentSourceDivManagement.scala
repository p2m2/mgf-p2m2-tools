package fr.inrae.p2m2.webapp

import fr.inrae.p2m2.format.MGFFeaturesIon
import fr.inrae.p2m2.visitor.{CaptureIonFragmentSource, PropertyIon}
import fr.inrae.p2m2.webapp.CaptureIonFragmentSourceDivManagement.clean
import org.scalajs.dom
import org.scalajs.dom.{Blob, BlobPropertyBag, Event, ProgressEvent, URL}
import org.scalajs.dom.html.Input
import scalatags.JsDom.all._

import scala.scalajs.js
import scala.scalajs.js.URIUtils.encodeURIComponent

case object CaptureIonFragmentSourceDivManagement {
  def setup() {
    dom
      .document
      .getElementById("minAb").asInstanceOf[Input].value = (4000.0).toString

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

  def fragmentSourcesFromFeature(): Unit = {

    /* get parameters */
    val minAb = dom
      .document
      .getElementById("minAb").asInstanceOf[Input].value.toDouble.abs
    val tolMz = dom
      .document
      .getElementById("tolMz").asInstanceOf[Input].value.toDouble.abs
    val tolRt = dom
      .document
      .getElementById("tolRt").asInstanceOf[Input].value.toDouble.abs

    val listFeaturesAndFragmentSource: Seq[(MGFFeaturesIon,Seq[MGFFeaturesIon])] = listFeatures.flatMap {
      feature =>
        CaptureIonFragmentSource.getFragmentSourcesFromFeature(
          feature,
          listFeatures,
          minAbundance = minAb,
          toleranceMz = tolMz,
          toleranceRt = tolRt
        ) match {
          case l if l.nonEmpty => Some(feature,l)
          case _ => None
        }
    }.sortBy(x => PropertyIon.pepMass(x._1)).reverse

    val listFS : Seq[String] = listFeaturesAndFragmentSource.flatMap {
      case (feature, lx) =>
          lx.map {
            x =>
              s"${feature.id};" +
                s"${PropertyIon.retentionTime(feature).getOrElse(-1.0)};" +
                s"${PropertyIon.pepMass(feature).getOrElse(-1.0)};" +
                s"${x.id};" +
                s"${PropertyIon.retentionTime(x).getOrElse(-1.0)};" +
                s"${PropertyIon.pepMass(x).getOrElse(-1.0)}"
          }
    }

    P2M2Js.waitBlock(false)

    clean()

    val header="FEATURE1;RT1;MZ1;FEATURE2;RT2;MZ2;"
    val fragmentSourceDetection = dom.document.getElementById("fragmentSourceDetection")

    fragmentSourceDetection
      .append(
        div(
          id := "fragmentSourceDetectionResults",
          a(
            "MS_features_co_eluted", download := "co-eluted_features.tsv",
            href := "data:text/tsv;charset=ISO-8859-1,"
              + encodeURIComponent(header + "\n" + listFS.mkString("\n")))
        ).render)

    fragmentSourceDetection
      .append(
        table(
          //style := "border: 1px solid black;",
          `class` := "results",
          tr( th("FEATURE"), th("RTINSECONDS"),th("PEPMASS"),th("FRAG. SOURCE") ),
            listFeaturesAndFragmentSource.map {
              case (feature, lx) =>
                val winHtml = "<!DOCTYPE html>\n" + MGFFeaturesIonDivInformation.information(feature).outerHTML
                val winUrl = URL.createObjectURL(
                  new Blob(js.Array(winHtml),new BlobPropertyBag { `type` -> "text/html"}))

                tr(
                  td(
                    a(href:=winUrl,target:="_blank" , feature.id)),
                  td(PropertyIon.retentionTime(feature)),td(PropertyIon.pepMass(feature))
                  ,td(lx.map( x => {
                    val winHtml = "<!DOCTYPE html>\n" + MGFFeaturesIonDivInformation.information(x).outerHTML
                    val winUrl = URL.createObjectURL(
                      new Blob(js.Array(winHtml), new BlobPropertyBag {
                        `type` -> "text/html"
                      }))
                    a(href:=winUrl,target:="_blank" , x.id)
                  }))
                )
            }
        ).render)

  }

  def setClickSubmitEvent(): Unit = {
    val inputTag = dom
      .document
      .getElementById("fragmentSourceDetectionSubmit").asInstanceOf[Input]

    inputTag.addEventListener("click", (_:Event) => { P2M2Js.waitBlock(true) })
    inputTag.addEventListener("click", (_:Event) => { fragmentSourcesFromFeature() })

  }
}
