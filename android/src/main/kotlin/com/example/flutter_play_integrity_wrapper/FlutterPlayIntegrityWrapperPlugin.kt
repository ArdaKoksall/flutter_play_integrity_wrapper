package com.example.flutter_play_integrity_wrapper

import android.content.Context
import androidx.annotation.NonNull
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.StandardIntegrityManager
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class FlutterPlayIntegrityWrapperPlugin: FlutterPlugin, MethodCallHandler {
    private lateinit var channel : MethodChannel
    private lateinit var context: Context

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_play_integrity_wrapper")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "requestIntegrityToken") {
            val nonce = call.argument<String>("nonce")
            val projectNumber = call.argument<String>("cloudProjectNumber")

            if (nonce == null || projectNumber == null) {
                result.error("INVALID_ARGS", "Nonce or Cloud Project Number is missing", null)
                return
            }

            requestToken(nonce, projectNumber.toLong(), result)
        } else {
            result.notImplemented()
        }
    }

    private fun requestToken(nonce: String, projectNumber: Long, result: Result) {
        try {
            val integrityManager = IntegrityManagerFactory.create(context)

            val request = com.google.android.play.core.integrity.IntegrityTokenRequest.builder()
                .setCloudProjectNumber(projectNumber)
                .setNonce(nonce)
                .build()

            integrityManager.requestIntegrityToken(request)
                .addOnSuccessListener { response ->
                    result.success(response.token())
                }
                .addOnFailureListener { e ->
                    result.error("INTEGRITY_FAILURE", e.message, null)
                }
        } catch (e: Exception) {
            result.error("EXCEPTION", e.message, null)
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}