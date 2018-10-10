package com.archmage.dinosaurus.components.cardsearch

import java.awt.Color

object Faction {

  val values: Map[String, Color] = Map(
    "anarch" -> new Color(244, 124, 40),
    "criminal" -> new Color(79, 103, 176),
    "shaper" -> new Color(106, 181, 69),
    "adam" -> new Color(178, 166, 80),
    "apex" -> new Color(201, 49, 47),
    "sunny" -> new Color(110, 112, 111),
    "neutral-runner" -> Color.lightGray,

    "haas-bioroid" -> new Color(107, 43, 138),
    "jinteki" -> new Color(166, 69, 50),
    "weyland-consortium" -> new Color(45, 120, 104),
    "nbn" -> new Color(215, 163, 45),
    "neutral-corp" -> Color.lightGray
  )
}