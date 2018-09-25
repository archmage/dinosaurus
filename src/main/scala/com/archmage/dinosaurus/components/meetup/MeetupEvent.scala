package com.archmage.dinosaurus.components.meetup

import org.json4s._
import org.json4s.jackson.JsonMethods._

object MeetupEvent {
  implicit val formats:DefaultFormats = DefaultFormats

  def make(json: String): MeetupEvent = {
    println(json)
    val event = parse(json).extract[MeetupEvent]
    event
  }

  def makeFromList(json: String): List[MeetupEvent] = {
    val posts = parse(json).children.head.extract[List[MeetupEvent]]
    posts
  }
}

case class MeetupEvent(name: String,
                       description: String,
                       event_url: String,
                       time: Int,
                       yes_rsvp_count: Int)
