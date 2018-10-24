package com.archmage.dinosaurus

import com.archmage.dinosaurus.modules.netrunnerdb.NetrunnerDBModel
import com.archmage.dinosaurus.modules.netrunnerdb.NetrunnerDBModel.CardListFunctions

object Sandbox extends App {

  println(NetrunnerDBModel.mwlRestrictedRunner.formatCardList.length)
  println(NetrunnerDBModel.mwlBannedRunner.formatCardList.length)
  println(NetrunnerDBModel.mwlRestrictedCorp.formatCardList.length)
  println(NetrunnerDBModel.mwlBannedCorp.formatCardList.length)

  println(NetrunnerDBModel.mwlRestrictedCorp.formatCardList)
}
