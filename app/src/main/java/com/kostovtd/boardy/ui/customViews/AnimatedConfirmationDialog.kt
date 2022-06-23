package com.kostovtd.boardy.ui.customViews

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import com.kostovtd.boardy.R
import kotlinx.android.synthetic.main.confirmation_dialog.*
import java.util.*

class AnimatedConfirmationDialog(
    activity: Activity,
    private val title: String,
    private val description: String,
    private val positiveCallback: () -> Unit,
    private val animationType: AnimationType,
    private val randomAnimation: Boolean = false
) : Dialog(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.confirmation_dialog)

        dialogTitle.text = title
        dialogDescription.text = description

        val animationInfo = if (randomAnimation) {
            animationType.animations.entries.elementAt(Random().nextInt(animationType.animations.size))
        } else {
            animationType.animations.entries.elementAt(0)
        }

        animationView.setAnimation(animationInfo.key)

        if (!animationInfo.value) {
            animationView.repeatCount = 1
        }

        confirm.setOnClickListener {
            positiveCallback()
            dismiss()
        }

        decline.setOnClickListener {
            dismiss()
        }
    }
}