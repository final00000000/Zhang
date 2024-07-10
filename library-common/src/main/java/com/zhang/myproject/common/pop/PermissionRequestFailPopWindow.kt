package com.zhang.myproject.common.pop

import android.content.Context
import android.view.Gravity
import com.zhang.myproject.base.utils.singleClick
import com.zhang.myproject.common.R
import com.zhang.myproject.common.databinding.PermissionRequestFailPopWindowBinding
import com.zhang.myproject.common.ktx.getMargin32
import com.zhang.myproject.common.ktx.getScreenWidth
import com.zhang.myproject.common.ktx.viewBindViewBinding
import razerdp.basepopup.BasePopupWindow

/**
 * Date: 2023/12/8
 * Author : Zhang
 * Description :
 */
class PermissionRequestFailPopWindow(context: Context) : BasePopupWindow(context) {

    private var mBinding: PermissionRequestFailPopWindowBinding? = null

    init {
        mBinding =
            context.viewBindViewBinding(R.layout.permission_request_fail_pop_window) as PermissionRequestFailPopWindowBinding
        contentView = mBinding?.root
        popupGravity = Gravity.CENTER
        width = getScreenWidth() - getMargin32()
        setOutSideDismiss(true)
        mBinding?.apply {
            tvExit.singleClick {
                dismiss()
                callback?.invoke(false)
            }
            tvGoSettings.singleClick {
                dismiss()
                callback?.invoke(true)
            }
        }

    }

    private var callback: ((Boolean) -> Unit?)? = null

    fun requestSetting(callBack: ((Boolean) -> Unit)? = null): PermissionRequestFailPopWindow {
        this.callback = callBack
        return this
    }

    fun showPopup(title: String, content: String) {
        mBinding?.apply {
            tvTitle.text = title
            tvMsg.text = content
            showPopupWindow()
        }
    }
}