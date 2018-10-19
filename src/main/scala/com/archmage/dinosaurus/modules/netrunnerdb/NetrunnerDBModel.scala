package com.archmage.dinosaurus.modules.netrunnerdb

import com.archmage.dinosaurus.globals.Constants
import scalaj.http.HttpStatusException

import scala.io.Source

object NetrunnerDBModel {
  val api: String = Constants.netrunnerDBApi
  val deckEndpoint: String = Constants.netrunnerDBPublicDeckEndpoint

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

  def lookupId(id: String): Option[NetrunnerDBCard] = cards.collectFirst { case card if card.code == id => card }

  def getDeck(id: String): Option[NetrunnerDBDeck] = {
    val url = s"$api/$deckEndpoint$id"
    try {
      val apiRequest = Constants.request(url)
      if (!apiRequest.contentType.get.contains("json")) None
      else Some(NetrunnerDBDeck.make(apiRequest.body))
    }
    catch {
      case _: HttpStatusException => None
    }
  }
}
