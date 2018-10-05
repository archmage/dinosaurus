package com.archmage.dinosaurus.listeners

import java.awt.Color

import com.archmage.dinosaurus.components.cardsearch.{Faction, NetrunnerDBModel}
import com.archmage.dinosaurus.components.dinosaurus.ResponseLogic
import com.archmage.dinosaurus.globals.{Constants, ExceptionWrapper}
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.util.EmbedBuilder

import scala.util.matching.Regex

class MessageListener extends IListener[MessageReceivedEvent] {

  val todayEventRegex: Regex = Constants.todayEventRegex.r
  val cardSearchRegex: Regex = Constants.cardSearchRegex.r

  override def handle(event: MessageReceivedEvent): Unit = {
    ExceptionWrapper.wrap(event, () => {
      event.getMessage.getContent.toLowerCase match {
        case todayEventRegex(_, _, _) => ResponseLogic.eventsToday(event.getChannel)
        case cardSearchRegex(searchText) =>
          val matches = NetrunnerDBModel.searchCard(searchText)
          if(matches.isEmpty) {
            event.getChannel.sendMessage(Constants.cardSearchNoMatchResponse.format(searchText))
          }
          else {
            val builder = new EmbedBuilder
            if(matches.size == 1) {
              val card = matches.head
              builder.withTitle(card.title)
              builder.withUrl(card.getUrl)
              builder.withColor(Faction.values.getOrElse(card.faction_code, Color.black))
              builder.withDescription(
                s"""**${if(card.cost.isDefined) s"(${card.cost.getOrElse(0)}c)" else ""} ${card.type_code.capitalize}${
                  if(card.keywords.isDefined) s": ${card.keywords.getOrElse("")}" else ""
                }**
                   |
                   |${if(card.text.isDefined) card.text.getOrElse("") else "No card text."}""".stripMargin)
            }
            else {
              builder.withTitle(s"${matches.size} matches found:")
              builder.withColor(210, 226, 101)

              val description = matches.slice(0, 10).foldLeft("") {
                (string, card) => s"$string[${card.title} (${card.pack_code})](${card.getUrl})\n"
              }.dropRight(1)
              builder.withDescription(description)
            }

            event.getChannel.sendMessage(builder.build())
          }
        case _ => ()
      }
    })
  }
}