package com.archmage.dinosaurus.modules.netrunnerdb

import com.archmage.dinosaurus.globals.Constants
import scalaj.http.HttpStatusException

import scala.io.Source

object NetrunnerDBModel {
  val api: String = Constants.netrunnerDBApi
  val deckEndpoint: String = Constants.netrunnerDBPublicDeckEndpoint
  val decklistEndpoint: String = Constants.netrunnerDBPublishedDeckEndpoint

  // hard-coding MWL for now
  val mwlRestrictedRunner: List[NetrunnerDBCard] = List(
    NetrunnerDBCard.makeManual("Aesop's Pawnshop", "shaper", "20052"),
    NetrunnerDBCard.makeManual("Employee Strike", "neutral-runner", "09053"),
    NetrunnerDBCard.makeManual("Film Critic", "shaper", "08086"),
    NetrunnerDBCard.makeManual("Gang Sign", "criminal", "08067"),
    NetrunnerDBCard.makeManual("Inversificator", "shaper", "12048"),
    NetrunnerDBCard.makeManual("Levy AR Lab Access", "shaper", "03035"),
    NetrunnerDBCard.makeManual("Mad Dash", "neutral-runner", "12008"),
    NetrunnerDBCard.makeManual("Magnum Opus", "shaper", "20050"),
    NetrunnerDBCard.makeManual("Rumor Mill", "anarch", "11022")
  )
  val mwlBannedRunner: List[NetrunnerDBCard] = List(
    NetrunnerDBCard.makeManual("Aaron Marrón", "criminal", "11106"),
    NetrunnerDBCard.makeManual("Bloo Moose", "neutral-runner", "12089"),
    NetrunnerDBCard.makeManual("Faust", "anarch", "08061"),
    NetrunnerDBCard.makeManual("Hyperdriver", "shaper", "08070"),
    NetrunnerDBCard.makeManual("Mars for Martians", "anarch", "12081"),
    NetrunnerDBCard.makeManual("Salvaged Vanadis Armory", "anarch", "12103"),
    NetrunnerDBCard.makeManual("Şifr", "anarch", "11101"),
    NetrunnerDBCard.makeManual("Tapwrm", "criminal", "11104"),
    NetrunnerDBCard.makeManual("Temüjin Contract", "criminal", "11026"),
    NetrunnerDBCard.makeManual("Zer0", "anarch", "21101")
  )
  val mwlRestrictedCorp: List[NetrunnerDBCard] = List(
    NetrunnerDBCard.makeManual("Bio-Ethics Association", "jinteki", "10050"),
    NetrunnerDBCard.makeManual("Brain Rewiring", "haas-bioroid", "13029"),
    NetrunnerDBCard.makeManual("Bryan Stinson", "weyland-consortium", "11117"),
    NetrunnerDBCard.makeManual("Global Food Initiative", "neutral-corp", "09026"),
    NetrunnerDBCard.makeManual("Hunter Seeker", "weyland-consortium", "13051"),
    NetrunnerDBCard.makeManual("Mother Goddess", "neutral-corp", "06010"),
    NetrunnerDBCard.makeManual("Mumba Temple", "neutral-corp", "10018"),
    NetrunnerDBCard.makeManual("Mumbad City Hall", "weyland-consortium", "10055"),
    NetrunnerDBCard.makeManual("Obokata Protocol", "jinteki", "12070"),
    NetrunnerDBCard.makeManual("Jinteki: Potential Unleashed", "jinteki", "11054"),
    NetrunnerDBCard.makeManual("Skorpios Defense Systems", "weyland-consortium", "13041"),
    NetrunnerDBCard.makeManual("Surveyor", "weyland-consortium", "21118"),
    NetrunnerDBCard.makeManual("Violet Level Clearance", "haas-bioroid", "11111"),
    NetrunnerDBCard.makeManual("Whampoa Reclamation", "neutral-corp", "12079")
  )
  val mwlBannedCorp: List[NetrunnerDBCard] = List(
    NetrunnerDBCard.makeManual("24/7 News Cycle", "nbn", "09019"),
    NetrunnerDBCard.makeManual("Cerebral Imaging: Infinite Frontiers", "haas-bioroid", "03001"),
    NetrunnerDBCard.makeManual("Clone Suffrage Movement", "haas-bioroid", "10049"),
    NetrunnerDBCard.makeManual("Estelle Moon", "haas-bioroid", "13032"),
    NetrunnerDBCard.makeManual("Friends in High Places", "haas-bioroid", "11090"),
    NetrunnerDBCard.makeManual("Museum of History", "neutral-corp", "10019"),
    NetrunnerDBCard.makeManual("Sensie Actors Union", "nbn", "10053"),
  )

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

  def tryAndBuildDeck(url: String): Option[NetrunnerDBDeck] = {
    try {
      val request = Constants.request(url)
      if(!request.contentType.get.contains("json")) None
      else Some(NetrunnerDBDeck.make(request.body))
    }
    catch {
      case _: HttpStatusException => None
    }
  }

  def getDeck(id: String): Option[NetrunnerDBDeck] = {
    val deckUrl = s"$api/$deckEndpoint$id"
    val decklistUrl = s"$api/$decklistEndpoint$id"

    val firstTry = tryAndBuildDeck(decklistUrl)
    if(firstTry.isDefined) firstTry
    else tryAndBuildDeck(deckUrl)
  }

  implicit class CardListFunctions(l: List[NetrunnerDBCard]) {
    def formatCardList: String = {
      l.foldLeft("") { (string, card) =>
        s"$string${card.getListEntry(showPack = false)}\n"
      }.dropRight(1)
    }
  }
}
