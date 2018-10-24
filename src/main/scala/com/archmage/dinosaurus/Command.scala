package com.archmage.dinosaurus

import sx.blah.discord.handle.obj.IChannel

case class Command(name: String, description: String, action: (IChannel, String) => Unit)


