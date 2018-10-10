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
        case cardSearchRegex(searchText) => ResponseLogic.cardSearchResponse(event.getChannel, searchText)
        case _ => ()
      }
    })
  }
}