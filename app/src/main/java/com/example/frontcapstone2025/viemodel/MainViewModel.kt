package com.example.frontcapstone2025.viemodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontcapstone2025.api.RetrofitManager
import com.example.frontcapstone2025.api.WifiPosition
import com.example.frontcapstone2025.utility.WifiUkf
import com.example.frontcapstone2025.utility.toDisplayList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlin.coroutines.resume
import java.io.File

class MainViewModel : ViewModel() {
    private val _chosenWifi = MutableStateFlow("")
    val chosenWifi: StateFlow<String> = _chosenWifi.asStateFlow()

    private val _upDistance = MutableStateFlow(-1.0)
    val upDistance: StateFlow<Double> = _upDistance.asStateFlow()
    private val _downDistance = MutableStateFlow(-1.0)
    val downDistance: StateFlow<Double> = _downDistance.asStateFlow()
    private val _frontDistance = MutableStateFlow(-1.0)
    val frontDistance: StateFlow<Double> = _frontDistance.asStateFlow()
    private val _leftDistance = MutableStateFlow(-1.0)
    val leftDistance: StateFlow<Double> = _leftDistance.asStateFlow()
    private val _armLength = MutableStateFlow(-1.0)
    val armLength: StateFlow<Double> = _armLength.asStateFlow()

    private val _wifiPosition = MutableStateFlow(WifiPosition())
    val wifiPosition: StateFlow<WifiPosition> = _wifiPosition.asStateFlow()

    private val _wifiSearchTime = MutableStateFlow(5000L)
    val wifiSearchTime: StateFlow<Long> = _wifiSearchTime.asStateFlow()

    private val _wifiListReady = MutableStateFlow(false)
    val wifiListReady: StateFlow<Boolean> = _wifiListReady

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private val _suspiciousWifi = MutableStateFlow<List<String>>(emptyList())
    val suspiciousWifi: StateFlow<List<String>> = _suspiciousWifi.asStateFlow()


    suspend fun getWifiPosition() {
        RetrofitManager.instance.getWifiPosition(
            upDistance = _upDistance.value / 100,
            downDistance = _downDistance.value / 100,
            frontDistance = _frontDistance.value / 100,
            leftDistance = _leftDistance.value / 100,
            armLength = _armLength.value,
            onSuccess = { wifiPosition: WifiPosition ->
                Log.d("wifiposition", wifiPosition.toString())
                _wifiPosition.update { wifiPosition }
            },
            onFailure = {

            }
        )
    }

    // MainViewModel.kt
    fun setChosenWifi(name: String) {
        _chosenWifi.value = name
    }

    fun getDistanceById(id: Int): StateFlow<Double> {
        return when (id) {
            1 -> upDistance
            2 -> downDistance
            3 -> leftDistance
            4 -> frontDistance
            else -> MutableStateFlow(-1.0) // 혹은 throw IllegalArgumentException
        }
    }

    fun setWifiListReady(value: Boolean) {
        _wifiListReady.value = value
    }

    fun setArmLength(value: Double) {
        _armLength.value = value
    }

    fun setShowDialog(value: Boolean) {
        _showDialog.value = value
    }

    fun isMeterChanged(): Boolean {
        val values = listOf(
            _upDistance.value,
            _downDistance.value,
            _frontDistance.value,
            _leftDistance.value
        )
        val count = values.count { kotlin.math.abs(it) > 1.0 }
        return count >= 3
    }

    fun setDistanceById(id: Int, value: Double) {
        when (id) {
            1 -> _upDistance.value = value
            2 -> _downDistance.value = value
            3 -> _leftDistance.value = value
            4 -> _frontDistance.value = value
            else -> {
                // 유효하지 않은 ID일 경우 로그 출력
                Log.w("MainViewModel", "Invalid ID passed to setDistanceById: $id")
            }
        }
    }


    // 버튼 클릭 시 쓰기 위해 일단 옮겨봄.
    suspend fun scanAndSaveDistance(
        id: Int,
        targetSsid: String,
        context: Context
    ) {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        if (!wifiManager.isWifiEnabled) wifiManager.isWifiEnabled = true

        val result = suspendCancellableCoroutine<List<ScanResult>> { cont ->
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(ctx: Context?, intent: Intent?) {
                    context.unregisterReceiver(this)
                    cont.resume(wifiManager.scanResults)
                }
            }
            context.registerReceiver(
                receiver,
                IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            )
            wifiManager.startScan()
        }

        val ukfMap = mutableMapOf<String, WifiUkf>()
        val displays = result.toDisplayList(ukfMap)

        val match = displays.find { it.ssid == targetSsid }
        match?.let {
            setDistanceById(id, it.distance)
        }
    }

    fun startCaptureAndAnalyze(context: Context) {
        viewModelScope.launch {
            try {
                val startIntent = Intent(Intent.ACTION_VIEW).apply {
                    setClassName("com.usbwifimon.app", "com.usbwifimon.app.CaptureCtrl")
                    putExtra("action", "start")
                    putExtra("channel", -1)
                    putExtra("channel_width", 1)
                    putExtra("pcap_name", "Capture.pcap")
                }
                context.startActivity(startIntent)
                delay(30_000L)
                val stopIntent = Intent(Intent.ACTION_VIEW).apply {
                    setClassName("com.usbwifimon.app", "com.usbwifimon.app.CaptureCtrl")
                    putExtra("action", "stop")
                }
                context.startActivity(stopIntent)
                // @todo: 파일 관련 다시 확인
                val file = File("/UsbWifiMonitor/Capture.pcap")
                RetrofitManager.instance.analyzePcap(
                    file = file,
                    onSuccess = { list -> _suspiciousWifi.value = list },
                    onFailure = {}
                )
            } catch (e: Exception) {
                Log.e("startCapture", e.toString())
            }
        }
    }
}