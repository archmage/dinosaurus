package com.archmage.dinosaurus.components.cardsearch

import java.awt.Color

import com.archmage.dinosaurus.globals.Constants
import org.json4s.{DefaultFormats, _}
import org.json4s.jackson.JsonMethods.{parse, _}
import sx.blah.discord.api.internal.json.objects.EmbedObject
import sx.blah.discord.util.EmbedBuilder

object NetrunnerDBCard {
  implicit val formats:DefaultFormats = DefaultFormats

  def make(json: String): NetrunnerDBCard = {
    val parsedJson = parse(json)
    val data = parsedJson.children(1).children.head // bad, but it works
    val event = data.extract[NetrunnerDBCard]
    event
  }

  def makeFromList(json: String): List[NetrunnerDBCard] = {
    val posts = parse(json).children(1).extract[List[NetrunnerDBCard]]
    posts
  }
}

case class NetrunnerDBCard(
                          advancement_cost: Option[Int],
                          agenda_points: Option[Int],
                          base_link: Option[Int],
                          code: String,
                          cost: Option[Int],
                          deck_limit: Int,
                          faction_code: String,
                          faction_cost: Option[Int],
                          flavor: Option[String],
                          illustrator: Option[String],
                          image_url: Option[String],
                          influence_limit: Option[Int],
                          keywords: Option[String],
                          memory_cost: Option[Int],
                          minimum_deck_size: Option[Int],
                          pack_code: String,
                          position: Int,
                          quantity: Int,
                          side_code: String,
                          strength: Option[Int],
                          text: Option[String],
                          title: String,
                          trash_cost: Option[Int],
                          type_code: String,
                          uniqueness: Boolean) {

  def getUrl: String = s"https://netrunnerdb.com/en/card/$code"

  def buildEmbed(dinoBuff: Boolean = false): EmbedObject = {
    val builder = new EmbedBuilder

    val description = s"${if(!faction_code.contains("neutral")) s"${Faction.values(faction_code).emoji} " else ""}" +
      s"[${if(uniqueness) "◆ " else ""}**$title**]($getUrl)\n" +
      s"${if(cost.isDefined) s"${cost.get}[credit] " else ""}" +
      s"${if(memory_cost.isDefined) s"${memory_cost.get}[mu] " else ""}" +
      s"${if(agenda_points.isDefined) s"${advancement_cost.get}/${agenda_points.get} ${Constants.agendaEmoji} " else ""}" +
      s"${if(base_link.isDefined) s"${Constants.linkEmoji}${base_link.get} " else ""}" +
      s"${if(minimum_deck_size.isDefined) s"${minimum_deck_size.get}/${influence_limit.get} " else ""}" +
      s"${type_code.capitalize}${if(keywords.isDefined) s": ${keywords.get}" else ""}\n\n" +
      s"${text.getOrElse("No card text.")}" +
      s"${if(flavor.isDefined) s"\n\n_${flavor.get}_" else ""}" +
      s"${if(strength.isDefined || trash_cost.isDefined) "\n" else ""}" +
      s"${if(strength.isDefined) s"\n**Strength ${
        if(dinoBuff) s"${strength.get + 2} (base ${strength.get}, +2 from Dinosaurus)" else strength.get}**" else ""}" +
      s"${if(trash_cost.isDefined) s"\n${trash_cost.get}[trash]" else ""}"

    builder.withDescription(Constants.formatDescriptionText(description))

    builder.withFooterText(s"${Pack.values.getOrElse(pack_code, "Unknown Pack")} #$position / " +
      s"${Faction.values(faction_code).name} ${"●" * faction_cost.getOrElse(0)}")

    builder.withColor(Faction.values(faction_code).color)
    builder.build()
  }

  def getListEntry(iconIfNone: Boolean = true, showPack: Boolean = true): String = {
    s"${if(!faction_code.contains("neutral")) s"${Faction.values(faction_code).emoji} "
      else if(iconIfNone) s"${Constants.spEmoji} " else ""}" +
      s"[$title${if(showPack) s" (${Pack.values.getOrElse(pack_code, "Unknown Pack")})" else ""}]($getUrl)"
  }
}
