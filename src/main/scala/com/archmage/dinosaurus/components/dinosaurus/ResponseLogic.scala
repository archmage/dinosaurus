package com.archmage.dinosaurus.components.dinosaurus

import java.time.{LocalDateTime, ZoneId}
import java.time.format.DateTimeFormatter

import com.archmage.dinosaurus.components.cardsearch.NetrunnerDBModel
import com.archmage.dinosaurus.components.meetup.MeetupModel
import com.archmage.dinosaurus.globals.Constants
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.util.EmbedBuilder

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

  def cardSearchResponse(channel: IChannel, searchText: String): Unit = {
    val matches = NetrunnerDBModel.searchCard(searchText)
    if(matches.isEmpty) {
      channel.sendMessage(Constants.cardSearchNoMatchResponse.format(searchText))
    }
    else {
      if(matches.size == 1) {
        channel.sendMessage(matches.head.buildEmbed)
      }
      else {
        val builder = new EmbedBuilder
        builder.withTitle(s"${matches.size} matches found:")
        builder.withColor(210, 226, 101)

        val description = matches.slice(0, 10).foldLeft("") {
          (string, card) => s"$string[${card.title} (${card.pack_code})](${card.getUrl})\n"
        }.dropRight(1)
        builder.withDescription(description)
        channel.sendMessage(builder.build())
      }
    }
  }
}
