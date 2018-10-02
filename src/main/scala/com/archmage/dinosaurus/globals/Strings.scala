package com.archmage.dinosaurus.globals

// big ol' list of wordstuff
object Strings {

  // bot's responses
  def dinoSpeak(message: String): String = s"<:dino:493687330423570442> `$message`"

  val greeting: String = dinoSpeak("Rawr! Dinosaurus here, ready to host!")
  val defaultMentionResponse: String = dinoSpeak("Hello there! Have you got a program for me?")

  val noEventsResponse: String = dinoSpeak("There are no events today :(")
  val oneEventResponse: String = dinoSpeak("There is one event today!")
  val manyEventsResponse: String = dinoSpeak(
    """There are %1$s events today! Here's the first one:
      |
      |%2$s""".stripMargin)

  val exceptionResponse: String = dinoSpeak("""Oh no! Whatever you just did threw this:` %1$s
    |`I've sent the details to your console. Not me, of course!""".stripMargin)

  // api config stuff
  val meetupApi = "https://api.meetup.com"
  val meetupEventsEndpoint = "/Melbourne-Casual-Netrunner-Meetup/events"
  val useragent = "Dinosaurus (https://github.com/archmage/dinosaurus)"
  val timezone = "Australia/Melbourne"

  // regex for matching commands and stuff
  val commandRegex = """^\.([a-z0-9]+)[ ]?(.*)?"""
  val todayEventRegex = """.*((event.*today)|(today.*event)).*"""
  val urlRegex = "(https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*))"

}
