package com.archmage.dinosaurus

import com.archmage.dinosaurus.globals.Constants
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.util.DiscordException
import sx.blah.discord.api.IDiscordClient
import com.archmage.dinosaurus.listeners.{CommandListener, GuildCreateListener, MentionListener, MessageListener}

import scala.io.Source

object Main extends App {
	def createClient(token: String, login: Boolean = true): IDiscordClient = {
		val clientBuilder = new ClientBuilder
		clientBuilder.withToken(token)
		try {
			if (login) clientBuilder.login()
			else clientBuilder.build()
		}
		catch {
			case e: Exception =>
				e.printStackTrace()
				println(s"Token was ${token.length} character${if(token.length == 1) "" else "s"} long.")
				System.exit(Constants.initialisationErrorCode)
				null
		}
	}

	// token stuff
	var client: IDiscordClient = _
	if(!args.isEmpty) {
		client = createClient(args.head)
	}
	else {
		val stream = Source.fromFile(Constants.tokenFilename)
		val token = stream.getLines().collectFirst { case x => x }
		stream.close()
		if(token.isEmpty) {
			println(s"No token found in ${Constants.tokenFilename}.")
			System.exit(Constants.noTokenFoundInFileErrorCode)
		}
		else client = createClient(token.get)
	}

  val dispatcher = client.getDispatcher

	dispatcher.registerListener(new GuildCreateListener())
	dispatcher.registerListener(new MentionListener())
	dispatcher.registerListener(new CommandListener())
	dispatcher.registerListener(new MessageListener())
}