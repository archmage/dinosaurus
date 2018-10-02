package com.archmage.dinosaurus.components.meetup

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, LocalTime, ZoneId}

import com.archmage.dinosaurus.globals.Strings
import scalaj.http.{Http, HttpResponse}

object MeetupModel {
  val api: String = Strings.meetupApi
  val eventsEndpoint: String = Strings.meetupEventsEndpoint
  val useragent: String = Strings.useragent

  // date code is always fucky, sorry if this has bugs
  def getEventsOnDay(date: LocalDate = LocalDate.now(ZoneId.of(Strings.timezone))): List[MeetupEvent] = {
    val url = s"$api$eventsEndpoint?no_later_than=${date.format(DateTimeFormatter.ISO_DATE)}T23:59:59"
    val apiRequest = request(url)
    val events = MeetupEvent.makeFromList(apiRequest.body)
    events
  }

  def formatEvent(event: MeetupEvent, hideEmbed: Boolean = false): String = {
    val description = event.description.replaceAll("<.*?>", "").replaceAll(Strings.urlRegex, "<$1>")
    val convertedDate = LocalDate.parse(event.local_date).format(DateTimeFormatter.ofPattern("EEEE, d MMMM uuuu"))
    val convertedTime = LocalTime.parse(event.local_time).format(DateTimeFormatter.ofPattern("h:mma"))
    s"""**${event.name}**
      |$convertedDate, $convertedTime
      |Attendees: ${event.yes_rsvp_count} confirmed
      |${if(hideEmbed) s"<${event.link}>" else s"${event.link}"}
      |
      |$description""".stripMargin
  }

  // core request function that simplifies header assignment and response formatting
  private def request(string: String): HttpResponse[String] = {
    val request = Http(string)
    request.header("User-Agent", useragent)
    val response = request.asString
    println(request)
    println(s"response: ${response.code}")
    if(response.isError) {
      response.throwError
    }
    response
  }
}