package com.example.customview.utils

import android.app.KeyguardManager
import android.content.Context
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal

/**
 * Created by test on 2017/9/14.
 * 指纹识别工具类
 */
object FingerprintUtil {
    private var cancellationSignal: CancellationSignal? = null

    @JvmStatic
    fun callFingerPrint(context: Context, listener: OnCallBackListener?) {
        val managerCompat = FingerprintManagerCompat.from(context)
        if (!managerCompat.isHardwareDetected) {
            listener?.onSupportFailed()
            return
        }

        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isKeyguardSecure) {
            listener?.onInsecurity()
            return
        }

        if (!managerCompat.hasEnrolledFingerprints()) {
            listener?.onEnrollFailed()
            return
        }

        listener?.onAuthenticationStart()
        cancellationSignal = CancellationSignal()

        managerCompat.authenticate(null, 0, cancellationSignal, object : FingerprintManagerCompat.AuthenticationCallback() {
            override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {
                listener?.onAuthenticationError(errMsgId, errString)
            }

            override fun onAuthenticationFailed() {
                listener?.onAuthenticationFailed()
            }

            override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
                listener?.onAuthenticationHelp(helpMsgId, helpString)
            }

            override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult) {
                listener?.onAuthenticationSucceeded(result)
            }
        }, null)
    }

    interface OnCallBackListener {
        fun onSupportFailed()

        fun onInsecurity()

        fun onEnrollFailed()

        fun onAuthenticationStart()

        fun onAuthenticationError(errMsgId: Int, errString: CharSequence?)

        fun onAuthenticationFailed()

        fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?)

        fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?)
    }

    @JvmStatic
    fun cancel() {
        cancellationSignal?.cancel()
    }
}
