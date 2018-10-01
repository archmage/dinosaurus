package com.archmage.dinosaurus.globals

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent

object ExceptionWrapper {
  def wrap(event: MessageEvent, runnable: () => Unit): Unit = {
    try {
      runnable.apply()
    }
    catch {
      case e: Exception => {
        val response = s"""\\:dino: `Oh no! Whatever you just did threw this:` ${e.getClass.toString.substring(6)}
                          |`I've sent the details to your console. Not me, of course!`""".stripMargin
        println(e.printStackTrace())
        event.getChannel.sendMessage(response)
      }
    }
  }
}
