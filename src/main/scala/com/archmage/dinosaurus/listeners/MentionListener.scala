package com.archmage.dinosaurus.listeners

import com.archmage.dinosaurus.globals.{Constants, ExceptionWrapper}
import com.archmage.dinosaurus.modules.dinosaurus.{DinosaurusBrokeSomethingException, ResponseLogic}
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MentionEvent

import scala.util.matching.Regex

/**
	* Fires when the bot is mentioned.
	*/
class MentionListener extends IListener[MentionEvent] {

	val throwExceptionRegex: Regex = Constants.throwExceptionRegex.r

	override def handle(event: MentionEvent): Unit = {
		ExceptionWrapper.wrap(event, () => {
			event.getMessage.getContent.toLowerCase match {
				case throwExceptionRegex(_, _, _) => throw new DinosaurusBrokeSomethingException
				case _ => ResponseLogic.defaultMentionResponse(event.getChannel)
			}
		})
	}
}