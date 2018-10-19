package com.archmage.dinosaurus.modules.snakedraft

import com.archmage.dinosaurus.modules.netrunnerdb.NetrunnerDBCard
import sx.blah.discord.handle.obj.IUser

case class SnakedraftPlayer(user: IUser, picks: List[NetrunnerDBCard] = List()) {
  def appendPick(pick: NetrunnerDBCard): SnakedraftPlayer = {
    SnakedraftPlayer(user, picks :+ pick)
  }
}
