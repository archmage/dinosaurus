package com.archmage.dinosaurus.listeners

import com.archmage.dinosaurus.globals.{Constants, ExceptionWrapper}
import com.archmage.dinosaurus.modules.dinosaurus.ResponseLogic
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

import scala.util.matching.Regex

/**
	* Fires when the command regex gets a match (.cmd args).
	*/
class CommandListener extends IListener[MessageReceivedEvent] {
	val command: Regex = Constants.commandRegex.r

	override def handle(event: MessageReceivedEvent): Unit = {
		ExceptionWrapper.wrap(event, () => {
			event.getMessage.getContent match {
				case command(name, args) => ResponseLogic.processCommand(event.getChannel, name, args)
				case _ => ()
			}
		})
	}
}