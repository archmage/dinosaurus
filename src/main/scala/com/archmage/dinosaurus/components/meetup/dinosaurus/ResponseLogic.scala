package com.archmage.dinosaurus.components.meetup.dinosaurus

import com.archmage.dinosaurus.components.meetup.MeetupModel
import com.archmage.dinosaurus.globals.Strings
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent

import scala.util.matching.Regex

object ResponseLogic {
  val todayEventRegex: Regex = Strings.todayEventRegex.r

  def eventsToday(event: MessageEvent): Unit = {
    val events = MeetupModel.getEventsOnDay()
    events.size match {
      case 0 => event.getChannel.sendMessage(Strings.noEventsResponse)
      case 1 => event.getChannel.sendMessage(
        s"""${Strings.oneEventResponse}
           |
           |${MeetupModel.formatEvent(events.head, true)}""".stripMargin)
      // TODO somehow tidy this up
      case _ => event.getChannel.sendMessage(s"""\\:dino: `There are ${events.size} events today! Here's the first one.`
      |
      |${MeetupModel.formatEvent(events.head, true)}""".stripMargin)
    }
  }

  def defaultMentionResponse(event: MessageEvent): Unit = event.getChannel.sendMessage(Strings.defaultMentionResponse)
}
