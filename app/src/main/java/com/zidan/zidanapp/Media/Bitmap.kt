package com.zidan.zidanapp.Media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import java.io.ByteArrayOutputStream

fun getScaledBitmap(imageUri: Uri, context: Context, maxWidth: Int, maxHeight: Int): Bitmap {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    val inputStream = context.contentResolver.openInputStream(imageUri)
    BitmapFactory.decodeStream(inputStream, null, options)
    inputStream?.close()

    // Kalkulasi inSampleSize dan menge-decode bitmap dengan inSampleSize
    options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
    options.inJustDecodeBounds = false
    val scaledBitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(imageUri), null, options)
    return scaledBitmap!!
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2
        while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}

fun compressImage(bitmap: Bitmap): ByteArray {
    val outputStream = ByteArrayOutputStream()
    var quality = 90
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

    // Check the file size after the initial compression
    while (outputStream.toByteArray().size > 2 * 1024 * 1024 && quality > 20) {
        outputStream.reset()
        quality -= 10
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    }
    return outputStream.toByteArray()
}

fun rotateImage(img: Bitmap, degree: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degree.toFloat())
    return Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
}