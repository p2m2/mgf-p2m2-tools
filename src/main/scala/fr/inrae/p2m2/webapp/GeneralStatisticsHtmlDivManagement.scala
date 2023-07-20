package fr.inrae.p2m2.webapp

import fr.inrae.p2m2.format.MGFFeaturesIon
import fr.inrae.p2m2.visitor.PropertyIon
import org.scalajs.dom
import org.scalajs.dom.html.Div

import scala.scalajs.js.Math.sqrt

case object GeneralStatisticsHtmlDivManagement {
  def setGeneralStatistics(listFeatures: Seq[MGFFeaturesIon]): Unit = {
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

    {
        val l = listFeatures.map(_.fragmentIons.length)

        val mean = (l.sum.toDouble / (l.length + 1))
        val std = sqrt(l.map(x => (x - mean) * (x - mean)).sum / (l.length + 1))

        dom
          .document
          .getElementById("meanFragIon").asInstanceOf[Div].innerHTML = mean.toString
        dom
          .document
          .getElementById("stdFragIon").asInstanceOf[Div].innerHTML = std.toString
      }

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

    {
      val l : Seq[Double] = listFeatures.flatMap(_.fragmentIons.map(_._2))

      val mean = (l.sum / (l.length + 1))
      val std = sqrt(l.map(x => (x - mean) * (x - mean)).sum / (l.length + 1))

      dom
        .document
        .getElementById("meanAbundanceFragIon").asInstanceOf[Div].innerHTML = mean.toString

      dom
        .document
        .getElementById("stdAbundanceFragIon").asInstanceOf[Div].innerHTML = std.toString
    }


  }

}
