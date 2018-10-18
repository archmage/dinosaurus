package com.archmage.dinosaurus.components.snakedraft

import com.archmage.dinosaurus.components.cardsearch.{NetrunnerDBCard, NetrunnerDBModel}
import com.archmage.dinosaurus.globals.Constants
import sx.blah.discord.handle.obj.{IChannel, IUser}
import sx.blah.discord.util.EmbedBuilder

// TODO serialisation/deserialisation
object SnakedraftManager {

  // eventual support for multiple concurrent drafts
  var activeDraft: Option[Snakedraft] = None

  // can't pick Sure Gamble or Hedge Fund
  val unpickableCards = Set("20056", "20132")
  // rotato - hardcoded for now
  // TODO make this programmatic
  val unpickablePacks = Set("core", "wla", "ta", "ce", "asis", "hs", "fp", "om", "st", "mt", "tc", "dt", "fal", "draft")
  // not implementing mwl yet

  def isCardIllegal(card: NetrunnerDBCard): Boolean = {
    unpickableCards.contains(card.code) ||
    unpickablePacks.contains(card.pack_code)
  }

  // TODO add corp/runner arg (bool here, "corp/runner" or "c/r" at read time)
  def picks(channel: IChannel): Unit = {
    if (activeDraft.isEmpty) channel.sendMessage(Constants.draftNoDraftExistsResponse)
    else {
      val embed = new EmbedBuilder
      embed.withTitle("Picks")
      activeDraft.get.players.foreach(player => {
        val picks = player.picks.foldLeft("") { (string, card) => {
          string + card.getListEntry(true, false) + "\n"
        }}.dropRight(1)
        embed.appendField(player.user.getDisplayName(channel.getGuild), picks, true)
      })
      channel.sendMessage(embed.build())
    }
  }

  def newDraft(channel: IChannel, name: String): Unit = {
    if(activeDraft.isDefined) {
      channel.sendMessage(Constants.draftInProgressResponse)
    }
    val romposaurus = new SnakedraftPlayer(channel.getGuild.getUserByID(496564296709308426L),
      List[NetrunnerDBCard](
//        NetrunnerDBModel.searchCard("Jemison").head,
        NetrunnerDBModel.searchCard("Magnum Opus").head,
        NetrunnerDBModel.searchCard("Emergent Creativity").head,
        NetrunnerDBModel.searchCard("Apocalypse").head
      )
    )
    val testingPlayers = List(romposaurus)
    val newDraft = new Snakedraft(if(name.trim.isEmpty) "New Snakedraft" else name, testingPlayers)
    activeDraft = Some(newDraft)
    channel.sendMessage(Constants.draftCreatedResponse.format(activeDraft.get.name))
  }

  def register(channel: IChannel, user: IUser): Unit = {
    if(activeDraft.isEmpty) channel.sendMessage(Constants.draftNoDraftExistsResponse)
    else {
      if(activeDraft.get.players.exists(player => player.user == user)) {
        channel.sendMessage(Constants.draftAlreadyRegisteredResponse.format(activeDraft.get.name))
      }
      else {
        activeDraft = Some(activeDraft.get.appendPlayer(SnakedraftPlayer(user)))
        channel.sendMessage(Constants.draftSuccessfulRegisterResponse.format(activeDraft.get.name))
      }
    }
  }

  def players(channel: IChannel): Unit = {

  }

  def pick(channel: IChannel, user: IUser, cardText: String): Unit = {
    if(activeDraft.isEmpty) channel.sendMessage(Constants.draftNoDraftExistsResponse)

    else if(!activeDraft.get.players.exists(player => player.user == user)) {
      channel.sendMessage(Constants.draftUserNotInDraftResponse)
    }

    else {
      var draft = activeDraft.get
      val searchResult = NetrunnerDBModel.searchCard(cardText)
      if(searchResult.length == 1) {
        val pick = searchResult.head
        // check pick for legality
        if(isCardIllegal(pick)) {
          channel.sendMessage(Constants.draftIllegalPickResponse.format(pick.title))
        }
        else if(draft.players.flatMap(player => player.picks).exists(card => card.code == pick.code)) {
          val pickOwner = draft.players.filter(player => player.picks.exists(card => card.code == pick.code)).head
          channel.sendMessage(Constants.draftPickAlreadyTakenResponse.format(
            pick.title, pickOwner.user.getDisplayName(channel.getGuild)))
        }
        else {
          // add pick
          draft = draft.pick(user, pick)
          activeDraft = Some(draft)

          // TODO implement this
          val snakeGoingRight = true
          val nextPlayerIndex = (draft.players.indexWhere(player => player.user == user) +
            (if(snakeGoingRight) 1 else -1)) %
            draft.players.length
          val nextPlayerUser = draft.players(nextPlayerIndex).user

          val embed = new EmbedBuilder
          embed.withDescription(Constants.draftPickConfirmedResponse.format(
            user.getDisplayName(channel.getGuild),
            pick.getListEntry(false, false),
            nextPlayerUser.mention()))
          channel.sendMessage(embed.build())
        }
      }
      else if(searchResult.isEmpty) {
        channel.sendMessage(Constants.draftPickNotFoundResponse.format(cardText))
      }
      else if(searchResult.length > 1) {
        channel.sendMessage(Constants.draftPickTooManyResponse.format(searchResult.length, cardText))
      }
    }
  }
}
