package com.archmage.dinosaurus.components.cardsearch

import com.archmage.dinosaurus.globals.Constants

import scala.io.Source

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
    var matches = cards.filter(card => card.title.toLowerCase == searchText.toLowerCase)
    if(matches.isEmpty) matches = cards.filter(card => card.title.toLowerCase.contains(searchText.toLowerCase))

    // for duplicates, pick the latter (revised core)
    // I like the newer instances, they're more recent!
    if(matches.size == 2 && matches.head.title == matches(1).title) {
      matches.slice(1, 2)
    }

    else matches
  }
}
