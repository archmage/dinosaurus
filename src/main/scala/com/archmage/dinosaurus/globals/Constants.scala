package com.archmage.dinosaurus.globals

import java.time.{Duration, LocalTime}

// big ol' list of wordstuff... and some non-wordstuff
object Constants {

  // discord client setup stuff
  val tokenFilename = "token.txt"

  // emoji
  val dinoEmoji = "<:dino:493687330423570442>"
  val spEmoji = "<:sp:500535963899133962>"

  val agendaEmoji = "<:agenda:499088379581956097>"
  val clickEmoji = "<:click:499063203184115722>"
  val creditEmoji = "<:credit:499078904430985217>"
  val linkEmoji = "<:link:499078904506613770>"
  val muEmoji = "<:mu:499078904183783436>"
  val recurringEmoji = "<:recurring:499095034520666112>"
  val subroutineEmoji = "<:subroutine:499094005372551168>"
  val trashEmoji = "<:trash:499078904406081548>"

  val anarchEmoji = "<:anarch:500510408499527683>"
  val criminalEmoji = "<:criminal:500510408839528468>"
  val shaperEmoji = "<:shaper:500510408122040334>"
  val adamEmoji = "<:adam:500510408616968229>"
  val apexEmoji = "<:apex:500510408705310720>"
  val sunnyEmoji = "<:sunny:500510408533082122>"

  val hbEmoji = "<:hb:500510408805974030>"
  val jintekiEmoji = "<:jinteki:500510408986198016>"
  val nbnEmoji = "<:nbn:500510408206188545>"
  val weylandEmoji = "<:weyland:500510927712419842>"

  // codes to replace in text
  val clickCode = "[click]"
  val creditCode = "[credit]"
  val linkCode = "[link]"
  val muCode = "[mu]"
  val recurringCode = "[recurring-credit]"
  val subroutineCode = "[subroutine]"
  val trashCode = "[trash]"

  val anarchCode = "[anarch]"
  val criminalCode = "[criminal]"
  val shaperCode = "[shaper]"

  val hbCode = "[haas-bioroid]"
  val jintekiCode = "[jinteki]"
  val nbnCode = "[nbn]"
  val weylandCode = "[weyland-consortium]"

  // bot's responses
  def dinoSpeak(message: String): String = s"$dinoEmoji `$message`"

  val greeting: String = dinoSpeak("Rawr! Dinosaurus here, ready to host!")
  val defaultMentionResponse: String = dinoSpeak("Hello there! Have you got a program for me?")

  val exceptionResponse: String = dinoSpeak("""Oh no! Whatever you just did threw this:` %1$s
                                              |`I've sent the details to your console. Not me, of course!""".stripMargin)

  val noEventsResponse: String = dinoSpeak("There are no events today :(")
  val oneEventResponse: String = dinoSpeak("There is one event today!")
  val manyEventsResponse: String = dinoSpeak(
    """There are %1$s events today! Here's the first one:
      |
      |%2$s""".stripMargin)

  val autocheckResponse: String = dinoSpeak("Hey! The autocheck thing happened!")

  val cardSearchResponse: String = dinoSpeak("You searched for %1$s! I don't know how to find cards yet though :(")
  val cardSearchNoMatchResponse: String = dinoSpeak("No Netrunner card result found for \"%1$s\".")

  // meetup api config stuff
  val meetupApi = "https://api.meetup.com"
  val meetupEventsEndpoint = "/Melbourne-Casual-Netrunner-Meetup/events"

  // netrunnerdb config stuff
  val netrunnerDBApi = "https://netrunnerdb.com/api/2.0"
  val netrunnerDBAllCardsEndpoint = "/public/cards"

  val cardCache = "cards.json"

  // put emoji in descriptions for embeds
  def formatDescriptionText(text: String): String = {
    // TODO fix these weird issues - not working atm
    text // .replaceAll("\\*", "\\*") // this actually does something, never fear
      // .replaceAll("_", "\\_")
      .replaceTag( "strong", "**$1**")
      .replaceTag("errata", "⚠ _$1_")
      .replaceTag("champion", "$1")
      .replaceTag("ul", "$1")
      .replaceTag("li", "\n - $1")
      .replaceTag("trace", "**$1** -")
      .replace(clickCode, clickEmoji)
      .replace(creditCode, creditEmoji)
      .replace(linkCode, linkEmoji)
      .replace(muCode, muEmoji)
      .replace(recurringCode, recurringEmoji)
      .replace(subroutineCode, subroutineEmoji)
      .replace(trashCode, trashEmoji)
      .replace(anarchCode, anarchEmoji)
      .replace(criminalCode, criminalEmoji)
      .replace(shaperCode, shaperEmoji)
      .replace(hbCode, hbEmoji)
      .replace(jintekiCode, jintekiEmoji)
      .replace(nbnCode, nbnEmoji)
      .replace(weylandCode, weylandEmoji)
  }

  // general api stuff
  val useragent = "Dinosaurus (https://github.com/archmage/dinosaurus)"
  val timezone = "Australia/Melbourne"

  // date things for autochecking
  val autocheckTime: LocalTime = LocalTime.of(9, 0)
  val autocheckInterval: Duration = Duration.ofMinutes(2)
  val autocheckChannel = 496514701061259274L // 493948188642770945L is #dev, 496514701061259274L is #events

  // regex for matching commands and stuff
  val commandRegex = """^\.([a-z0-9]+)[ ]?(.*)?"""
  val todayEventRegex = """.*((event.*today)|(today.*event)).*"""
  val urlRegex = "(https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*))"
  val throwExceptionRegex = """.*((blow something up)|(break something)).*"""
  val cardSearchRegex = """.*?[\[\(]{2}(.+?)[\]\)]{2}.*"""
  val tagRegex = "<%1$s>(.*?)</%1$s>"
  val packParseRegex = "{\"code\":\"(.*?)\".*?\"name\":\"(.*?)\".*?},"

  // error code on crash when initialising client
  val noTokenFoundInFileErrorCode = 404
  val initialisationErrorCode = 14

  implicit class StringFunctions(s: String) {
    def replaceTag(tag: String, replacement: String): String = {
      s.replaceAll(tagRegex.format(tag), replacement)
    }
  }
}
