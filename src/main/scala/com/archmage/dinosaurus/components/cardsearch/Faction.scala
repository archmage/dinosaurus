package com.archmage.dinosaurus.components.cardsearch

import java.awt.Color

object Faction {

  val values: Map[String, Color] = Map(
    "anarch" -> Color.orange,
    "criminal" -> Color.blue,
    "shaper" -> Color.green,
    "adam" -> Color.white,
    "apex" -> Color.white,
    "sunny" -> Color.white,
    "neutral-runner" -> Color.gray,

    "haas-bioroid" -> Color.magenta,
    "jinteki" -> Color.red,
    "weyland-consortium" -> Color.cyan,
    "nbn" -> Color.yellow,
    "neutral-corp" -> Color.gray
  )
}