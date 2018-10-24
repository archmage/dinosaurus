package com.archmage.dinosaurus.listeners

import java.time.{Duration, LocalDate, LocalDateTime, ZoneId}

import com.archmage.dinosaurus.modules.dinosaurus.ResponseLogic
import com.archmage.dinosaurus.globals.Constants
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent
import sx.blah.discord.handle.obj.IChannel

import scala.util.matching.Regex

/**
  * Fires when the bot connects. Used to schedule a daily Meetup autocheck.
  */
class GuildCreateListener extends IListener[GuildCreateEvent] {

  val todayEventRegex: Regex = Constants.todayEventRegex.r
  var autocheckThread: Option[Thread] = None

  override def handle(event: GuildCreateEvent): Unit = {
    // only start a new thread if one doesn't exist!!
    // this fixes a fascinating bug with disconnects/reconnects spawning a second thread
    if(autocheckThread.isEmpty) {
      while(!event.getGuild.getShard.isReady) {
        Thread.sleep(1000)
      }
      autocheckThread = Some(new Thread(() => {
        val targetChannel = event.getGuild.getChannelByID(Constants.autocheckChannel)

        // scheduling stuff, not sure where it'll live yet
        val targetDateTime = LocalDateTime.of(LocalDate.now(ZoneId.of(Constants.timezone)), Constants.autocheckTime)
        scheduleAutocheck(targetDateTime, targetChannel)
      }))
      autocheckThread.get.start()
    }
  }

  /**
    * Tail recusrive function that schedules a check, waits until the target time, does its check, then calls itself.
    *
    * @param date
    * @param channel
    */
  // TODO move this to a separate class, it's abstract and doesn't belong here
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