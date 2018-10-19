package com.archmage.dinosaurus

import com.archmage.dinosaurus.modules.netrunnerdb.{Faction, NetrunnerDBModel}
import com.archmage.dinosaurus.modules.snakedraft.SnakedraftManager

object Sandbox extends App {
//  val card = NetrunnerDBModel.searchCard("blah blah").get
//  println(card)

//  println(NetrunnerDBModel.allTheCards.substring(30, 400))
//  println(NetrunnerDBModel.searchCard("dog"))

//  println(Faction.values)

//  NetrunnerDBModel.cards.sortBy(card => card.title.length).reverse.slice(0, 10).foreach { card =>
//    println(s"${card.title} (${card.title.length})")
//  }
//  SnakedraftManager.isCardIllegal(NetrunnerDBModel.searchCard("Keyhole").head)

  println(NetrunnerDBModel.getDeck("1194623"))
  println(NetrunnerDBModel.getDeck("100"))
  println(NetrunnerDBModel.getDeck("4004623"))
}
