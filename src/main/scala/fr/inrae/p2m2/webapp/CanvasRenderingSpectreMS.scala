package fr.inrae.p2m2.webapp

import org.scalajs.dom
import org.scalajs.dom.html.Canvas
import scalatags.JsDom
import scalatags.JsDom.all._

case object CanvasRenderingSpectreMS {
  def test() : Unit = {
    type Ctx2D =
      dom.CanvasRenderingContext2D
    val c = canvas(
      id := "test",
      width := 300,
      height := 300
    ).render

    val ctx = c.getContext("2d").asInstanceOf[Ctx2D]
    val w = 300

    ctx.strokeStyle = "red"
    ctx.lineWidth = 3
    ctx.beginPath()
    ctx.moveTo(w / 3, 0)
    ctx.lineTo(w / 3, w / 3)
    ctx.moveTo(w * 2 / 3, 0)
    ctx.lineTo(w * 2 / 3, w / 3)
    ctx.moveTo(w, w / 2)
    ctx.arc(w / 2, w / 2, w / 2, 0, 3.14)

    ctx.stroke()

    dom.document.body.appendChild(c)
  }

}
