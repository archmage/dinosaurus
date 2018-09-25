package com.archmage.dinosaurus.listeners

import java.time.LocalDate

import com.archmage.dinosaurus.components.meetup.MeetupModel
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

import scala.util.matching.Regex

class CommandListener extends IListener[MessageReceivedEvent] {
	val command: Regex = """^\.([a-z0-9]*)[ ]?(.*)?""".r

	override def handle(event: MessageReceivedEvent): Unit = {
		event.getMessage.getContent match {
			case command(name, args) => {
				if(name == "event") {
					// meetup things
					val meetupEvent = MeetupModel.getEvent(LocalDate.now(), LocalDate.now())
					event.getChannel.sendMessage(MeetupModel.formatEvent(meetupEvent))
				}
				else {
					event.getChannel.sendMessage(s"Command: `$name`; Args: `$args`")
				}
			}
			case _ => ()
		}
	}
}