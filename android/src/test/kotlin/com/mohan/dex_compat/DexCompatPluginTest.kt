package com.mohan.dex_compat

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.mockito.Mockito
import kotlin.test.Test

internal class DexCompatPluginTest {
    @Test
    fun onMethodCall_isDesktopMode_returnsBoolean() {
        val plugin = DexCompatPlugin()

        val call = MethodCall("isDesktopMode", null)
        val mockResult: MethodChannel.Result = Mockito.mock(MethodChannel.Result::class.java)
        plugin.onMethodCall(call, mockResult)

        // Without an Activity attached, isDesktopMode() returns false.
        Mockito.verify(mockResult).success(false)
    }

    @Test
    fun onMethodCall_unknownMethod_returnsNotImplemented() {
        val plugin = DexCompatPlugin()

        val call = MethodCall("unknownMethod", null)
        val mockResult: MethodChannel.Result = Mockito.mock(MethodChannel.Result::class.java)
        plugin.onMethodCall(call, mockResult)

        Mockito.verify(mockResult).notImplemented()
    }
}
