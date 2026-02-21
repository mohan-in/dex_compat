package com.mohan.dex_compat

import android.app.Activity
import android.os.Build
import android.view.WindowInsets
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** DexCompatPlugin — detects Samsung DeX / freeform desktop windowing. */
class DexCompatPlugin :
    FlutterPlugin,
    MethodCallHandler,
    ActivityAware {

    private lateinit var channel: MethodChannel
    private var activity: Activity? = null

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "dex_compat")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "isDesktopMode") {
            result.success(isDesktopMode())
        } else {
            result.notImplemented()
        }
    }

    private fun isDesktopMode(): Boolean {
        val currentActivity = activity ?: return false

        // Primary: check for a captionBar inset (DeX window title bar).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val rootInsets = currentActivity.window.decorView.rootWindowInsets
            if (rootInsets != null) {
                val captionInsets = rootInsets.getInsets(WindowInsets.Type.captionBar())
                if (captionInsets.top > 0) {
                    return true
                }
            }
        }

        // Fallback: multi-window mode on a large screen (≥600dp).
        if (currentActivity.isInMultiWindowMode) {
            val isLargeScreen =
                currentActivity.resources.configuration.smallestScreenWidthDp >= 600
            if (isLargeScreen) {
                return true
            }
        }

        return false
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    // ActivityAware
    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivity() {
        activity = null
    }
}
