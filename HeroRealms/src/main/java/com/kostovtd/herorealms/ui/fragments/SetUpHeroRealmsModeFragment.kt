package com.kostovtd.herorealms.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kostovtd.herorealms.R
import com.kostovtd.herorealms.ui.viewModels.HeroRealmsViewModel
import kotlinx.android.synthetic.main.fragment_hero_realms_mode.*

class SetUpHeroRealmsModeFragment : Fragment(R.layout.fragment_hero_realms_mode) {

    private val viewModel: HeroRealmsViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.oneVSone).setOnClickListener {
            viewModel.createGameSession()
        }

        viewModel.isGameSessionCreated.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.boardGameGameSession.value?.gameSessionId?.let { gameSessionId ->
                    viewModel.getGameSessionById(
                        gameSessionId
                    )
                }
            }
        }

        viewModel.gameSessionDatabase.observe(viewLifecycleOwner) {
            startSetUpHeroRealmsPlayersFragmentAsAdmin()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                disableAllViews()
            } else {
                enableAllViews()
            }
        }
    }


    private fun startSetUpHeroRealmsPlayersFragmentAsAdmin() {
        val action =
            SetUpHeroRealmsModeFragmentDirections.actionSetUpHeroRealmsModeFragmentToSetUpHeroRealmsPlayersFragment()
        findNavController().navigate(action)
    }


    private fun disableAllViews() {
        oneVSone.isEnabled = false
        other.isEnabled = false
    }


    private fun enableAllViews() {
        oneVSone.isEnabled = true
        other.isEnabled = true
    }
}