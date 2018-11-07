package com.archmage.dinosaurus.modules.netrunnerdb

import java.awt.Color

import com.archmage.dinosaurus.globals.Constants

// hard-coded for now, not likely to change
// endpoint: /api/2.0/public/factions
object Faction {
  val values: Map[String, Faction] = Map(
    "anarch" -> Faction("Anarch", new Color(244, 124, 40), Constants.anarchEmoji),
    "criminal" -> Faction("Criminal", new Color(79, 103, 176), Constants.criminalEmoji),
    "shaper" -> Faction("Shaper", new Color(106, 181, 69), Constants.shaperEmoji),
    "adam" -> Faction("Adam", new Color(178, 166, 80), Constants.adamEmoji),
    "apex" -> Faction("Apex", new Color(201, 49, 47), Constants.apexEmoji),
    "sunny-lebeau" -> Faction("Sunny", new Color(110, 112, 111), Constants.sunnyEmoji),
    "neutral-runner" -> Faction("Neutral", Color.lightGray),

    "haas-bioroid" -> Faction("Haas-Bioroid", new Color(107, 43, 138), Constants.hbEmoji),
    "jinteki" -> Faction("Jinteki", new Color(166, 69, 50), Constants.jintekiEmoji),
    "nbn" -> Faction("NBN", new Color(215, 163, 45), Constants.nbnEmoji),
    "weyland-consortium" -> Faction("Weyland", new Color(45, 120, 104), Constants.weylandEmoji),
    "neutral-corp" -> Faction("Neutral", Color.lightGray)
  )
}

case class Faction(name: String, color: Color, emoji: String = "")