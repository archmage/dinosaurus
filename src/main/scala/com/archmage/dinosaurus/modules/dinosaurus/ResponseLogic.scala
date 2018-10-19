package com.archmage.dinosaurus.modules.dinosaurus

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId}

import com.archmage.dinosaurus.modules.netrunnerdb.{NetrunnerDBCard, NetrunnerDBModel}
import com.archmage.dinosaurus.modules.meetup.MeetupModel
import com.archmage.dinosaurus.globals.Constants
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent
import sx.blah.discord.handle.obj.{ActivityType, IChannel, StatusType}
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
           |${events.head.formatEvent(hideLinkPreview)}""".stripMargin)
      case _ => channel.sendMessage(Constants.manyEventsResponse.format(
        events.size, events.head.formatEvent(hideLinkPreview)))
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
    if(matches.isEmpty) channel.sendMessage(Constants.hostingInvalidCardResponse)
    else {
      if(matches.size == 1) {
        val oldHosting = hosting
        hosting = matches.headOption
        Constants.discordClient.get.changePresence(
          StatusType.ONLINE, ActivityType.PLAYING, Constants.hostingPresenceProgram.format(hosting.get.title))
        channel.sendMessage(Constants.dinoSpeak(s"Now hosting ${hosting.get.title}!" +
          s"${if(oldHosting.isDefined) s" Goodbye, ${oldHosting.get.title}!" else ""}"))
        channel.sendMessage(hosting.get.buildEmbed(true))
      }
      else {
        channel.sendMessage(Constants.hostingTooManyResultsResponse.format(matches.size))
      }
    }
  }

  /**
    * Stop hosting.
    */
  def unhost(channel: IChannel): Unit = {
    if(hosting.isEmpty) channel.sendMessage(Constants.hostingNotCurrentlyHostingResponse)
    else {
      val oldHosting = hosting.get
      hosting = None
      Constants.discordClient.get.changePresence(
        StatusType.ONLINE, ActivityType.PLAYING, Constants.hostingPresenceEmpty)
      channel.sendMessage(Constants.hostingStopHostingResponse.format(oldHosting.title))
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
    else channel.sendMessage(Constants.hostingNotCurrentlyHostingResponse)
  }

  /**
    * Dab away the haters.
    */
  def dab(channel: IChannel): Unit = channel.sendMessage(Constants.dinoSpeak("ヽ( •_)ᕗ"))

  /**
    * Produces an image link to MWL 2.2.
    */
  def mwl(channel: IChannel): Unit = {
    channel.sendMessage(Constants.dinoSpeak("MWL v2.2 (6 September 2018):` https://i.imgur.com/qEGzkpX.png").dropRight(1))
  }

  /**
    * Spits out a random card!
    */
  def randomCard(channel: IChannel): Unit = {
    channel.sendMessage(NetrunnerDBModel.cards(Constants.random.nextInt(NetrunnerDBModel.cards.length)).buildEmbed())
  }

  /**
    * Lookup a deck using NetrunnerDBModel, given an ID, and produce a nice embed.
    */
  def deckLookup(channel: IChannel, deckId: String): Unit = {
    val deck = NetrunnerDBModel.getDeck(deckId)
    if(deck.isEmpty) channel.sendMessage(Constants.deckSearchDeckNotFound.format(deckId))
    else channel.sendMessage(deck.get.buildEmbed)
  }
}
