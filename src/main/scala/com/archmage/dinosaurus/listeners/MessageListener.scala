package com.archmage.dinosaurus.listeners

import com.archmage.dinosaurus.components.dinosaurus.ResponseLogic
import com.archmage.dinosaurus.globals.{ExceptionWrapper, Constants}
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

import scala.util.matching.Regex

class MessageListener extends IListener[MessageReceivedEvent] {

  val todayEventRegex: Regex = Constants.todayEventRegex.r

  override def handle(event: MessageReceivedEvent): Unit = {
    ExceptionWrapper.wrap(event, () => {
      event.getMessage.getContent.toLowerCase match {
        case todayEventRegex(_, _, _) => ResponseLogic.eventsToday(event.getChannel)
        case _ => ()
      }
    })
  }
}