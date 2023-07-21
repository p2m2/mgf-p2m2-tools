package fr.inrae.p2m2.webapp

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

/* Check p2m2.js */
package object P2M2Js {

  @JSGlobal
  @js.native
  def waitBlock(v: Boolean): String = js.native
}
