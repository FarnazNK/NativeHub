package com.nativehub.biometric

import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import java.util.concurrent.Executor

@ReactModule(name = BiometricModule.NAME)
class BiometricModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    companion object {
        const val NAME = "BiometricModule"
    }

    private val executor: Executor by lazy {
        ContextCompat.getMainExecutor(reactApplicationContext)
    }

    override fun getName(): String = NAME

    @ReactMethod
    fun isAvailable(promise: Promise) {
        try {
            val biometricManager = BiometricManager.from(reactApplicationContext)
            val canAuthenticate = biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
            )
            
            val result = Arguments.createMap().apply {
                putBoolean("available", canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS)
                putString("biometricType", detectBiometricType())
            }
            promise.resolve(result)
        } catch (e: Exception) {
            promise.reject("ERROR", e.message, e)
        }
    }

    @ReactMethod
    fun getBiometricType(promise: Promise) {
        promise.resolve(detectBiometricType())
    }

    @ReactMethod
    fun authenticate(options: ReadableMap?, promise: Promise) {
        val activity = currentActivity as? FragmentActivity ?: run {
            promise.reject("ERROR", "Activity not available")
            return
        }
        
        val title = options?.getString("title") ?: "Authenticate"
        val description = options?.getString("description") ?: "Verify your identity"
        
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                promise.resolve(Arguments.createMap().apply { putBoolean("success", true) })
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                promise.resolve(Arguments.createMap().apply {
                    putBoolean("success", false)
                    putMap("error", Arguments.createMap().apply {
                        putString("code", "AUTH_ERROR_$errorCode")
                        putString("message", errString.toString())
                    })
                })
            }
        }
        
        activity.runOnUiThread {
            val biometricPrompt = BiometricPrompt(activity, executor, callback)
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setDescription(description)
                .setNegativeButtonText("Cancel")
                .build()
            biometricPrompt.authenticate(promptInfo)
        }
    }

    @ReactMethod
    fun hasDeviceCredentials(promise: Promise) {
        val biometricManager = BiometricManager.from(reactApplicationContext)
        val canAuth = biometricManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL)
        promise.resolve(canAuth == BiometricManager.BIOMETRIC_SUCCESS)
    }

    private fun detectBiometricType(): String {
        val pm = reactApplicationContext.packageManager
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && 
                pm.hasSystemFeature("android.hardware.biometrics.face") -> "face"
            pm.hasSystemFeature("android.hardware.fingerprint") -> "fingerprint"
            else -> "none"
        }
    }
}
