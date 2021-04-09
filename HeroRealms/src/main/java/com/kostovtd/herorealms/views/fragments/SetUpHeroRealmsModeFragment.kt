package com.kostovtd.herorealms.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kostovtd.boardy.data.models.BoardGameGameSession
import com.kostovtd.boardy.data.models.PlayerType
import com.kostovtd.boardy.util.Constants
import com.kostovtd.boardy.views.activities.BaseView
import com.kostovtd.herorealms.R
import com.kostovtd.herorealms.presenters.SetUpHeroRealmsModePresenter
import kotlinx.android.synthetic.main.fragment_hero_realms_mode.*

class SetUpHeroRealmsModeFragment : Fragment(R.layout.fragment_hero_realms_mode), SetUpHeroRealmsModeView {

    private val presenter = SetUpHeroRealmsModePresenter()

    override fun getViewContext(): Context? = context


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getSerializable(Constants.BOARD_GAME_GAME_SESSION_KEY).let { presenter.boardGameGameSession = it as BoardGameGameSession? }

        presenter.attachView(this)

        view.findViewById<Button>(R.id.oneVSone).setOnClickListener {
            presenter.createHeroRealmsGameSession()
        }
    }


    override fun startSetUpHeroRealmsPlayersFragmentAsAdmin(boardGameGameSession: BoardGameGameSession) {
        val action = SetUpHeroRealmsModeFragmentDirections.actionSetUpHeroRealmsModeFragmentToSetUpHeroRealmsPlayersFragment(PlayerType.ADMIN, boardGameGameSession)
        findNavController().navigate(action)
    }


    override fun disableAllViews() {
        oneVSone.isEnabled = false
        other.isEnabled = false
    }


    override fun enableAllViews() {
        oneVSone.isEnabled = true
        other.isEnabled = true
    }


    override fun showLoading() {
        activity?.let { it ->
            (it as BaseView).showLoading()
        }
    }


    override fun hideLoading() {
        activity?.let { it ->
            (it as BaseView).hideLoading()
        }
    }
}