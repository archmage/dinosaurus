package com.archmage.dinosaurus.listeners

import java.time.LocalDate

import com.archmage.dinosaurus.components.meetup.MeetupModel
import com.archmage.dinosaurus.components.meetup.dinosaurus.ResponseLogic
import com.archmage.dinosaurus.globals.{ExceptionWrapper, Strings}
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MentionEvent

import scala.util.matching.Regex

class MentionListener extends IListener[MentionEvent] {

	override def handle(event: MentionEvent) = {
		ExceptionWrapper.wrap(event, () => {
			event.getMessage.getContent.toLowerCase match {
				case _ => ResponseLogic.defaultMentionResponse(event)
			}
		})
	}
}