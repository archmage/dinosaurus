package com.archmage.dinosaurus.modules.meetup

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, LocalTime}

import com.archmage.dinosaurus.globals.Constants
import org.json4s._
import org.json4s.jackson.JsonMethods._

object MeetupEvent {
  implicit val formats:DefaultFormats = DefaultFormats

  def make(json: String): MeetupEvent = {
    val event = parse(json).extract[MeetupEvent]
    event
  }

  def makeFromList(json: String): List[MeetupEvent] = {
    val posts = parse(json).extract[List[MeetupEvent]]
    posts
  }
}

case class MeetupEvent(name: String,
                       local_date: String,
                       local_time: String,
                       description: String,
                       link: String,
                       yes_rsvp_count: Int,
                       waitlist_count: Int) {

  def formatEvent(hideEmbed: Boolean = false): String = {
    val formattedDescription = description.replaceAll("<.*?>", "").replaceAll(Constants.urlRegex, "<$1>")
    val convertedDate = LocalDate.parse(local_date).format(DateTimeFormatter.ofPattern("EEEE, d MMMM uuuu"))
    val convertedTime = LocalTime.parse(local_time).format(DateTimeFormatter.ofPattern("h:mma"))
    s"""**$name**
      |$convertedDate, $convertedTime
      |Attendees: $yes_rsvp_count confirmed
      |${if(hideEmbed) s"<$link>" else s"$link"}
      |
      |$formattedDescription""".stripMargin
  }
}
