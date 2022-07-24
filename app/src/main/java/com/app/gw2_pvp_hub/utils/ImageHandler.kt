package com.app.gw2_pvp_hub.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

class ImageHandler {

    fun getBitmap(image: String): Bitmap? {
        if (image != "1") {
            val byteArray = Base64.decode(image, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(
                byteArray,
                0,
                byteArray.size
            )
        }
        return null
    }

    fun convertToString(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            stream
        )
        val image = stream.toByteArray()

        return Base64.encodeToString(image, Base64.DEFAULT)
    }
}