package com.archmage.dinosaurus.listeners

import java.time.LocalDate

import com.archmage.dinosaurus.components.meetup.MeetupModel
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MentionEvent

import scala.util.matching.Regex

class MentionListener extends IListener[MentionEvent] {

	val dateMention: Regex = """.*event.*""".r

	override def handle(event: MentionEvent) = {
		event.getMessage.getContent match {
			case dateMention() => {
				val meetupEvent = MeetupModel.getEvent(LocalDate.now(), LocalDate.now())
				event.getChannel.sendMessage(MeetupModel.formatEvent(meetupEvent))
			}
			case _ => {
				val response = "`Dinosaurus here, ready to host! (づ｡◕‿‿◕｡)づ`"
				event.getChannel.sendMessage(response)
			}
		}
	}
}