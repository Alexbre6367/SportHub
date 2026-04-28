package com.example.sporthub.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import java.io.File

fun Uri.toBitmap(context: Context): Bitmap? {
    return try {
        val actualUri = if (this.scheme == null) {
            Uri.fromFile(File(context.filesDir, this.toString()))
        } else {
            this
        }

        val source = ImageDecoder.createSource(context.contentResolver, actualUri)
        ImageDecoder.decodeBitmap(source)
    } catch (e: Exception) {
        Log.e("MyLog", "Ошибка загрузки Bitmap", e)
        null
    }
}
