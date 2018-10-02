package com.archmage.dinosaurus.listeners

import com.archmage.dinosaurus.components.dinosaurus.{DinosaurusBrokeSomethingException, ResponseLogic}
import com.archmage.dinosaurus.globals.{ExceptionWrapper, Strings}
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MentionEvent

import scala.util.matching.Regex

class MentionListener extends IListener[MentionEvent] {

	val throwExceptionRegex: Regex = Strings.throwExceptionRegex.r

	override def handle(event: MentionEvent) = {
		ExceptionWrapper.wrap(event, () => {
			event.getMessage.getContent.toLowerCase match {
				case throwExceptionRegex(_, _, _) => throw new DinosaurusBrokeSomethingException
				case _ => ResponseLogic.defaultMentionResponse(event)
			}
		})
	}
}