package com.archmage.dinosaurus.globals

object Strings {

  // TODO tidy up all these \\:dino: `content` things

  val greeting = "\\:dino: `Rawr! Dinosaurus here, ready to host!`"
  val defaultMentionResponse = "\\:dino: `Hello there! Have you got a program for me?`"

  val noEventsResponse = "\\:dino: `There are no events today :(`"
  val oneEventResponse = "\\:dino: `There is one event today!`"
//  val manyEventsResponse = "" // this lives inline, sadly

  val meetupApi = "https://api.meetup.com"
  val meetupEventsEndpoint = "/Melbourne-Casual-Netrunner-Meetup/events" // "/2/events"
  val useragent = "Dinosaurus"

  val commandRegex = """^\.([a-z0-9]+)[ ]?(.*)?"""

  val todayEventRegex = """.*((event.*today)|(today.*event)).*"""

  val urlRegex = "(https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*))"
}
