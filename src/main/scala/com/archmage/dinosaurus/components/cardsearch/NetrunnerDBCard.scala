package com.archmage.dinosaurus.components.cardsearch

import org.json4s.{DefaultFormats, _}
import org.json4s.jackson.JsonMethods.{parse, _}

object NetrunnerDBCard {
  implicit val formats:DefaultFormats = DefaultFormats

  def make(json: String): NetrunnerDBCard = {
    val parsedJson = parse(json)
    val data = parsedJson.children(1).children.head // bad, but it works
    val event = data.extract[NetrunnerDBCard]
    event
  }

  def makeFromList(json: String): List[NetrunnerDBCard] = {
    val posts = parse(json).children(1).extract[List[NetrunnerDBCard]]
    posts
  }
}

case class NetrunnerDBCard(title: String,
                           code: String,
                           pack_code: String,
                           faction_code: String,
                           type_code: String,
                           cost: Option[Int],
                           keywords: Option[String],
                           text: Option[String]) {

  def getUrl: String = s"https://netrunnerdb.com/en/card/$code"
}
