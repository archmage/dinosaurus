package com.archmage.dinosaurus.globals

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent

object ExceptionWrapper {
  def wrap(event: MessageEvent, runnable: () => Unit): Unit = {
    try {
      runnable.apply()
    }
    catch {
      case e: Exception => {
        val response = String.format(Strings.exceptionResponse, e.getClass.toString.substring(6))
        println(e.printStackTrace())
        event.getChannel.sendMessage(response)
      }
    }
  }
}
