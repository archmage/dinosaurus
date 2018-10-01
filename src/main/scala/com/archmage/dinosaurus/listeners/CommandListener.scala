package com.archmage.dinosaurus.listeners

import java.time.LocalDate

import com.archmage.dinosaurus.components.meetup.MeetupModel
import com.archmage.dinosaurus.components.meetup.dinosaurus.ResponseLogic
import com.archmage.dinosaurus.globals.{ExceptionWrapper, Strings}
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

import scala.util.matching.Regex

class CommandListener extends IListener[MessageReceivedEvent] {
	val command: Regex = Strings.commandRegex.r

	override def handle(event: MessageReceivedEvent): Unit = {
		ExceptionWrapper.wrap(event, () => {
			event.getMessage.getContent match {
				case command(name, args) => {
					name match {
						case "today" => ResponseLogic.eventsToday(event)
						case _ => event.getChannel.sendMessage(s"Command: `$name`; Args: `$args`")
					}
				}
				case _ => ()
			}
		})
	}
}