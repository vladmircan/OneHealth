package com.example.onehealth.app

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.onehealth.domain.sync.SyncManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationLifecycleObserver @Inject constructor(
    private val syncManager: SyncManager
): DefaultLifecycleObserver {

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        syncManager.sync()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        syncManager.cancel()
        super.onDestroy(owner)
    }
}