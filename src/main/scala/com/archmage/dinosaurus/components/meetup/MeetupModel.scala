package com.archmage.dinosaurus.components.meetup

import java.time.LocalDate
import scalaj.http.{Http, HttpResponse}

object MeetupModel {
  val api = "https://api.meetup.com"
  val eventsEndpoint = "/2/events"
  val parameters = "?&sign=true&photo-host=public&group_urlname=Melbourne-Casual-Netrunner-Meetup&page=1"
  val useragent = "Dinosaurus"

  val urlRegex = "(https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*))"

  def getEvent(startDate: LocalDate, endDate: LocalDate): MeetupEvent = {
    val event = MeetupEvent.makeFromList(request(s"$api$eventsEndpoint$parameters").body)
    event.head
  }

  def formatEvent(event: MeetupEvent): String = {
    val description = event.description.replaceAll("<.*?>", "").replaceAll(urlRegex, "<$1>")
    s"**${event.name}**\n$description\n\n${event.event_url}"
  }

  // core request function that simplifies header assignment and response formatting
  private def request(string: String): HttpResponse[String] = {
    val request = Http(string)
    request.header("User-Agent", useragent)
    val response = request.asString
    println(s"response: ${response.code}")
    if(response.isError) {
      response.throwError
    }
    response
  }
}
