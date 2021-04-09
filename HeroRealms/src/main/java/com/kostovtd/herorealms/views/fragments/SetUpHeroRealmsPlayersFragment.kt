package com.kostovtd.herorealms.views.fragments

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kostovtd.boardy.data.models.BoardGameGameSession
import com.kostovtd.boardy.data.models.PlayerType
import com.kostovtd.boardy.util.Constants
import com.kostovtd.herorealms.R
import com.kostovtd.herorealms.adapters.PlayersAdapter
import com.kostovtd.herorealms.presenters.SetUpHeroRealmsPlayersPresenter
import kotlinx.android.synthetic.main.fragment_hero_realms_players.*

class SetUpHeroRealmsPlayersFragment : Fragment(R.layout.fragment_hero_realms_players),
    SetUpHeroRealmsPlayersView {

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

            when(it) {
                PlayerType.ADMIN ->{
                    presenter.subscribeGameSessionFirestore()
                }
                PlayerType.PLAYER -> {
                    presenter.addCurrentUserToFirestorePlayersListAndSubscribe()
                }
            }
        }
    }


    override fun showQRcode(qrBitmap: Bitmap) {
        qrCode.setImageBitmap(qrBitmap)
    }


    override fun showPlayers(names: ArrayList<String>, adminId: String) {
        adapter?.let {
            it.data = names
            it.adminId = adminId
            it.notifyDataSetChanged()
        } ?: run {
            adapter = context?.let { PlayersAdapter(it) }
            adapter?.data = names
            adapter?.adminId = adminId
            playersList.adapter = adapter
        }
    }


    override fun disableAllViews() {
        startGame.isEnabled = false
    }


    override fun enableAllViews() {
        startGame.isEnabled = true
    }


    private fun setUpViewsByPlayerType(playerType: PlayerType) {
        when(playerType) {
            PlayerType.ADMIN -> startGame.visibility = View.VISIBLE
            PlayerType.PLAYER -> startGame.visibility = View.GONE
        }
    }
}