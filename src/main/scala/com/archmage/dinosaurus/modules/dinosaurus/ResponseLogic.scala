package com.archmage.dinosaurus.modules.dinosaurus

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId}

import com.archmage.dinosaurus.Command
import com.archmage.dinosaurus.globals.Constants
import com.archmage.dinosaurus.modules.meetup.MeetupModel
import com.archmage.dinosaurus.modules.netrunnerdb.{NetrunnerDBCard, NetrunnerDBModel}
import sx.blah.discord.handle.obj.{ActivityType, IChannel, StatusType}
import sx.blah.discord.util.EmbedBuilder
import com.archmage.dinosaurus.modules.netrunnerdb.NetrunnerDBModel.CardListFunctions

import scala.util.matching.Regex

/**
  * The "brains" of Dinosaurus, this class contains all of its response logic.
  */
object ResponseLogic {
  val todayEventRegex: Regex = Constants.todayEventRegex.r
  var hosting: Option[NetrunnerDBCard] = None

  def defaultMentionResponse(channel: IChannel): Unit = channel.sendMessage(Constants.defaultMentionResponse)

  val commands: List[Command] = List(
    Command("card", "Search for a card via name.", cardSearch),
    Command("dab", "Dab on the haters.", (channel, _) => dab(channel)),
    Command("deck", "Display a decklist, given an ID.", deckLookup),
    Command("help", "Show this list.", (channel, _) => commandList(channel)),
    Command("host", "Host a breaker on Dinosaurus!", host),
    Command("hosting", "Show the card Dino is hosting.", (channel, _) => hosting(channel)),
    Command("mwl", "Show the current Most Wanted List.", (channel, _) => mwl(channel)),
    Command("randomcard", "Show a random card!", (channel, _) => randomCard(channel)),
    Command("today", "Show today's events!", (channel, _) => eventsToday(channel)),
    Command("unhost", "Stop hosting the current card.", (channel, _) => unhost(channel))
  )

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
  def cardSearch(channel: IChannel, searchText: String): Unit = {
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
    val embed = new EmbedBuilder

    // hard-coding to save characters!
    val restrictedCorpString: String =
      """<:jinteki:500510408986198016> [Bio-Ethics Association](https://bit.ly/2SeLiFR)
         |<:hb:500510408805974030> [Brain Rewiring](https://bit.ly/2CAUyhT)
         |<:weyland:500510927712419842> [Bryan Stinson](https://bit.ly/2SdBls7)
         |<:sp:500535963899133962> [Global Food Initiative](https://bit.ly/2ApkHPn)
         |<:weyland:500510927712419842> [Hunter Seeker](https://bit.ly/2PgLS7g)
         |<:sp:500535963899133962> [Mother Goddess](https://bit.ly/2PpUlVX)
         |<:sp:500535963899133962> [Mumba Temple](https://bit.ly/2yYAvGT)
         |<:weyland:500510927712419842> [Mumbad City Hall](https://bit.ly/2yttoa0)
         |<:jinteki:500510408986198016> [Obokata Protocol](https://bit.ly/2yttnCY)
         |<:jinteki:500510408986198016> [Jinteki: Potential Unleashed](https://bit.ly/2Apl7Fr)
         |<:weyland:500510927712419842> [Skorpios Defense Systems](https://bit.ly/2ETvF47)
         |<:weyland:500510927712419842> [Surveyor](https://bit.ly/2RbgTqj)
         |<:hb:500510408805974030> [Violet Level Clearance](https://bit.ly/2yXE3t6)
         |<:sp:500535963899133962> [Whampoa Reclamation](https://bit.ly/2q6AT1R)""".stripMargin

    embed.withTitle("NAPD Most Wanted List v2.2 (6 September 2018)")
    embed.appendField("Runner Restricted", NetrunnerDBModel.mwlRestrictedRunner.formatCardList, true)
    embed.appendField("Corp Restricted", restrictedCorpString, true)
    embed.appendField("Runner Banned", NetrunnerDBModel.mwlBannedRunner.formatCardList, true)
    embed.appendField("Corp Banned", NetrunnerDBModel.mwlBannedCorp.formatCardList, true)
    channel.sendMessage(embed.build())
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

  def processCommand(channel: IChannel, name: String, args: String): Unit = {
    val matchingCommand = commands.collectFirst { case command if command.name.toLowerCase() == name => command }
    if(matchingCommand.isDefined) {
      matchingCommand.get.action.apply(channel, args)
    }
    else {
      channel.sendMessage(Constants.commandNotFoundResponse.format(name))
    }
  }

  def commandList(channel: IChannel): Unit = {
    val embed = new EmbedBuilder
    embed.withTitle("Command List")
    val description = commands.foldLeft("") { (string, command) =>
      s"$string**.${command.name}** - ${command.description}\n"
    }.dropRight(1)
    embed.withDescription(description)
    embed.withFooterText("You can show this command list by typing \".help\"!")
    channel.sendMessage(embed.build())
  }
}
