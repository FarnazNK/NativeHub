package com.nativehub.performance

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.view.Choreographer
import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@ReactModule(name = PerformanceModule.NAME)
class PerformanceModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    companion object {
        const val NAME = "PerformanceModule"
    }

    private val activeTraces = ConcurrentHashMap<String, Long>()
    private val frameCount = AtomicInteger(0)

    override fun getName(): String = NAME

    @ReactMethod
    fun getMetrics(promise: Promise) {
        try {
            val activityManager = reactApplicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            
            val runtime = Runtime.getRuntime()
            val batteryStatus = getBatteryStatus()
            
            promise.resolve(Arguments.createMap().apply {
                putDouble("cpuUsage", 0.0)
                putDouble("memoryUsage", (runtime.totalMemory() - runtime.freeMemory()).toDouble())
                putDouble("availableMemory", memoryInfo.availMem.toDouble())
                putDouble("totalMemory", memoryInfo.totalMem.toDouble())
                putDouble("batteryLevel", batteryStatus.first)
                putBoolean("isCharging", batteryStatus.second)
                putDouble("timestamp", System.currentTimeMillis().toDouble())
            })
        } catch (e: Exception) {
            promise.reject("ERROR", e.message, e)
        }
    }

    @ReactMethod
    fun getFrameMetrics(promise: Promise) {
        promise.resolve(Arguments.createMap().apply {
            putDouble("fps", 60.0)
            putInt("droppedFrames", 0)
            putInt("totalFrames", frameCount.get())
            putDouble("avgFrameTime", 16.67)
        })
    }

    @ReactMethod
    fun startTrace(traceName: String, promise: Promise) {
        activeTraces[traceName] = System.currentTimeMillis()
        promise.resolve(null)
    }

    @ReactMethod
    fun stopTrace(traceName: String, promise: Promise) {
        val startTime = activeTraces.remove(traceName)
        if (startTime == null) {
            promise.reject("ERROR", "Trace not found")
            return
        }
        val endTime = System.currentTimeMillis()
        promise.resolve(Arguments.createMap().apply {
            putString("name", traceName)
            putDouble("startTime", startTime.toDouble())
            putDouble("endTime", endTime.toDouble())
            putDouble("duration", (endTime - startTime).toDouble())
            putMap("attributes", Arguments.createMap())
        })
    }

    @ReactMethod
    fun startFPSMonitoring(intervalMs: Double?, promise: Promise) {
        promise.resolve(null)
    }

    @ReactMethod
    fun stopFPSMonitoring(promise: Promise) {
        promise.resolve(null)
    }

    private fun getBatteryStatus(): Pair<Double, Boolean> {
        val intent = reactApplicationContext.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val batteryPct = if (level >= 0 && scale > 0) (level.toDouble() / scale * 100) else 0.0
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
        return Pair(batteryPct, isCharging)
    }
}
