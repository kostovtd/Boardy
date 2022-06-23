package com.kostovtd.boardy.ui.activities

import android.app.Activity
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import com.kostovtd.boardy.R

/**
 * Created by tosheto on 21.02.21.
 */
class QRCodeScannerActivity : CaptureActivity() {

    companion object {
        fun newIntentForResult(activity: Activity) {
            val integrator = IntentIntegrator(activity).apply {
                captureActivity = QRCodeScannerActivity::class.java
                setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                setBeepEnabled(false)
                setPrompt(activity.getString(R.string.scanning_code))
            }
            integrator.initiateScan()
        }
    }
}