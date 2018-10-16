package com.archmage.dinosaurus.components.snakedraft

import com.archmage.dinosaurus.components.cardsearch.NetrunnerDBCard
import sx.blah.discord.handle.obj.IUser

case class SnakedraftPlayer(user: IUser, picks: List[NetrunnerDBCard] = List()) {
  def appendPick(pick: NetrunnerDBCard): SnakedraftPlayer = {
    SnakedraftPlayer(user, picks :+ pick)
  }
}
