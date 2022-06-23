package com.kostovtd.herorealms.ui.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kostovtd.boardy.data.models.GameSessionStatus
import com.kostovtd.boardy.data.models.PlayerType
import com.kostovtd.boardy.ui.customViews.AnimatedConfirmationDialog
import com.kostovtd.boardy.ui.customViews.AnimatedInformationDialog
import com.kostovtd.boardy.ui.customViews.AnimationType
import com.kostovtd.boardy.util.Constants
import com.kostovtd.herorealms.R
import com.kostovtd.herorealms.adapters.IPlayersListener
import com.kostovtd.herorealms.adapters.PlayersAdapter
import com.kostovtd.herorealms.ui.viewModels.HeroRealmsViewModel
import kotlinx.android.synthetic.main.fragment_hero_realms_players.*
import com.kostovtd.boardy.R as RApp

class SetUpHeroRealmsPlayersFragment : Fragment(R.layout.fragment_hero_realms_players),
    IPlayersListener {

    private val viewModel: HeroRealmsViewModel by activityViewModels()
    private var adapter: PlayersAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.playersList).layoutManager =
            LinearLayoutManager(activity)

        viewModel.playerType.value?.let {
            setUpViewsByPlayerType(it)

            viewModel.generateQRCode()

            if(it == PlayerType.PLAYER) {
                viewModel.addCurrentPlayerToGameSession()
            } else {
                viewModel.subscribeToGameSession()
            }
        }

        startGame.setOnClickListener {
            viewModel.startGameSession()
        }

        setObservers()
    }


    private fun setObservers() {
        observeLoading()
        observeQRCode()
        observeGameSession()
        observePlayers()
    }


    private fun observeQRCode() {
        viewModel.generatedQRCode.observe(viewLifecycleOwner) {
            qrCode.setImageBitmap(it)
        }
    }


    private fun observeLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                disableAllViews()
            } else {
                enableAllViews()
            }
        }
    }


    private fun observeGameSession() {
        viewModel.gameSessionStatus.observe(viewLifecycleOwner) {
            when (it) {
                GameSessionStatus.ACTIVE -> {
                    startHeroRealmsGameFragment()
                }
                GameSessionStatus.SUSPENDED -> {
                    onGameSessionSuspended()
                }
                else -> {}
            }
        }

        viewModel.canStartGameSession.observe(viewLifecycleOwner) {
            startGame.isEnabled = it
        }
    }


    private fun observePlayers() {
        viewModel.isCurrentPlayerAdded.observe(viewLifecycleOwner) {
            if(it) {
                viewModel.subscribeToGameSession()
            }
        }

        viewModel.isCurrentPlayerRemoved.observe(viewLifecycleOwner) {
            if (it && viewModel.isCurrentPlayerAdded.value == true) {
                onCurrentPlayerRemovedFromGameSession()
            }
        }

        viewModel.players.observe(viewLifecycleOwner) { players ->
            viewModel.currentPlayer?.let { currentPlayer ->
                viewModel.gameSessionFirestore.value?.let { gameSessionFirestore ->
                    showPlayers(players, gameSessionFirestore.adminId, currentPlayer.uid)
                }
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val titleId = RApp.string.are_you_sure

                val descriptionId = if (viewModel.playerType.value == PlayerType.ADMIN) {
                    RApp.string.you_are_about_to_leave_game_session_admin
                } else {
                    RApp.string.you_are_about_to_leave_game_session_player
                }

                val positiveCallback = {
                    if (viewModel.playerType.value == PlayerType.ADMIN) {
                        viewModel.suspendGameSession()
                    } else {
                        viewModel.removeCurrentPlayerFromGameSession()
                    }
                }

                showConfirmationDialog(
                    getString(titleId),
                    getString(descriptionId), AnimationType.ATTENTION, positiveCallback
                )
                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showPlayers(names: ArrayList<String>, adminId: String, currentPlayerId: String) {
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


    private fun disableAllViews() {
        startGame.isEnabled = false
    }


    private fun enableAllViews() {
        startGame.isEnabled = true
    }


    private fun startHeroRealmsGameFragment() {
        val action = SetUpHeroRealmsPlayersFragmentDirections
            .actionSetUpHeroRealmsPlayersFragmentToHeroRealmsGameFragment()
        findNavController().navigate(action)
    }


    override fun onRemovePlayerSelected(playerData: String) {
        showConfirmationDialog(
            getString(RApp.string.are_you_sure),
            getString(
                RApp.string.about_to_kick_out_player,
                playerData.split(Constants.FIRESTORE_VALUE_SEPARATOR)[1]
            ),
            AnimationType.REMOVE_PLAYER,
        ) { viewModel.removePlayerFromGameSession(playerData) }
    }


    private fun onCurrentPlayerRemovedFromGameSession() {
        showInformationDialog(
            getString(RApp.string.psst),
            getString(RApp.string.you_were_kicked_out_game_session),
            AnimationType.BAD_NEWS
        ) { activity?.finish() }
    }


    private fun onGameSessionSuspended() {
        when (viewModel.playerType.value) {
            PlayerType.ADMIN -> {
                showInformationDialog(
                    getString(RApp.string.yay),
                    getString(RApp.string.game_session_suspended_admin),
                    AnimationType.SUCCESS
                ) { activity?.finish() }
            }
            else -> {
                showInformationDialog(
                    getString(RApp.string.bad_news),
                    getString(RApp.string.game_session_suspended_player),
                    AnimationType.BAD_NEWS
                ) { activity?.finish() }
            }
        }
    }


    private fun showConfirmationDialog(
        title: String,
        description: String,
        animationType: AnimationType,
        positiveCallback: () -> Unit
    ) {
        activity?.let {
            AnimatedConfirmationDialog(
                it, title,
                description,
                positiveCallback,
                animationType,
                true
            ).show()
        }
    }


    private fun showInformationDialog(
        title: String,
        description: String,
        animationType: AnimationType,
        callback: () -> Unit
    ) {
        activity?.let {
            AnimatedInformationDialog(
                it, title,
                description,
                callback,
                animationType,
                true
            ).show()
        }
    }

    private fun setUpViewsByPlayerType(playerType: PlayerType) {
        when (playerType) {
            PlayerType.ADMIN -> startGame.visibility = View.VISIBLE
            PlayerType.PLAYER -> startGame.visibility = View.GONE
        }
    }
}