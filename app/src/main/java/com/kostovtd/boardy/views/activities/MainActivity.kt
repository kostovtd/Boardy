package com.kostovtd.boardy.views.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import com.kostovtd.boardy.R
import com.kostovtd.boardy.data.models.BoardGameGameSession
import com.kostovtd.boardy.data.models.PlayerType
import com.kostovtd.boardy.presenters.MainPresenter
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.boardy.util.downloadAndInstallModule
import com.kostovtd.boardy.util.isModuleInstalled
import com.kostovtd.boardy.util.startBoardGameModuleAs
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Created by tosheto on 21.10.20.
 */
class MainActivity : BaseActivity(), MainView {

    private val mainPresenter = MainPresenter()

    override fun getLayout(): View = layoutInflater.inflate(R.layout.activity_main, null)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)

        mainPresenter.attachView(this)

        signout.setOnClickListener {
            mainPresenter.signOut()
        }

        findGame.setOnClickListener {
            mainPresenter.findBoardGames()
        }

        startGame.setOnClickListener {
            mainPresenter.startGame()
        }

        downloadGame.setOnClickListener {
            downloadAndInstallModule(this, mainPresenter.boardGameGameSession?.moduleName ?: "", mainPresenter)
        }

        join.setOnClickListener {
            QRCodeScannerActivity.newIntentForResult(this)
        }
    }


    override fun goToSignInActivity() {
        SignInActivity.newIntent(this)
    }


    override fun enableStartGame() {
        startGame.isEnabled = true
    }


    override fun enableDownloadGame() {
        downloadGame.isEnabled = true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK) {
            when (requestCode) {
                IntentIntegrator.REQUEST_CODE -> {
                    val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
                    result?.let {
                        val gameSessionQRJson = Base64.decode(it.contents, Base64.DEFAULT)
                        val boardGameGameSession = Gson().fromJson(
                            gameSessionQRJson.decodeToString(),
                            BoardGameGameSession::class.java
                        )

                        mainPresenter.boardGameGameSession = boardGameGameSession

                        if (isModuleInstalled(this, boardGameGameSession.moduleName)) {
                            startBoardGameModuleAs(PlayerType.PLAYER, this, boardGameGameSession)
                        } else {
                            downloadAndInstallModule(
                                this,
                                mainPresenter.boardGameGameSession?.moduleName ?: "",
                                mainPresenter
                            )
                        }
                    } ?: run {
                        showError(ErrorType.QR_SCANNING_FAILED)
                    }
                }
                else -> {
                    Toast.makeText(this, "Unhandled return info", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    companion object {

        fun newIntent(context: Context, clearBackStack: Boolean = false) {
            val intent = Intent(context, MainActivity::class.java)

            if (clearBackStack) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }

            context.startActivity(intent)
        }

    }
}