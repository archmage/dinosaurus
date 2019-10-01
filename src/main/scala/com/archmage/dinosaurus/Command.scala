package com.archmage.dinosaurus

import discord4j.core.`object`.entity.Channel

case class Command(name: String, description: String, action: (Channel, String) => Unit)


