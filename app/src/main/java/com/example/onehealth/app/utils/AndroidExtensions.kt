package com.example.onehealth.app.utils

import android.content.Context
import android.content.res.Resources
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat

private val resolvedColors = hashMapOf<Int, Int>()
fun Resources.getResolvedColor(@ColorRes colorId: Int): Int {
    return resolvedColors.getOrPut(colorId) {
        ResourcesCompat.getColor(this, colorId, null)
    }
}

private val resolvedDimensions = hashMapOf<Int, Int>()
fun Resources.getDimensionInPixels(@DimenRes dimensionId: Int): Int {
    return resolvedDimensions.getOrPut(dimensionId) {
        this.getDimensionPixelSize(dimensionId)
    }
}

fun Context.toastIt(
    @StringRes message: Int,
    duration: Int = Toast.LENGTH_LONG
) {
    Toast.makeText(this, message, duration).show()
}