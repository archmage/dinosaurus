package com.archmage.dinosaurus.components.cardsearch

import com.archmage.dinosaurus.globals.Constants

import scala.io.Source
import scala.util.matching.Regex

object NetrunnerDBModel {
  val cards: List[NetrunnerDBCard] = {
    val stream = Source.fromFile(Constants.cardCache)
    val string = stream.mkString
    stream.close()
    NetrunnerDBCard.makeFromList(string)
  }

  def getCardList(): Unit = {
    // api call
    // dump to local file
    // not implementing yet
  }

  def searchCard(searchText: String): List[NetrunnerDBCard] = {
    val matches = cards.filter(card => card.title.toLowerCase.contains(searchText.toLowerCase))
    if(matches.size == 2 && matches.head.title == matches(1).title) {
      matches.slice(1, 2)
    }
    else matches
  }
}
