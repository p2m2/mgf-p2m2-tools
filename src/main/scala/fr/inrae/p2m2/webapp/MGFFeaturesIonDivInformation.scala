package fr.inrae.p2m2.webapp

import fr.inrae.p2m2.format.MGFFeaturesIon
import fr.inrae.p2m2.visitor.PropertyIon
import org.scalajs.dom
import org.scalajs.dom.html._
import scalatags.JsDom.all._

case object MGFFeaturesIonDivInformation {
  val css =
    """
      |body {
      |    background: #F5F5F5;
      |}
      |
      |.centered-element{
      |     margin-left: auto;
      |     margin-right: auto;
      |     padding: 35px;
      |     width: 520px;
      |}
      |.results table, .results th, .results td {
      |  border: 1px solid white;
      |  border-collapse: collapse;
      |}
      |.results th, .results td {
      |  background-color: #96D4D4;
      |}
      |""".stripMargin
  def information(feature : MGFFeaturesIon) = {
    val myhtml = html(
      head(
        meta(charset := "UTF-8" ),
      ),
      body(
        div(
          `class` := "centered-element",
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
            )
          )
        )
      )
    ).render

    val l = dom.document.createElement("style")
    l.innerHTML=css
    myhtml.getElementsByTagName("head")(0).asInstanceOf[Head].appendChild(l)


    /* ne fonctionne pas..... voir l affichage du canvas...*/
    /*
    val canvas = CanvasRenderingSpectreMS.featureMzXAbundanceY(feature, 300, 300)

    dom
      .document
      .getElementById("boardMgfTools").append(canvas)
    myhtml.getElementsByTagName("div")(0).asInstanceOf[Div].appendChild(canvas)
*/
    myhtml
  }



}
