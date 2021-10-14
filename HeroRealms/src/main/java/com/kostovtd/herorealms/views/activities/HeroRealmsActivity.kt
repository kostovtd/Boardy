package com.kostovtd.herorealms.views.activities

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.kostovtd.boardy.data.models.BoardGameGameSession
import com.kostovtd.boardy.data.models.PlayerType
import com.kostovtd.boardy.util.Constants
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.boardy.util.MessageHandler
import com.kostovtd.herorealms.R
import com.kostovtd.herorealms.presenters.HeroRealmsPresenter
import kotlinx.android.synthetic.main.activity_hero_realms.*


/**
 * Created by tosheto on 25.12.20.
 */
class HeroRealmsActivity : AppCompatActivity(), HeroRealmsView {

    private lateinit var messageHandler: MessageHandler
    private val presenter = HeroRealmsPresenter()

    override fun getViewContext(): Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_realms)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter.attachView(this)

        messageHandler = MessageHandler(this, baseRootContainer)

        intent?.let {
            if (it.hasExtra(Constants.PLAYER_TYPE_KEY)) {
                presenter.playerType =
                    it.getSerializableExtra(Constants.PLAYER_TYPE_KEY) as PlayerType?
            }

            if (it.hasExtra(Constants.BOARD_GAME_GAME_SESSION_KEY)) {
                presenter.boardGameGameSession =
                    it.getSerializableExtra(Constants.BOARD_GAME_GAME_SESSION_KEY) as BoardGameGameSession?
            }
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController

        presenter.playerType?.let { playerType ->
            presenter.boardGameGameSession?.let { boardGameGameSession ->
                val bundle = Bundle()
                bundle.putSerializable(
                    Constants.PLAYER_TYPE_KEY,
                    playerType
                )
                bundle.putSerializable(
                    Constants.BOARD_GAME_GAME_SESSION_KEY,
                    boardGameGameSession
                )

                when (playerType) {
                    PlayerType.ADMIN -> {
                        navController.setGraph(R.navigation.nav_graph, bundle)
                    }
                    PlayerType.PLAYER -> {
                        val navInflater = navController.navInflater
                        val graph = navInflater.inflate(R.navigation.nav_graph)
                        graph.setStartDestination(R.id.setUpHeroRealmsPlayersFragment)
                        navController.setGraph(graph, bundle)
                    }
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.install(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.let {
            when (it.itemId) {
                android.R.id.home -> onBackPressed()
            }
            return true
        }
    }


    override fun showLoading() {
        progressBarContainer.visibility = View.VISIBLE
    }


    override fun hideLoading() {
        progressBarContainer.visibility = View.GONE
    }


    override fun finishActivity() {
        finish()
    }


    override fun showError(errorType: ErrorType) {
        messageHandler.showErrorSnackbar(errorType)
    }
}