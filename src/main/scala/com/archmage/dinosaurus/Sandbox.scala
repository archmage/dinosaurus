package com.archmage.dinosaurus

import com.archmage.dinosaurus.components.cardsearch.{Faction, NetrunnerDBModel}
import com.archmage.dinosaurus.components.snakedraft.SnakedraftManager

object Sandbox extends App {
//  val card = NetrunnerDBModel.searchCard("blah blah").get
//  println(card)

//  println(NetrunnerDBModel.allTheCards.substring(30, 400))
  println(NetrunnerDBModel.searchCard("dog"))

//  println(Faction.values)

//  NetrunnerDBModel.cards.sortBy(card => card.title.length).reverse.slice(0, 10).foreach { card =>
//    println(s"${card.title} (${card.title.length})")
//  }
  SnakedraftManager.isCardIllegal(NetrunnerDBModel.searchCard("Keyhole").head)
}
