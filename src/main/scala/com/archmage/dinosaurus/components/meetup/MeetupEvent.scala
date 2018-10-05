package com.archmage.dinosaurus.components.meetup

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
                       waitlist_count: Int)
