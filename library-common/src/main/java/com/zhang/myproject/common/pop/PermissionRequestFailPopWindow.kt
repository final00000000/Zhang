package com.zhang.myproject.common.pop

import android.content.Context
import android.view.Gravity
import com.zhang.myproject.base.utils.singleClick
import com.zhang.myproject.common.R
import com.zhang.myproject.common.databinding.PermissionRequestFailPopWindowBinding
import com.zhang.myproject.common.utils.getScreenWidth
import com.zhang.myproject.common.utils.viewBindViewBinding
import razerdp.basepopup.BasePopupWindow
import razerdp.util.animation.AnimationHelper
import razerdp.util.animation.Direction
import razerdp.util.animation.TranslationConfig

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
        width = getScreenWidth(context) - (dipToPx(32f).toInt() * 2)
        setOutSideDismiss(true)
//        showAnimation = AnimationHelper.asAnimation()
//            .withTranslation(TranslationConfig().from(Direction.BOTTOM)).toShow()
//        dismissAnimation = AnimationHelper.asAnimation()
//            .withTranslation(TranslationConfig.TO_TOP).toDismiss()
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