package com.kostovtd.boardy.ui.activities

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import com.kostovtd.boardy.R
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.boardy.util.InfoType
import com.kostovtd.boardy.util.MessageHandler
import com.kostovtd.boardy.util.SuccessType
import kotlinx.android.synthetic.main.activity_base.*

/**
 * Created by tosheto on 14.11.20.
 */
abstract class BaseActivity : AppCompatActivity(), BaseView {

    private lateinit var messageHandler: MessageHandler


    protected abstract fun getLayout(): View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentLayout()

        messageHandler = MessageHandler(this, baseRootContainer)
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


    override fun showSuccess(successType: SuccessType) {
        messageHandler.showSuccessScankbar(successType)
    }

    override fun showInfo(infoType: InfoType) {
        messageHandler.showInfoScankbar(infoType)
    }

    override fun getViewContext(): Context? = this


    private fun setContentLayout() {
        val view = getLayout()
        view.id = View.generateViewId()
        baseRootContainer.addView(view, MAIN_CONTENT_LAYOUT_POSITION)
        setBaseRootContainerConstraintsToView(view)
    }


    private fun setBaseRootContainerConstraintsToView(view: View) {
        val constraintSet = ConstraintSet()
        constraintSet.connect(view.id, ConstraintSet.TOP, toolbar.id, ConstraintSet.BOTTOM)
        constraintSet.connect(
            view.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            view.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        constraintSet.connect(
            view.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
        baseRootContainer.setConstraintSet(constraintSet)
    }


    companion object {
        const val MAIN_CONTENT_LAYOUT_POSITION = 1
    }
}