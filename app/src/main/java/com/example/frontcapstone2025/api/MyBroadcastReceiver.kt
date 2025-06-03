package com.example.frontcapstone2025.api

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.frontcapstone2025.MainActivity

class MyBroadcastReceiver : BroadcastReceiver() {
    private val TAG = "MyBroadcastReceiver"

    object CaptureObservable {
        private val _runningStatus = MutableLiveData<Boolean>()
        val runningStatus: LiveData<Boolean> get() = _runningStatus

        fun update(running: Boolean) {
            _runningStatus.value = running
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d(TAG, "onReceive $action")

        if (action == MainActivity.CAPTURE_STATUS_ACTION) {
            // Notify via the CaptureObservable
            val running = intent.getBooleanExtra("running", true)
            CaptureObservable.update(running)
        }
    }
}

