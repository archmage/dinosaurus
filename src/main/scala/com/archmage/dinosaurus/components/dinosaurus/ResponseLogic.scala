package com.archmage.dinosaurus.components.dinosaurus

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId}

import com.archmage.dinosaurus.components.cardsearch.{NetrunnerDBCard, NetrunnerDBModel}
import com.archmage.dinosaurus.components.meetup.MeetupModel
import com.archmage.dinosaurus.globals.Constants
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.util.EmbedBuilder

import scala.util.matching.Regex

/**
  * The "brains" of Dinosaurus, this class contains all of its response logic.
  */
object ResponseLogic {
  val todayEventRegex: Regex = Constants.todayEventRegex.r
  var hosting: Option[NetrunnerDBCard] = None

  def defaultMentionResponse(event: MessageEvent): Unit = event.getChannel.sendMessage(Constants.defaultMentionResponse)

  /**
    * Checks Meetup for events on the day, and responds accordingly.
    */
  def eventsToday(channel: IChannel, hideLinkPreview: Boolean = true): Unit = {
    val events = MeetupModel.getEventsOnDay()
    events.size match {
      case 0 => channel.sendMessage(Constants.noEventsResponse)
      case 1 => channel.sendMessage(
        s"""${Constants.oneEventResponse}
           |
           |${MeetupModel.formatEvent(events.head, hideLinkPreview)}""".stripMargin)
      case _ => channel.sendMessage(Constants.manyEventsResponse.format(
        events.size, MeetupModel.formatEvent(events.head, hideLinkPreview)))
    }
  }

  /**
    * Formatted response for Meetup checks.
    */
  def autocheckResponse(channel: IChannel): Unit = {
    channel.sendMessage(s"**${LocalDateTime.now(ZoneId.of(Constants.timezone)).format(
      DateTimeFormatter.ofPattern("EEEE, d MMMM uuuu"))}**")
    eventsToday(channel, hideLinkPreview = false)
  }

  /**
    * Searches the NetrunnerDB model for cards and responds with a nicely formatted embed.
    */
  def cardSearchResponse(channel: IChannel, searchText: String): Unit = {
    val matches = NetrunnerDBModel.searchCard(searchText)
    if(matches.isEmpty) {
      channel.sendMessage(Constants.cardSearchNoMatchResponse.format(searchText))
    }
    else {
      if(matches.size == 1) {
        channel.sendMessage(matches.head.buildEmbed())
      }
      else {
        val builder = new EmbedBuilder
        builder.withTitle(s"${matches.size} matches found:")
        builder.withColor(210, 226, 101)

        val description = matches.slice(0, 10).foldLeft("") { (string, card) => s"$string${card.getListEntry()}\n"
          }.dropRight(1)
        builder.withDescription(description)
        channel.sendMessage(builder.build())
      }
    }
  }

  /**
    * Host a non-AI icebreaker on Dinosaurus!
    */
  def host(channel: IChannel, hostString: String): Unit = {
    val matches = NetrunnerDBModel.searchCard(hostString).filter(card => {
      card.keywords.isDefined && card.keywords.get.contains("Icebreaker") && !card.keywords.get.contains("AI")
    })
    if(matches.isEmpty) channel.sendMessage(Constants.dinoSpeak("I can't host that, that's not a non-AI icebreaker!"))
    else {
      if(matches.size == 1) {
        val oldHosting = hosting
        hosting = matches.headOption
        channel.sendMessage(Constants.dinoSpeak(s"Now hosting ${hosting.get.title}!" +
          s"${if(oldHosting.isDefined) s" Goodbye, ${oldHosting.get.title}!" else ""}"))
        channel.sendMessage(hosting.get.buildEmbed(true))
      }
      else {
        channel.sendMessage(Constants.dinoSpeak(s"I found ${matches.size} non-AI icebreakers matching that text! Please be more specific :("))
      }
    }
  }

  /**
    * See what Dinosaurus is currently hosting.
    */
  def hosting(channel: IChannel): Unit = {
    if(hosting.isDefined) {
      val muFace = hosting.get.memory_cost.get match {
        case 1 => "ヽ(ˇ∀ˇ )ゞ"
        case 2 => "ᕙ(⇀‸↼‶)ᕗ"
        case _ => "(╯°□°）╯"
      }
      channel.sendMessage(Constants.dinoSpeak(s"I am hosting ${hosting.get.title}! I'm saving you ${hosting.get.memory_cost.get} MU $muFace"))
      channel.sendMessage(hosting.get.buildEmbed(true))
    }
    else channel.sendMessage(Constants.dinoSpeak("""I'm not hosting anything right now :( You can give me a program to host with ".host cardname"!"""))
  }

  /**
    * ヽ( •_)ᕗ
    */
  def dab(channel: IChannel): Unit = channel.sendMessage(Constants.dinoSpeak("ヽ( •_)ᕗ"))

  def mwl(channel: IChannel): Unit = {
    channel.sendMessage(Constants.dinoSpeak("MWL v2.2 (6 September 2018):` https://i.imgur.com/qEGzkpX.png").dropRight(1))
  }
}
