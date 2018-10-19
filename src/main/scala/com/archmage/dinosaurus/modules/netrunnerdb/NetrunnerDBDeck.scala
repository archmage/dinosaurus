package com.archmage.dinosaurus.modules.netrunnerdb

import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse
import sx.blah.discord.api.internal.json.objects.EmbedObject
import sx.blah.discord.util.EmbedBuilder

object NetrunnerDBDeck {
  implicit val formats:DefaultFormats = DefaultFormats

  def make(json: String): NetrunnerDBDeck = {
    val parsedJson = parse(json)
    val data = parsedJson.children.head.children.head
    data.extract[NetrunnerDBDeck]
  }
}

case class NetrunnerDBDeck(
                          id: String,
                          name: String,
                          description: String,
                          cards: Map[String, Int]) {

  lazy val cardsMapped: Map[NetrunnerDBCard, Int] = cards.map(card => (NetrunnerDBModel.lookupId(card._1).get, card._2))
  lazy val identity: Option[NetrunnerDBCard] = cardsMapped.collectFirst {
    case card if card._1.type_code == "identity" => card._1 }

  def buildEmbed: EmbedObject = {
    val builder = new EmbedBuilder
    builder.withTitle(name)

    val identityLine = identity.get.getListEntry(showPack = false)

    val corpTypeOrder = List("agenda", "asset", "upgrade", "operation", "ice")
    val runnerTypeOrder = List("event", "hardware", "resource", "program")
    // TODO ice subtype sorting
    // TODO icebreaker / program sorting

    val cardListWithoutIdentity = cardsMapped.keySet.toList.filter(card => card.type_code != "identity")
    val cardListSortedByTitle = cardListWithoutIdentity.sortBy(card => card.title)
    val cardListSortedByType = cardListSortedByTitle.sortBy(card => {
      if(card.side_code == "corp") corpTypeOrder.indexOf(card.type_code)
      else runnerTypeOrder.indexOf(card.type_code)
    })
    /*
    val cardListSortedBySubtype = cardListSortedByType.sortBy(card => {
      if(card.side_code == "corp") {
        if(card.type_code != "ice") 0
        // other nonsense, do this later
      }
    })
    */
    val finalCardList = cardListSortedByType.foldLeft("") { (string, card) =>
      s"$string${card.factionSymbol()} ${cardsMapped(card)}x " +
        s"${card.getListEntry(showFactionSymbol = false, showPack = false)} " +
        s"${if(identity.isDefined && card.faction_code != identity.get.faction_code)
          "●" * card.faction_cost.getOrElse(0) else ""}\n"
    }.dropRight(1)

    val description = s"$identityLine\n\n$finalCardList"

    builder.withDescription(description)
    builder.build()
  }
}
