package fr.inrae.p2m2.webapp

import fr.inrae.p2m2.format.MGFFeaturesIon
import fr.inrae.p2m2.parser.MGF
import fr.inrae.p2m2.webapp.GeneralStatisticsHtmlDivManagement.setGeneralStatistics
import org.scalajs.dom
import org.scalajs.dom.html.{Div, Input, Progress}
import org.scalajs.dom.{Event, FileReader, HTMLInputElement}
import org.scalajs.dom.ProgressEvent
import scalatags.JsDom
import scalatags.JsDom.all._

import scala.annotation.unused
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success}

object MGFWebApp {
  def readFileAsText (file : dom.File) (implicit @unused ec: ExecutionContext) : Future[String] = {
    P2M2Js.waitBlock(true)
    val p = Promise[String]()
    val fr = new FileReader()

    fr.onload = _ => {
      p.success(fr.result.toString)
    }

    fr.onerror = _ => {
      System.err.println(s"** Can not parse file ** ")
      p.failure(new Exception())
    }

    fr.onloadstart = _ => P2M2Js.waitBlock(true)

    fr.onloadend = _ => P2M2Js.waitBlock(false)

    fr.readAsText(file,"ISO-8859-1")
    p.future
  }

  private def setLog() : Unit = {
    val el = dom
      .document
      .getElementById("log")

    if (el != null) el.innerText = ""

  }
  def main(args: Array[String]): Unit = {
/*
    val f1 = MGFFeaturesIon("Neg_2", Map("rtinseconds" -> "271.108", "pepmass" -> "477.0647", "scans" -> "2",
      "mslevel" -> "2", "charge" -> "1-")
      , List((95.8718, 38), (95.8909, 60), (95.9089, 84), (95.9298, 86), (95.9523, 18000), (95.9889, 58), (96.0093, 68), (96.0453, 140), (96.0593, 140), (96.0816, 120), (96.1183, 72), (96.1423, 48), (96.1757, 60), (96.1963, 54), (96.2251, 100), (96.2484, 62), (96.2625, 72), (96.2839, 60), (96.3076, 86), (96.3314, 56), (96.344, 110), (96.3623, 120), (96.3758, 72), (96.3959, 32), (96.4101, 68), (96.4322, 68), (96.4717, 68), (96.559, 44), (96.6113, 88), (96.6599, 120), (96.6752, 34), (96.7004, 52), (96.7152, 42), (96.7762, 80), (96.8041, 58), (96.8186, 30), (96.8414, 24), (96.8608, 46), (96.8698, 60), (96.881, 76), (96.8973, 170), (96.9309, 150), (96.9601, 51000), (96.9939, 92), (97.0093, 200), (97.0283, 240), (97.0416, 130), (97.0567, 110), (97.0751, 130), (97.0998, 160), (97.1317, 240), (97.1724, 150), (97.1885, 82), (97.2, 140), (97.2121, 180), (97.2398, 120), (97.2687, 230), (97.2905, 150), (97.3184, 180), (97.3446, 180), (97.3729, 190), (97.3859, 100), (97.4126, 78), (97.437, 38), (97.4555, 46), (97.4711, 150), (97.4808, 90), (97.4978, 44), (97.5138, 94), (97.5383, 94), (97.5544, 94), (97.5871, 110), (97.6143, 130), (97.6323, 100), (97.6528, 64), (97.6797, 66), (97.7158, 64), (97.7274, 84), (97.7477, 58), (97.7848, 82), (97.8284, 26), (97.8474, 36), (97.8597, 74), (97.9262, 80), (97.9485, 380), (97.9591, 450), (97.9826, 64), (98.0049, 20), (98.0504, 48), (98.0796, 26), (98.091, 38), (98.1154, 24), (98.1671, 100), (98.21, 52), (98.26, 40), (98.2821, 68), (98.2995, 56), (98.3236, 58), (98.3672, 48), (98.4502, 72), (98.4875, 40), (98.6722, 84), (98.6909, 26), (98.7099, 36), (98.7467, 24), (98.9382, 54), (98.9571, 1200), (99.0647, 100), (99.0979, 64), (99.1396, 66), (99.1626, 46), (99.1921, 80), (99.213, 58), (99.2411, 56), (99.2554, 38), (99.2729, 58), (99.3227, 50), (99.3821, 66), (99.5651, 24), (99.8057, 36), (100.2636, 36), (100.279, 44), (100.3282, 36), (101.1809, 42), (101.4992, 20), (101.6708, 56), (102.2604, 130), (102.9737, 110), (104.5045, 60), (108.1287, 32), (108.2112, 36), (108.3747, 30), (109.0015, 32), (110.2473, 38), (110.2999, 40), (111.0108, 160), (111.9316, 86), (115.6754, 32), (122.6441, 26), (130.8248, 36), (140.9608, 38), (141.0456, 640), (141.3954, 20), (141.5711, 32), (142.0255, 50), (148.9896, 94), (149.0421, 120), (154.0533, 370), (154.4919, 32), (155.0567, 190), (159.6046, 32), (165.0543, 34), (167.3347, 40), (169.6343, 32), (170.0073, 40), (170.3667, 64), (170.9968, 34), (171.1002, 32), (172.0237, 36), (177.2497, 32), (183.5415, 32), (184.0801, 130), (185.9576, 30), (186.0189, 62), (186.1979, 32), (188.0384, 90), (195.0323, 140), (196.9604, 88), (197.5688, 36), (198.7957, 44), (203.0282, 130), (205.0449, 130), (214.0375, 46), (214.2236, 26), (214.3479, 42), (234.2224, 32), (241.3985, 32), (250.3197, 32), (251.4892, 58), (259.0126, 210), (259.2009, 32), (260.283, 34), (263.6798, 56), (272.9749, 100), (276.7384, 26), (279.9408, 32), (282.9518, 42), (283.2806, 68), (283.9121, 58), (283.9898, 260), (284.0554, 26), (284.1067, 34), (284.2661, 50), (284.8477, 20), (284.9996, 110), (285.0399, 50), (285.4303, 78), (285.895, 26), (287.9889, 110), (290.95, 360), (316.7149, 32), (322.7602, 32), (327.0088, 100), (334.0506, 140), (334.7452, 36), (340.9896, 100), (363.1221, 120), (375.1557, 48), (382.8756, 120), (383.0533, 88), (383.114, 78), (384.7878, 28), (384.9334, 260), (399.028, 82), (403.5427, 40), (405.6089, 32), (419.8203, 32), (430.0507, 44), (435.0891, 120), (445.5788, 32), (445.78, 48), (445.8495, 56), (445.9211, 40), (446.0458, 2100), (446.1678, 86), (446.3396, 26), (446.5167, 48), (446.6434, 56), (446.6935, 34), (446.7292, 64), (446.7796, 96), (446.8729, 28), (446.9176, 24), (446.9662, 80), (446.9973, 76), (447.0514, 670), (447.1414, 32), (447.2291, 36), (447.4207, 20), (447.4882, 46), (447.7259, 28), (447.8845, 40), (447.9316, 24), (448.0405, 330), (448.1292, 40), (448.7913, 28), (448.9453, 32), (449.0473, 130), (449.1465, 30), (449.8113, 48), (450.8451, 22), (451.7144, 42), (476.4377, 26), (476.7765, 54), (476.9268, 32), (476.9716, 32), (477.0643, 660), (477.3604, 30), (477.462, 34), (477.5809, 20), (477.8437, 44), (477.9334, 28), (478.058, 72), (478.3292, 36), (478.5442, 40), (478.7787, 28), (478.9168, 170), (479.051, 96), (479.6095, 38), (479.7597, 20), (479.8226, 32), (479.9673, 28), (480.2268, 54), (481.2645, 96), (482.1001, 22), (514.2741, 60), (535.6644, 32), (546.6051, 32), (598.8458, 32), (607.6697, 32), (685.1884, 32), (843.8948, 32), (931.5331, 32), (939.5991, 32), (1040.2625, 32), (1123.6815, 46), (1141.1581, 32), (1301.811, 32)))
    CanvasRenderingSpectreMS.featureMzXAbundanceY(f1, 300, 300)
*/
    CaptureIonFragmentSourceDivManagement.setup()

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

            setLog()
            CaptureIonFragmentSourceDivManagement.clean()

            val lFutures = Future.sequence(files.map(f => readFileAsText(f) ))
            lFutures.onComplete {
              case Success(mgfFilesContent : List[String]) =>
                val listSourceFragment : Seq[MGFFeaturesIon] = mgfFilesContent.flatMap {
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
                    listFeatures.distinct
                }
                setGeneralStatistics(listSourceFragment)
                CaptureIonFragmentSourceDivManagement(listSourceFragment).setClickSubmitEvent()
               // listSourceFragment.foreach(x => CanvasRenderingSpectreMS.featureMzXAbundanceY(x, 300, 300))
              case Success(some) =>
                System.err.println(s" * This case match is not managed : ${some.toString} .")
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
