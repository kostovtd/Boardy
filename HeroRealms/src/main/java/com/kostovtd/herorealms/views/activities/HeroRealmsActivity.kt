package com.kostovtd.herorealms.views.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.kostovtd.herorealms.R
import com.kostovtd.shared.ErrorType
import com.kostovtd.shared.util.ErrorMessageHandler
import kotlinx.android.synthetic.main.activity_hero_realms.*


/**
 * Created by tosheto on 25.12.20.
 */
class HeroRealmsActivity: AppCompatActivity(), HeroRealmsView {

    private lateinit var errorMessageHandler: ErrorMessageHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_realms)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        errorMessageHandler = ErrorMessageHandler(this, baseRootContainer)

        finishHeroRealms.setOnClickListener {
            Snackbar.make(baseRootContainer, "Click", Snackbar.LENGTH_LONG).show()
        }
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
        errorMessageHandler.showErrorSnackbar(errorType)
    }
}