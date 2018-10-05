package com.archmage.dinosaurus

import com.archmage.dinosaurus.globals.Constants
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.util.DiscordException
import sx.blah.discord.api.IDiscordClient
import com.archmage.dinosaurus.listeners.{CommandListener, GuildCreateListener, MentionListener, MessageListener}

import scala.io.Source

object Main extends App {
	def createClient(token: String, login: Boolean): IDiscordClient = {
		val clientBuilder = new ClientBuilder
		clientBuilder.withToken(token)
		try {
			if (login) clientBuilder.login()
			else clientBuilder.build()
		}
		catch {
			case de: DiscordException =>
				de.printStackTrace()
				null
		}
	}

	// token stuff
	var client: IDiscordClient = _
	if(!args.isEmpty) {
		client = createClient(args.head, true)
	}
	else {
		val stream = Source.fromFile(Constants.tokenFilename)
		val token = stream.mkString
		stream.close()
		client = createClient(token, true)
	}

  val dispatcher = client.getDispatcher

	dispatcher.registerListener(new GuildCreateListener())
	dispatcher.registerListener(new MentionListener())
	dispatcher.registerListener(new CommandListener())
	dispatcher.registerListener(new MessageListener())
}