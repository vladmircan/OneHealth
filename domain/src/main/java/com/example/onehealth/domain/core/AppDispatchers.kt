package com.example.onehealth.domain.core

import kotlinx.coroutines.Dispatchers

object AppDispatchers {
    val IO = Dispatchers.IO
    val MAIN = Dispatchers.Main
    val DEFAULT = Dispatchers.Default
}