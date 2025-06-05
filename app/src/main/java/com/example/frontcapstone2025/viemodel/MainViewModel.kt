package com.example.frontcapstone2025.viemodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.frontcapstone2025.api.RetrofitManager
import com.example.frontcapstone2025.api.WifiPosition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

    suspend fun getWifiPosition() {
        RetrofitManager.instance.getWifiPosition(
            upDistance = _upDistance.value,
            downDistance = _downDistance.value,
            frontDistance = _frontDistance.value,
            leftDistance = _leftDistance.value,
            armLength = _armLength.value,
            onSuccess = { wifiPosition: WifiPosition ->
                Log.d("wifiposition", wifiPosition.toString())
                _wifiPosition.update { wifiPosition }
            },
            onFailure = {

            }
        )
    }
}