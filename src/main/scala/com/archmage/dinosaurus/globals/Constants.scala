package com.archmage.dinosaurus.globals

import java.time.{Duration, LocalTime}

// big ol' list of wordstuff... and some non-wordstuff
object Constants {

  // discord client setup stuff
  val tokenFilename = "dev_token.txt"

  // bot's responses
  def dinoSpeak(message: String): String = s"<:dino:493687330423570442> `$message`"

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

  // general api stuff
  val useragent = "Dinosaurus (https://github.com/archmage/dinosaurus)"
  val timezone = "Australia/Melbourne"

  // date things for autochecking
  val autocheckTime: LocalTime = LocalTime.of(9, 0) // 9:00
  val autocheckInterval: Duration = Duration.ofMinutes(2)
  val autocheckChannel = 496514701061259274L // 493948188642770945L is #dev, 496514701061259274L is #events

  // regex for matching commands and stuff
  val commandRegex = """^\.([a-z0-9]+)[ ]?(.*)?"""
  val todayEventRegex = """.*((event.*today)|(today.*event)).*"""
  val urlRegex = "(https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*))"
  val throwExceptionRegex = """.*((blow something up)|(break something)).*"""
  val cardSearchRegex = """\[\[(.+?)\]\]"""
}
