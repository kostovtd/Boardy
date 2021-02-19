package com.kostovtd.herorealms.views.activities

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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
    private lateinit var realtimeDatabase: DatabaseReference
    private val presenter = HeroRealmsPresenter()

    override fun getContext(): Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_realms)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter.attachView(this)

        messageHandler = MessageHandler(this, baseRootContainer)
        realtimeDatabase = Firebase.database.getReference("1q2w3e/AH24sfa23fs")

        finishHeroRealms.setOnClickListener {
            finish()
        }

        increase.setOnClickListener {
//            points?.p1 = points?.p1?.plus(1)
//            realtimeDatabase.setValue(points)
        }

        decrease.setOnClickListener {
//            presenter.deleteGameSessionDatabase()
        }

        add.setOnClickListener {
            presenter.createGameSession()
        }


        val pointsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                points = snapshot.getValue<Points>()
//                myPoints.text = "my points: " + points?.p1
//                yourPoints.text = "your points: " + points?.p2
            }

            override fun onCancelled(error: DatabaseError) {
                messageHandler.showErrorSnackbar(ErrorType.EMPTY_CONFIRM_PASSWORD)
            }
        }

        realtimeDatabase.addValueEventListener(pointsListener)
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
}