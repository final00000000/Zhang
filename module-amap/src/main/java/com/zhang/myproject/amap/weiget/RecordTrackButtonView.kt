package com.zhang.myproject.amap.weiget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import com.zhang.myproject.amap.R
import com.zhang.myproject.base.utils.singleClick
import com.zhang.myproject.common.ktx.checkLocationPermission
import com.zhang.myproject.helper.RECORD_TRACK_FINISH
import com.zhang.myproject.helper.RECORD_TRACK_PAUSE
import com.zhang.myproject.helper.RECORD_TRACK_RESUME
import com.zhang.myproject.helper.RECORD_TRACK_START

/**
 * Date: 2024/6/4
 * Author : Zhang
 * Description :
 */
class RecordTrackButtonView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    private var mStart: LinearLayoutCompat? = null
    private var mPause: TextView? = null
    private var mResume: TextView? = null
    private var mFinish: RecordTrackSlideView? = null

    private var mCallBack: ActionCallBack? = null

    init {
        View.inflate(context, R.layout.layout_cycling_go_button_view, this)
        mStart = findViewById(R.id.ll_start)
        mPause = findViewById(R.id.tv_pause)
        mResume = findViewById(R.id.tv_resume)
        mFinish = findViewById(R.id.sl_finish)
        initViewClick()
    }

    private fun initViewClick() {
        mStart?.singleClick {
            if (!context.checkLocationPermission()) {
                mCallBack?.requestPermission {
                    if (it) {
                        setButtonStatus(RECORD_TRACK_START)
                    }
                }
            } else {
                setButtonStatus(RECORD_TRACK_START)
            }
        }
        mPause?.singleClick {
            setButtonStatus(RECORD_TRACK_PAUSE)
        }
        mResume?.singleClick {
            setButtonStatus(RECORD_TRACK_RESUME)
        }

        mFinish?.setOnSlideClickListener {
            if (it == 1) {
                setButtonStatus(RECORD_TRACK_FINISH)
            }
        }
    }

    fun setButtonStatus(status: String, isCallBack: Boolean = true) {
        when (status) {
            RECORD_TRACK_START -> {
                mStart?.isVisible = false
                mPause?.isVisible = true
                mResume?.isVisible = false
                mFinish?.isVisible = true
                if (isCallBack) {
                    mCallBack?.onStart()
                }
                mFinish?.setStartNavigation()
            }

            RECORD_TRACK_PAUSE -> {
                mStart?.isVisible = false
                mPause?.isVisible = false
                mResume?.isVisible = true
                mFinish?.isVisible = true
                mCallBack?.onPaused()
            }

            RECORD_TRACK_RESUME -> {
                mStart?.isVisible = false
                mPause?.isVisible = true
                mResume?.isVisible = false
                mFinish?.isVisible = true
                mCallBack?.onResume()
            }

            RECORD_TRACK_FINISH -> {
                mCallBack?.onFinish()
            }
        }
    }

    fun autoStartRecord() {
        mStart?.isVisible = false
        mPause?.isVisible = true
        mResume?.isVisible = false
        mFinish?.isVisible = true
        mFinish?.setStartNavigation()
    }

    fun resetDefault() {
        mStart?.isVisible = true
        mPause?.isVisible = false
        mResume?.isVisible = false
        mFinish?.isVisible = false
    }

    fun setCallBack(callBack: ActionCallBack) {
        mCallBack = callBack
    }
}

interface ActionCallBack {
    fun requestPermission(call: ((Boolean) -> Unit))
    fun onStart()
    fun onPaused()
    fun onResume()
    fun onFinish()
}