package fr.inrae.p2m2.webapp

import fr.inrae.p2m2.format.MGFFeaturesIon
import fr.inrae.p2m2.visitor.PropertyIon
import org.scalajs.dom
import org.scalajs.dom.{Event, MouseEvent}
import org.scalajs.dom.html.Canvas
import scalatags.JsDom
import scalatags.JsDom.all._

case object CanvasRenderingSpectreMS {
  type Ctx2D =
    dom.CanvasRenderingContext2D

  val border : Int = 10

  def initCanvas(idC: String) : Canvas = {
    canvas(
      id := idC
    ).render
  }

  def ctxSetAxes(ctx : Ctx2D) : Ctx2D = {
    // Axes
    ctx.strokeStyle = "black"
    ctx.lineWidth = 2
    ctx.font = "11px Arial"

    val w = ctx.canvas.width - border
    val h = ctx.canvas.height - border

    // Axe X : M/Z (maxMZ -> w)
    ctx.beginPath()
    ctx.moveTo(0, h)
    ctx.lineTo(w, h)
    ctx.stroke()

    val shiftTestX = w*3/100
    val shiftTestY = w*3/100

    ctx.fillText("M/Z", w-shiftTestX, h-shiftTestY)

    // Axe Y : Abundance (maxAb -> h)
    ctx.beginPath()
    ctx.moveTo(0, h)
    ctx.lineTo(0, 0)
    ctx.stroke()

    ctx.fillText("Intensity", 2, border)

    ctx

  }

  def pic(ctx : Ctx2D,x: Double,y: Double): Unit = {
    val h = ctx.canvas.height - border

    ctx.beginPath()
    ctx.moveTo(x, h - y)
    ctx.lineTo(x, h)

    ctx.stroke()
  }

  def featureMzXAbundanceY(feature : MGFFeaturesIon, w: Int = 300, h:Int = 300) : Canvas = {

    val canvasPlotFeatureFragmentIons : Canvas = initCanvas(feature.id + "Canvas")

    canvasPlotFeatureFragmentIons.width = w
    canvasPlotFeatureFragmentIons.height = h

    val maxMz: Double = feature.fragmentIons.map(_._1).max
    val maxIntensity: Double = feature.fragmentIons.map(_._2).max

    val ctx = canvasPlotFeatureFragmentIons.getContext("2d").asInstanceOf[Ctx2D]

    canvasPlotFeatureFragmentIons.addEventListener("mousemove", (e: MouseEvent) => {
      //println(s"""clientX ${e.clientX} clientY ${e.clientY} pageX   ${e.pageX} pageY   ${e.pageY} screenX ${e.screenX} screenY ${e.screenY} """)
      val r = feature.fragmentIons.filter {
        case (mz : Double,intensity : Double) =>
          val x: Double = (mz * (w - border)) / maxMz
          val y: Double = (intensity * (h - border)) / maxIntensity

          ((x - e.clientX).abs <3)&& (y>e.clientY)
      }
      println(r)
    })

    ctxSetAxes(ctx)

    // Ions

    ctx.strokeStyle = "red"
    ctx.lineWidth = 2

    feature.fragmentIons.foreach {
      case (mz : Double,abundance : Double) => {

        val x: Double = (mz * (w- border)) / maxMz
        val y: Double = (abundance * (h- border)) / maxIntensity

        println(x, y)
        pic(ctx, x, y)
      }
    }
    canvasPlotFeatureFragmentIons
  }

}
