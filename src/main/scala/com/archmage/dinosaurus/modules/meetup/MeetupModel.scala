package com.archmage.dinosaurus.modules.meetup

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
    val apiRequest = Constants.request(url)
    val events = MeetupEvent.makeFromList(apiRequest.body)
    events
  }
}
