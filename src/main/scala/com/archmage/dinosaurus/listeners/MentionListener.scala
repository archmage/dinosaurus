package com.archmage.dinosaurus.listeners

import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MentionEvent

class MentionListener extends IListener[MentionEvent] {
	override def handle(event: MentionEvent) = {
		val response = "`Dinosaurus here, ready to host! (づ｡◕‿‿◕｡)づ`"
		event.getChannel.sendMessage(response)
	}
}