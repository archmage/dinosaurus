package com.archmage.dinosaurus.components.meetup

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, LocalTime, ZoneId}

import com.archmage.dinosaurus.globals.Constants
import scalaj.http.{Http, HttpResponse}

object MeetupModel {
  val api: String = Constants.meetupApi
  val group: String = Constants.meetupGroup
  val eventsEndpoint: String = Constants.meetupEventsEndpoint
  val useragent: String = Constants.useragent

  // date code is always fucky, sorry if this has bugs
  def getEventsOnDay(date: LocalDate = LocalDate.now(ZoneId.of(Constants.timezone))): List[MeetupEvent] = {
    val url = s"$api/$group/$eventsEndpoint?no_later_than=${date.format(DateTimeFormatter.ISO_DATE)}T23:59:59"
    val apiRequest = request(url)
    val events = MeetupEvent.makeFromList(apiRequest.body)
    events
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
