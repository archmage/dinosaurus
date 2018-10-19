package com.archmage.dinosaurus.listeners

import com.archmage.dinosaurus.modules.dinosaurus.ResponseLogic
import com.archmage.dinosaurus.globals.{Constants, ExceptionWrapper}
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

import scala.util.matching.Regex

/**
  * Fires on specific regex matches of regular messages.
  */
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