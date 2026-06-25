package com.example.customview.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.databinding.ActivityLoadingViewBinding
import com.example.customview.loadingview.LVChromeLogo
import com.example.customview.loadingview.LVCircular
import com.example.customview.loadingview.LVCircularCD
import com.example.customview.loadingview.LVLineWithText
import com.example.customview.loadingview.LVSunSetView
import com.example.customview.loadingview.view.LVBattery
import com.example.customview.loadingview.view.LVBlazeWood
import com.example.customview.loadingview.view.LVBlock
import com.example.customview.loadingview.view.LVCircularJump
import com.example.customview.loadingview.view.LVCircularRing
import com.example.customview.loadingview.view.LVCircularSmile
import com.example.customview.loadingview.view.LVCircularZoom
import com.example.customview.loadingview.view.LVEatBeans
import com.example.customview.loadingview.view.LVFinePoiStar
import com.example.customview.loadingview.view.LVFunnyBar
import com.example.customview.loadingview.view.LVGears
import com.example.customview.loadingview.view.LVGearsTwo
import com.example.customview.loadingview.view.LVGhost
import com.example.customview.loadingview.view.LVNews
import com.example.customview.loadingview.view.LVPlayBall
import com.example.customview.loadingview.view.LVRingProgress
import com.example.customview.loadingview.view.LVWifi
import java.util.Timer
import java.util.TimerTask

/**
 * @author test
 */
open class LoadingViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingViewBinding
    private lateinit var mLVPlayBall: LVPlayBall
    private lateinit var mLVCircularRing: LVCircularRing
    private lateinit var mLVCircular: LVCircular
    private lateinit var mLVCircularJump: LVCircularJump
    private lateinit var mLVCircularZoom: LVCircularZoom
    private lateinit var mLVLineWithText: LVLineWithText
    private lateinit var mLVEatBeans: LVEatBeans
    private lateinit var mLVCircularCD: LVCircularCD
    private lateinit var mLVCircularSmile: LVCircularSmile
    private lateinit var mLVGears: LVGears
    private lateinit var mLVGearsTwo: LVGearsTwo
    private lateinit var mLVFinePoiStar: LVFinePoiStar
    private lateinit var mLVChromeLogo: LVChromeLogo
    private lateinit var mLVBattery: LVBattery
    private lateinit var mLVWifi: LVWifi
    private lateinit var mLVNews: LVNews
    private lateinit var mLVBlock: LVBlock
    private lateinit var mLVGhost: LVGhost
    private lateinit var mLVFunnyBar: LVFunnyBar
    private lateinit var mLVRingProgress: LVRingProgress
    private lateinit var lv_sunset: LVSunSetView
    private lateinit var mLVBlazeWood: LVBlazeWood
    private var mValueLVLineWithText = 0
    private var mValueLVNews = 0

    @JvmField
    var mTimerLVLineWithText: Timer? = Timer()

    @JvmField
    var mTimerLVNews: Timer? = Timer()

    private val mHandle = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 2) {
                mLVLineWithText.setValue(msg.arg1)
            } else if (msg.what == 1) {
                mLVNews.setValue(msg.arg1)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mLVCircular = binding.lvCircular.apply {
            setViewColor(Color.rgb(255, 99, 99))
            setRoundColor(Color.rgb(255, 0, 0))
        }
        mLVCircularCD = binding.lvCircularCD.apply {
            setViewColor(Color.rgb(0, 255, 0))
        }
        mLVLineWithText = binding.lvLinetext.apply {
            setViewColor(Color.rgb(33, 66, 77))
            setTextColor(Color.rgb(233, 166, 177))
        }
        mLVCircularJump = binding.lvCircularJump.apply {
            setViewColor(Color.rgb(133, 66, 99))
        }
        mLVCircularRing = binding.lvCircularring.apply {
            setViewColor(Color.argb(100, 255, 255, 255))
            setBarColor(Color.YELLOW)
        }
        mLVCircularSmile = binding.lvCircularSmile.apply {
            setViewColor(Color.rgb(144, 238, 146))
        }
        mLVCircularZoom = binding.lvCircularZoom.apply {
            setViewColor(Color.rgb(255, 0, 122))
        }
        mLVEatBeans = binding.lvEatBeans.apply {
            setViewColor(Color.WHITE)
            setEyeColor(Color.BLUE)
        }
        mLVFinePoiStar = binding.lvFinePoiStar.apply {
            setViewColor(Color.WHITE)
            setCircleColor(Color.YELLOW)
            setDrawPath(true)
        }
        mLVGears = binding.lvGears.apply {
            setViewColor(Color.rgb(55, 155, 233))
        }
        mLVGearsTwo = binding.lvGearsTwo.apply {
            setViewColor(Color.rgb(155, 55, 233))
        }
        mLVWifi = binding.lvWifi.apply {
            setViewColor(Color.BLACK)
        }
        mLVNews = binding.lvNews.apply {
            setViewColor(Color.WHITE)
        }
        mLVRingProgress = binding.lvRingp.apply {
            setViewColor(Color.WHITE)
            setTextColor(Color.BLACK)
            setPorBarStartColor(Color.YELLOW)
            setPorBarEndColor(Color.BLUE)
        }
        mLVGhost = binding.lvGhost.apply {
            setViewColor(Color.WHITE)
            setHandColor(Color.BLACK)
        }
        mLVPlayBall = binding.lvPlayball.apply {
            setViewColor(Color.WHITE)
            setBallColor(Color.RED)
        }
        mLVChromeLogo = binding.lvChromeLogo
        mLVBattery = binding.lvBattery.apply {
            setBatteryOrientation(LVBattery.BatteryOrientation.VERTICAL)
            setShowNum(false)
            setViewColor(Color.WHITE)
            setCellColor(Color.GREEN)
        }
        mLVBlock = binding.lvBlock.apply {
            setViewColor(Color.rgb(245, 209, 22))
            setShadowColor(Color.BLACK)
        }
        mLVFunnyBar = binding.lvFunnybar.apply {
            setViewColor(Color.rgb(234, 167, 107))
        }
        mLVBlazeWood = binding.lvWood
        lv_sunset = binding.lvSunset.apply {
            setSunendTime("16:20")
            setSunstartTime("07:16")
        }
    }

    fun startAnim(v: View) {
        stopAll()
        when (v) {
            is LVCircular -> v.startAnim()
            is LVCircularCD -> v.startAnim()
            is LVCircularSmile -> v.startAnim()
            is LVCircularRing -> v.startAnim()
            is LVCircularZoom -> v.startAnim()
            is LVCircularJump -> v.startAnim()
            is LVEatBeans -> v.startAnim(3500)
            is LVPlayBall -> v.startAnim()
            is LVLineWithText -> startLVLineWithTextAnim()
            is LVGears -> v.startAnim()
            is LVGearsTwo -> v.startAnim()
            is LVFinePoiStar -> {
                v.setDrawPath(false)
                v.startAnim(3500)
            }
            is LVChromeLogo -> v.startAnim()
            is LVBattery -> v.startAnim(5000)
            is LVWifi -> v.startAnim(9000)
            is LVNews -> startLVNewsAnim()
            is LVBlock -> v.startAnim()
            is LVGhost -> v.startAnim()
            is LVFunnyBar -> v.startAnim()
            is LVRingProgress -> v.startAnim(3000)
            is LVBlazeWood -> v.startAnim(500)
            is LVSunSetView -> v.startSunset()
            else -> Unit
        }
    }

    fun startAnimAll(v: View) {
        mLVCircular.startAnim()
        mLVCircularRing.startAnim()
        mLVPlayBall.startAnim()
        mLVCircularJump.startAnim()
        mLVCircularZoom.startAnim()
        startLVLineWithTextAnim()
        mLVEatBeans.startAnim(3500)
        mLVCircularCD.startAnim()
        mLVCircularSmile.startAnim(1000)
        mLVGears.startAnim()
        mLVGearsTwo.startAnim()
        mLVFinePoiStar.setDrawPath(true)
        mLVFinePoiStar.startAnim(3500)
        mLVChromeLogo.startAnim()
        mLVBattery.startAnim(5000)
        mLVWifi.startAnim(9000)
        startLVNewsAnim()
        mLVBlock.startAnim()
        mLVGhost.startAnim()
        mLVFunnyBar.startAnim()
        mLVRingProgress.startAnim(3000)
        mLVBlazeWood.startAnim(500)
        lv_sunset.startSunset()
    }

    fun stopAnimAll(v: View) {
        stopAll()
    }

    private fun stopAll() {
        mLVCircular.stopAnim()
        mLVPlayBall.stopAnim()
        mLVCircularJump.stopAnim()
        mLVCircularZoom.stopAnim()
        mLVCircularRing.stopAnim()
        mLVEatBeans.stopAnim()
        stopLVLineWithTextAnim()
        mLVCircularCD.stopAnim()
        mLVCircularSmile.stopAnim()
        mLVGears.stopAnim()
        mLVGearsTwo.stopAnim()
        mLVFinePoiStar.stopAnim()
        mLVChromeLogo.stopAnim()
        mLVBattery.stopAnim()
        mLVWifi.stopAnim()
        stopLVNewsAnim()
        mLVBlock.stopAnim()
        mLVGhost.stopAnim()
        mLVFunnyBar.stopAnim()
        mLVRingProgress.stopAnim()
        mLVBlazeWood.stopAnim()
        lv_sunset.stopAnim()
    }

    private fun startLVLineWithTextAnim() {
        mValueLVLineWithText = 0
        mTimerLVLineWithText?.cancel()
        mTimerLVLineWithText = Timer()
        timerTaskLVLineWithText()
    }

    private fun stopLVLineWithTextAnim() {
        mTimerLVLineWithText?.cancel()
        mLVLineWithText.setValue(mValueLVLineWithText)
    }

    private fun startLVNewsAnim() {
        mValueLVNews = 0
        mTimerLVNews?.cancel()
        mTimerLVNews = Timer()
        timerTaskLVNews()
    }

    private fun stopLVNewsAnim() {
        mLVNews.stopAnim()
        mTimerLVNews?.cancel()
        mLVNews.setValue(mValueLVNews)
    }

    fun timerTaskLVNews() {
        val timer = mTimerLVNews ?: return
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (mValueLVNews < 100) {
                    mValueLVNews++
                    mHandle.obtainMessage(1).apply {
                        arg1 = mValueLVNews
                        sendToTarget()
                    }
                } else {
                    timer.cancel()
                }
            }
        }, 0, 10)
    }

    fun timerTaskLVLineWithText() {
        val timer = mTimerLVLineWithText ?: return
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (mValueLVLineWithText < 100) {
                    mValueLVLineWithText++
                    mHandle.obtainMessage(2).apply {
                        arg1 = mValueLVLineWithText
                        sendToTarget()
                    }
                } else {
                    timer.cancel()
                }
            }
        }, 0, 50)
    }

    override fun onDestroy() {
        stopAll()
        mTimerLVLineWithText?.cancel()
        mTimerLVLineWithText = null
        mTimerLVNews?.cancel()
        mTimerLVNews = null
        mHandle.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
