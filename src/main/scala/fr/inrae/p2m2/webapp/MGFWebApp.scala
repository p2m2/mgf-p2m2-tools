package fr.inrae.p2m2.webapp

import fr.inrae.p2m2.format.MGFFeaturesIon
import fr.inrae.p2m2.parser.MGF
import fr.inrae.p2m2.visitor.{CaptureIonFragmentSource, PropertyIon}
import org.scalajs.dom
import org.scalajs.dom.{FileReader, HTMLInputElement}
import org.scalajs.dom.html.Input
import scalatags.JsDom
import scalatags.JsDom.all._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future, Promise}
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

  def main(args: Array[String]): Unit = {

    val inputTag: JsDom.TypedTag[Input] = input(
      id := "inputFiles",
      `type` := "file",
      multiple := "multiple",
      onchange := {
        (ev : dom.InputEvent) =>
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
                    val listFeatures: Seq[MGFFeaturesIon] = MGF.parse(textByLine)
                    /*
                    listFeatures.flatMap {
                      feature =>
                        CaptureIonFragmentSource.getFragmentSourcesFromFeature(feature, listFeatures).map {
                          x => s"${feature.id},${PropertyIon.retentionTime(feature)},${x.id},${PropertyIon.retentionTime(x)}"
                        }
                    }*/
                    listFeatures.map(_.id)
                }
                a(
                  "IsoCor file", href := "data:text/tsv;name=isocor_gcms.tsv;charset=ISO-8859-1,"
                    + encodeURIComponent(listSourceFragment.mkString("\n"))).render.click()

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
