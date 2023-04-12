package com.aqchen.filterfiesta.ui.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.math.roundToInt

fun getScaledBitmap(bitmap: Bitmap, destWidth: Int, destHeight: Int): Bitmap {
    val srcWidth = bitmap.width.toFloat()
    val srcHeight = bitmap.height.toFloat()

    Log.d("PhotoEditorViewModel", "srcHeight: $srcHeight destHeight: $destHeight srcWidth: $srcWidth destWidth: $destWidth")

    val sampleSize = if (srcHeight <= destHeight && srcWidth <= destWidth) {
        1.0f
    } else {
        val heightScale = srcHeight / destHeight
        val widthScale = srcWidth / destWidth

        minOf(heightScale, widthScale)
    }
    Log.d("PhotoEditorViewModel", "Scale: $sampleSize")
    return Bitmap.createScaledBitmap(bitmap, (srcWidth / sampleSize).roundToInt(), (srcHeight / sampleSize).roundToInt(), true)
}

fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
    if (outputStream != null) {
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun saveImageToCache(bitmap: Bitmap, fileName: String, context: Context): Uri {
    val cacheFile = File(context.cacheDir, "images")
    cacheFile.mkdirs() // don't forget to make the directory
    val stream =
        FileOutputStream("$cacheFile/$fileName")
    saveImageToStream(bitmap, stream)
    // need to get saved image (in cache) via a new file object
    val imagePath = File(context.cacheDir, "images")
    val newFile = File(imagePath, fileName)
    return FileProvider.getUriForFile(
        context,
        "com.aqchen.filterfiesta.fileprovider",
        newFile
    )
}

