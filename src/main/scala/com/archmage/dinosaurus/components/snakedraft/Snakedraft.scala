package com.archmage.dinosaurus.components.snakedraft

import com.archmage.dinosaurus.components.cardsearch.NetrunnerDBCard
import sx.blah.discord.handle.obj.IUser

// representation of a draft
case class Snakedraft(name: String, players: List[SnakedraftPlayer] = List()) {
  def appendPlayer(player: SnakedraftPlayer): Snakedraft = {
    Snakedraft(name, players :+ player)
  }

  def pick(user: IUser, pick: NetrunnerDBCard): Snakedraft = {
    Snakedraft(name, players.map {
      case p if p.user == user => p.appendPick(pick)
      case x => x
    })
  }
}

