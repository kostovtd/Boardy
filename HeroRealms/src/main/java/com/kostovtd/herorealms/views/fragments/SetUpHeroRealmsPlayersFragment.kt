package com.kostovtd.herorealms.views.fragments

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kostovtd.boardy.data.models.BoardGameGameSession
import com.kostovtd.boardy.data.models.PlayerType
import com.kostovtd.boardy.util.Constants
import com.kostovtd.boardy.views.customViews.AnimatedConfirmationDialog
import com.kostovtd.boardy.views.customViews.AnimatedInformationDialog
import com.kostovtd.boardy.views.customViews.AnimationType
import com.kostovtd.herorealms.R
import com.kostovtd.herorealms.adapters.IPlayersListener
import com.kostovtd.herorealms.adapters.PlayersAdapter
import com.kostovtd.herorealms.presenters.SetUpHeroRealmsPlayersPresenter
import kotlinx.android.synthetic.main.fragment_hero_realms_players.*
import com.kostovtd.boardy.R as RApp

class SetUpHeroRealmsPlayersFragment : Fragment(R.layout.fragment_hero_realms_players),
    SetUpHeroRealmsPlayersView, IPlayersListener {

    private val presenter = SetUpHeroRealmsPlayersPresenter()
    private var adapter: PlayersAdapter? = null

    override fun getViewContext(): Context? = context


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attachView(this)

        view.findViewById<RecyclerView>(R.id.playersList).layoutManager = LinearLayoutManager(activity)

        arguments?.getSerializable(Constants.PLAYER_TYPE_KEY).let {
            presenter.playerType = it as PlayerType
        }

        arguments?.getSerializable(Constants.BOARD_GAME_GAME_SESSION_KEY).let {
            presenter.boardGameGameSession = it as BoardGameGameSession
        }

        presenter.playerType?.let {
            setUpViewsByPlayerType(it)
            presenter.generateQRCode()

            if(presenter.playerType == PlayerType.PLAYER) {
                presenter.getGameSessionByIdAndAddCurrentUserToGameSession()
            } else {
                presenter.subscribeGameSession()
            }
        }

        startGame.setOnClickListener {
            presenter.startHeroRealmsGameSession()
        }
    }


    override fun showQRcode(qrBitmap: Bitmap) {
        qrCode.setImageBitmap(qrBitmap)
    }


    override fun onDestroy() {
        presenter.unsubscribeGameSession()
        super.onDestroy()
    }


    override fun finishActivity() {
        activity?.finish()
    }


    override fun showPlayers(names: ArrayList<String>, adminId: String, currentPlayerId: String) {
        adapter?.let {
            it.data = names
            it.adminId = adminId
            it.currentPlayerId = currentPlayerId
            it.notifyDataSetChanged()
        } ?: run {
            adapter = context?.let { PlayersAdapter(it) }
            adapter?.data = names
            adapter?.adminId = adminId
            adapter?.currentPlayerId = currentPlayerId
            adapter?.listener = this
            playersList.adapter = adapter
        }
    }


    override fun disableAllViews() {
        startGame.isEnabled = false
    }


    override fun enableAllViews() {
        startGame.isEnabled = true
    }


    override fun enableStartGame() {
        startGame.isEnabled = true
    }


    override fun disableStartGame() {
        startGame.isEnabled = false
    }

    override fun startHeroRealmsGameFragmentAsAdmin() {
        presenter.boardGameGameSession?.let { boardGameGameSession ->
            val action = SetUpHeroRealmsPlayersFragmentDirections
                .actionSetUpHeroRealmsPlayersFragmentToHeroRealmsGameFragment(PlayerType.ADMIN, boardGameGameSession)
            findNavController().navigate(action)
        }
    }


    override fun startHeroRealmsGameFragmentAsPlayer() {
        presenter.boardGameGameSession?.let { boardGameGameSession ->
            val action = SetUpHeroRealmsPlayersFragmentDirections
                .actionSetUpHeroRealmsPlayersFragmentToHeroRealmsGameFragment(PlayerType.PLAYER, boardGameGameSession)
            findNavController().navigate(action)
        }
    }


    override fun onRemovePlayerSelected(playerData: String) {
        showConfirmationDialog(
            getString(RApp.string.are_you_sure),
            getString(RApp.string.about_to_kick_out_player, playerData.split(Constants.FIRESTORE_VALUE_SEPARATOR)[1])
        ) { presenter.removePlayerFromHeroRealmsGameSession(playerData) }
    }


    override fun onCurrentPlayerRemovedFromGameSession() {
        val callback = {
            presenter.unsubscribeGameSession()
            finishActivity()
        }
        showInformationDialog(getString(RApp.string.psst),
            getString(RApp.string.you_were_kicked_out_game_session),
            callback)
    }


    override fun showConfirmationDialog(
        title: String,
        description: String,
        positiveCallback: () -> Unit
    ) {
        activity?.let {
            AnimatedConfirmationDialog(
                it, title,
                description,
                positiveCallback,
                AnimationType.REMOVE_PLAYER,
                true
            ).show()
        }
    }


    override fun showInformationDialog(title: String, description: String, callback: () -> Unit) {
        activity?.let {
            AnimatedInformationDialog(
                it, title,
                description,
                callback,
                AnimationType.BAD_NEWS,
                true
            ).show()
        }
    }

    private fun setUpViewsByPlayerType(playerType: PlayerType) {
        when(playerType) {
            PlayerType.ADMIN -> startGame.visibility = View.VISIBLE
            PlayerType.PLAYER -> startGame.visibility = View.GONE
        }
    }
}