package fr.inrae.p2m2.webapp

import fr.inrae.p2m2.format.MGFFeaturesIon
import fr.inrae.p2m2.visitor.PropertyIon
import scalatags.JsDom.all._

case object MGFFeaturesIonDivInformation {
  def information(feature : MGFFeaturesIon) = {
    val myhtml = html(
      head(
        meta(charset := "UTF-8" ),
        link(rel:="stylesheet", href:="p2m2.css")
      ),
      body(
        h1(s"Feature ${feature.id}"),
        table(
          `class` := "results",
          tr(
            td("Feature"),
            td(feature.id)
          ),
          tr(
            td("pepMass"),
            td(PropertyIon.pepMass(feature))
          ),
          tr(
            td("retentionTime"),
            td(PropertyIon.retentionTime(feature))
          ),
          tr(
            td("msLevel"),
            td(PropertyIon.msLevel(feature))
          ),
          tr(
            td("charge"),
            td(PropertyIon.charge(feature))
          ),
          tr(
            td("scans"),
            td(PropertyIon.scans(feature))
          ),
          tr(
            td(

            )
          )
        ),
        script(
          `type`:="text/javascript",
          src:="./p2m2.js"
        )
      )
    ).render

    /* ne fonctionne pas..... voir l affichage du canvas...*/

    myhtml.append(CanvasRenderingSpectreMS.featureMzXAbundanceY(feature, 500, 500))
    myhtml
  }



}
