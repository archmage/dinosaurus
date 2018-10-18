# Dinosaurus
**Dinosaurus** is a [Discord](https://discordapp.com/) bot that serves [Netrunner](https://www.fantasyflightgames.com/en/products/android-netrunner-the-card-game/)-related functions. I made it to solve a problem case of the Melbourne Netrunner scene wanting easy access to Meetup info, and that info not being readily available via existing solutions.

Some features were inspired / implemented from [Stimbot](https://github.com/dodgepong/stimbot). Thanks to [dodgepong](https://github.com/dodgepong) for making his bot open-source and thus providing references to feature implementations.

# Features

### Meetup
 - basic access to Meetup events through the Meetup API
 - daily posts showing Meetup events on the day
### NetrunnerDB
 - card search by typing \[\[searchtext\]\]
### Other
 - Dinosaurus-style card hosting
 - MWL image link

# Project status
This project is currently being actively developed according to the needs of the Melbourne Netrunner community.

# Development
So, you want to play around with Dino's internals?

Some stuff to keep in mind:
 - As of writing (2018-10-18), **Dino isn't designed to be run by anyone but myself.** As such, some extra configuration might be needed.
 - Dino is written in [Scala](https://www.scala-lang.org/), builds with [sbt](https://www.scala-sbt.org/), and is designed to be run from a "fat" JAR file (generated with [`sbt assembly`](https://github.com/sbt/sbt-assembly)) in the background of whatever environment you're in. If these are things you're not familiar with, I'd advise reading up on them.
 - I've verified Dino runs on Windows and Linux. I see no reason why it shouldn't work on any other JVM-supported machine that has an internet connection.
 
After cloning the repo, importing it into your IDE and doing `sbt run` should work.

In order to actually serve a Discord bot, you'll need to create a new bot through [Discord's application interface](https://discordapp.com/developers/applications/) and use the token from that bot alongside your local clone of this repo, in a file called `token.txt`. 

**For what I hope are obvious reasons, the _token_ for the live instance of Dinosaurus (that serves the Melbourne Netrunner Discord) is not publicly available.**