package com.bleepingdragon.twiftly.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import java.time.LocalDateTime
import androidx.core.graphics.createBitmap

class MiscTools {

    companion object {

        public fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
            val drawable = ContextCompat.getDrawable(context, drawableId)
            val bitmap = createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }

        public fun getDateTimeFromString(dateString: String): LocalDateTime {
            return LocalDateTime.parse(dateString)
        }

        public fun getDateTimeStringFromDate(date: LocalDateTime): String {
            return date.toString()
        }

    }
}