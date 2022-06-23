package com.kostovtd.herorealms.ui.activities

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.dynamicfeatures.fragment.DynamicNavHostFragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.kostovtd.boardy.data.models.BoardGameGameSession
import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.data.models.PlayerType
import com.kostovtd.boardy.util.Constants
import com.kostovtd.herorealms.R
import com.kostovtd.herorealms.ui.fragments.SetUpHeroRealmsPlayersFragment
import com.kostovtd.herorealms.ui.viewModels.HeroRealmsViewModel
import kotlinx.android.synthetic.main.activity_hero_realms.*


/**
 * Created by tosheto on 25.12.20.
 */
class HeroRealmsActivity : AppCompatActivity() {

    private val viewModel: HeroRealmsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_realms)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var playerType: PlayerType? = null

        intent?.let {
            if (it.hasExtra(Constants.PLAYER_TYPE_KEY)) {
                playerType = it.getSerializableExtra(Constants.PLAYER_TYPE_KEY) as PlayerType
                viewModel.setPlayerType(it.getSerializableExtra(Constants.PLAYER_TYPE_KEY) as PlayerType)
            }

            if (it.hasExtra(Constants.BOARD_GAME_GAME_SESSION_KEY)) {
                viewModel.setBoardGameGameSession(it.getSerializableExtra(Constants.BOARD_GAME_GAME_SESSION_KEY) as BoardGameGameSession)
            }

            if(it.hasExtra(Constants.GAME_SESSION_FIRESTORE_KEY)) {
                viewModel.setGameSessionFirestore(it.getSerializableExtra(Constants.GAME_SESSION_FIRESTORE_KEY) as GameSessionFirestore)
            }

            if(it.hasExtra(Constants.GAME_SESSION_DATABASE_KEY)) {
                viewModel.setGameSessionDatabase(it.getSerializableExtra(Constants.GAME_SESSION_DATABASE_KEY) as GameSessionDatabase)
            }
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController

        when(playerType) {
            PlayerType.ADMIN -> {
                navController.setGraph(R.navigation.hero_realms_nav_graph)
            }
            else -> {
                val navInflater = navController.navInflater
                val graph = navInflater.inflate(R.navigation.hero_realms_nav_graph)
                graph.setStartDestination(R.id.setUpHeroRealmsPlayersFragment)
                navController.graph = graph
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
                android.R.id.home -> {
                    val currentFragment = supportFragmentManager.primaryNavigationFragment
                    if (currentFragment is DynamicNavHostFragment) {
                        if (currentFragment.childFragmentManager.primaryNavigationFragment is SetUpHeroRealmsPlayersFragment) {
                            return false
                        } else {
                            onBackPressed()
                        }
                    } else {
                        onBackPressed()
                    }
                }
            }
            return true
        }
    }
}