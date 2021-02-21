package com.kostovtd.boardy.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

/**
 * Created by tosheto on 21.02.21.
 */
fun generateQRCodeBitmap(text: String, bitmapWidth: Int = 500, bitmapHeight: Int = 500): Bitmap? {
    if (text.isBlank()) {
        return null
    }

    var bitmap: Bitmap? = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888)
    val codeWriter = MultiFormatWriter()
    try {
        val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, bitmapWidth, bitmapHeight)
        for (x in 0 until bitmapHeight) {
            for (y in 0 until bitmapHeight) {
                bitmap?.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
    } catch (e: WriterException) {
        //TODO add logging logic
        bitmap = null
    }
    return bitmap
}