package com.archmage.dinosaurus.listeners

import com.archmage.dinosaurus.globals.Strings
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent

import scala.util.matching.Regex

// fires when the bot connects
class GuildCreateListener extends IListener[GuildCreateEvent] {

  val todayEventRegex: Regex = Strings.todayEventRegex.r

  override def handle(event: GuildCreateEvent): Unit = {
    // somehow delay this until we're ready
//    event.getGuild.getChannelByID(493948188642770945L).sendMessage(Strings.greeting);
  }
}