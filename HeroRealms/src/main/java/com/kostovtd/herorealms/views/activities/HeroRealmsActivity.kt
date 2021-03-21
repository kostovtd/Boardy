package com.kostovtd.herorealms.views.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.zxing.integration.android.IntentIntegrator
import com.kostovtd.boardy.data.models.GameSessionDatabase
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

    override fun getContext(): Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_realms)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter.attachView(this)

        messageHandler = MessageHandler(this, baseRootContainer)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.install(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                android.R.id.home -> onBackPressed()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                showError(ErrorType.QR_SCANNING_FAILED)
            } else {
                val qrCode = result.contents
                presenter.subscribeHeroRealmsGameSession(qrCode)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun handleGameSessionChanges(gameSessionDatabase: GameSessionDatabase) {
//        if (gameSessionDatabase.active) {
//            textMyPoints.text = "My points: ${gameSessionDatabase.points["XXX"]}"
//            textHisPoints.text = "His points: ${gameSessionDatabase.points["ZZZ"]}"
//        } else {
//            presenter.unsubscribeHeroRealmsGameSession()
//            finishActivity()
//        }
    }
}