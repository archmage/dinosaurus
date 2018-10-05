package com.archmage.dinosaurus.components.dinosaurus

import java.time.{LocalDateTime, ZoneId}
import java.time.format.DateTimeFormatter

import com.archmage.dinosaurus.components.meetup.MeetupModel
import com.archmage.dinosaurus.globals.Constants
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent
import sx.blah.discord.handle.obj.IChannel

import scala.util.matching.Regex

object ResponseLogic {
  val todayEventRegex: Regex = Constants.todayEventRegex.r

  def eventsToday(channel: IChannel, hideEmbed: Boolean = true): Unit = {
    val events = MeetupModel.getEventsOnDay()
    events.size match {
      case 0 => channel.sendMessage(Constants.noEventsResponse)
      case 1 => channel.sendMessage(
        s"""${Constants.oneEventResponse}
           |
           |${MeetupModel.formatEvent(events.head, hideEmbed)}""".stripMargin)
      case _ => channel.sendMessage(Constants.manyEventsResponse.format(
        events.size, MeetupModel.formatEvent(events.head, hideEmbed)))
    }
  }

  def defaultMentionResponse(event: MessageEvent): Unit = event.getChannel.sendMessage(Constants.defaultMentionResponse)

  def autocheckResponse(channel: IChannel): Unit = {
    channel.sendMessage(s"**${LocalDateTime.now(ZoneId.of(Constants.timezone)).format(
      DateTimeFormatter.ofPattern("EEEE, d MMMM uuuu"))}**")
    eventsToday(channel, false)
  }
}
