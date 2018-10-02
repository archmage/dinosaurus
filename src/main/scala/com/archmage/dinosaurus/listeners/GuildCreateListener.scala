package com.archmage.dinosaurus.listeners

import java.time.{Duration, LocalDate, LocalDateTime, ZoneId}

import com.archmage.dinosaurus.components.dinosaurus.ResponseLogic
import com.archmage.dinosaurus.globals.Constants
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent
import sx.blah.discord.handle.obj.IChannel

import scala.util.matching.Regex

// fires when the bot connects
class GuildCreateListener extends IListener[GuildCreateEvent] {

  val todayEventRegex: Regex = Constants.todayEventRegex.r
  var testThread: Thread = _

  override def handle(event: GuildCreateEvent): Unit = {
    // somehow delay this until we're ready
    testThread = new Thread(() => {
      // this is real heckin' sketchy, but if it works it works, right?
      Thread.sleep(5000)

      val targetChannel = event.getGuild.getChannelByID(Constants.autocheckChannel)

      // no greeting on prod
//      targetChannel.sendMessage(Constants.greeting)

      // scheduling stuff, not sure where it'll live yet
      val targetDateTime = LocalDateTime.of(LocalDate.now(ZoneId.of(Constants.timezone)), Constants.autocheckTime)
      scheduleAutocheck(targetDateTime, targetChannel)
    })
    testThread.start()
  }

  def scheduleAutocheck(date: LocalDateTime, channel: IChannel): Unit = {
    val delayUntilNextCheck = Duration.between(LocalDateTime.now(ZoneId.of(Constants.timezone)), date)
    if(delayUntilNextCheck.toMillis >= 0) {
      println(s"Sleeping until $date.")
      Thread.sleep(delayUntilNextCheck.toMillis)

      // once awake...
      ResponseLogic.autocheckResponse(channel)
    }
    scheduleAutocheck(date.plusDays(1), channel)
  }
}