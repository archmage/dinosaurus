package com.archmage.dinosaurus

import com.archmage.dinosaurus.components.cardsearch.{Faction, NetrunnerDBModel}

object Sandbox extends App {
//  val card = NetrunnerDBModel.searchCard("blah blah").get
//  println(card)

//  println(NetrunnerDBModel.allTheCards.substring(30, 400))
  println(NetrunnerDBModel.searchCard("dog"))

//  println(Faction.values)
}
