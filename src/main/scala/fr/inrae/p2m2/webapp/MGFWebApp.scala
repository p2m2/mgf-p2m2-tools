package fr.inrae.p2m2.webapp

import fr.inrae.p2m2.format.MGFFeaturesIon
import fr.inrae.p2m2.parser.MGF
import fr.inrae.p2m2.visitor.{CaptureIonFragmentSource, PropertyIon}
import org.scalajs.dom
import org.scalajs.dom.{FileReader, HTMLInputElement}
import org.scalajs.dom.html.{Div, Input, Progress}
import scalatags.JsDom
import scalatags.JsDom.all._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.scalajs.js.Math.sqrt
import scala.scalajs.js.URIUtils.encodeURIComponent
import scala.util.{Failure, Success}

object MGFWebApp {
  def readFileAsText (file : dom.File) (implicit ec: ExecutionContext) : Future[String] = {
    val p = Promise[String]()
    val fr = new FileReader()

    fr.onload = _ => {
      p.success(fr.result.toString)

    }

    fr.onerror = _ => {
      p.failure(new Exception())
    }

    fr.readAsText(file,"ISO-8859-1")
    p.future
  }

  def setGeneralStatistics(listFeatures: Seq[MGFFeaturesIon]) : Unit = {
    dom
      .document
      .getElementById("numberFeatures").asInstanceOf[Div].innerHTML = listFeatures.length.toString

    dom
      .document
      .getElementById("minPepmass").asInstanceOf[Div].innerHTML =
      listFeatures.flatMap(PropertyIon.pepMass).min.toString

    dom
      .document
      .getElementById("maxPepmass").asInstanceOf[Div].innerHTML =
      listFeatures.flatMap(PropertyIon.pepMass).max.toString

    dom
      .document
      .getElementById("minRt").asInstanceOf[Div].innerHTML =
      listFeatures.flatMap(PropertyIon.retentionTime).min.toString

    dom
      .document
      .getElementById("maxRt").asInstanceOf[Div].innerHTML =
      listFeatures.flatMap(PropertyIon.retentionTime).max.toString

    dom
      .document
      .getElementById("nbScans").asInstanceOf[Div].innerHTML =
      listFeatures.flatMap(PropertyIon.scans).distinct.length.toString

    dom
      .document
      .getElementById("msLevelList").asInstanceOf[Div].innerHTML =
      listFeatures.flatMap(PropertyIon.msLevel).distinct.mkString(",")

    dom
      .document
      .getElementById("chargeList").asInstanceOf[Div].innerHTML =
      listFeatures.flatMap(PropertyIon.charge).distinct.mkString(",")

    val l = listFeatures.map(_.fragmentIons.length)

    val mean = (l.sum.toDouble / (l.length+1))
    val std = sqrt(l.map( x => (x - mean) * (x - mean) ).sum / (l.length+1))

    dom
      .document
      .getElementById("meanFragIon").asInstanceOf[Div].innerHTML = mean.toString
    dom
      .document
      .getElementById("stdFragIon").asInstanceOf[Div].innerHTML = std.toString

    dom
      .document
      .getElementById("minMzFragIon").asInstanceOf[Div].innerHTML =
      listFeatures.map(_.fragmentIons.map(_._1).min).min.toString

    dom
      .document
      .getElementById("maxMzFragIon").asInstanceOf[Div].innerHTML =
      listFeatures.map(_.fragmentIons.map(_._1).max).max.toString

    dom
      .document
      .getElementById("minAbundanceFragIon").asInstanceOf[Div].innerHTML =
      listFeatures.map(_.fragmentIons.map(_._2).min).min.toString

    dom
      .document
      .getElementById("maxAbundanceFragIon").asInstanceOf[Div].innerHTML =
      listFeatures.map(_.fragmentIons.map(_._2).max).max.toString

  }

  def main(args: Array[String]): Unit = {

    val inputTag: JsDom.TypedTag[Input] = input(
      id := "inputFiles",
      `type` := "file",
      //multiple := "multiple",
      onchange := {
        (ev : dom.InputEvent) =>

          dom
            .document
            .getElementById("file").asInstanceOf[Progress].setAttribute("value", "0")

          val files = ev.currentTarget.asInstanceOf[HTMLInputElement].files

          if (files.nonEmpty) {
            dom
              .document
              .getElementById("log").innerText=""

            val lFutures = Future.sequence(files.map(f => readFileAsText(f) ))

            lFutures.onComplete {
              case Success(reportsGcmsInTextFormat : List[String]) =>
                val listSourceFragment : List [String] = reportsGcmsInTextFormat.flatMap {
                  fileContent =>
                    val textByLine: Seq[String] = fileContent.split("\n")

                    val listFeatures: Seq[MGFFeaturesIon] = MGF.parse(textByLine,
                      (nLineRead:Int) => {
                        val percent = ((nLineRead.toDouble / textByLine.length.toDouble)*100).toInt
                        //System.err.println(percent)
                        dom
                          .document
                          .getElementById("file").asInstanceOf[Progress].setAttribute("value", percent.toString)
                    })
                    setGeneralStatistics(listFeatures)
                    /*
                    listFeatures.flatMap {
                      feature =>
                        CaptureIonFragmentSource.getFragmentSourcesFromFeature(feature, listFeatures).map {
                          x => s"${feature.id},${PropertyIon.retentionTime(feature)},${x.id},${PropertyIon.retentionTime(x)}"
                        }
                    }*/
                    listFeatures.map(_.id)
                }
                /*
                a(
                  "IsoCor file", href := "data:text/tsv;name=isocor_gcms.tsv;charset=ISO-8859-1,"
                    + encodeURIComponent(listSourceFragment.mkString("\n"))).render.click()*/

              case Failure(e) =>
                System.err.println("failure :"+e.getMessage)
            }
          }

      }
    )
    dom
      .document
      .getElementById("inputFilesDiv")
      .append(inputTag.render)
  }
}
