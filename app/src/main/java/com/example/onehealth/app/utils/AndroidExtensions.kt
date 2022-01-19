package com.example.onehealth.app.utils

import android.content.res.Resources
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
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