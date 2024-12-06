package com.zhang.myproject.common.ktx

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.zhang.myproject.base.utils.singleClick
import com.zhang.myproject.common.R
import com.zhang.myproject.common.databinding.LayoutDashboardCommonDialogBinding
import razerdp.basepopup.BasePopupWindow

/**
 * Date: 2024/12/6
 * Author : Zhang
 * Description :
 */

class ShowDashboardPop(
    mContext: Context, title: String = getStringRes(R.string.prompt),
    content: String = getStringRes(R.string.prompt),
    titleSize: Int = 14, contentSize: Int = 14,
    backgroundRes: Int? = null, resId: Int? = null,
    leftStr: String? = null, rightStr: String? = null,
    mWidth: Int = getMargin28(), mHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    gravity: Int? = Gravity.CENTER, isCancel: Boolean = true,
    btnLeft: (() -> Unit)? = null, btnRight: (() -> Unit)? = null,
) : BasePopupWindow(mContext) {

    private var mBinding: LayoutDashboardCommonDialogBinding? = null

    init {
        mBinding = mContext.viewBindViewBinding(R.layout.layout_dashboard_common_dialog) as LayoutDashboardCommonDialogBinding?
        contentView = mBinding?.root
        width = mWidth
        height = mHeight
        popupGravity = gravity ?: Gravity.CENTER

        mBinding?.let {
            it.tvTitle.text = title
            it.tvContent.text = content

            if (backgroundRes != null) {
                it.llRoot.setBackgroundResource(backgroundRes)
            } else {
                it.llRoot.setBackgroundResource(R.drawable.bg_f5f6fa_0f1012_radius_14)
            }

            if (resId != null) {
                it.ivIcon.visibility = View.VISIBLE
                it.ivIcon.setBackgroundResource(resId)
            } else {
                it.ivIcon.visibility = View.GONE
            }
            it.tvTitle.textSize = titleSize.toFloat()
            it.tvContent.textSize = contentSize.toFloat()

            if (!TextUtils.isEmpty(title)) {
                it.tvTitle.visibility = View.VISIBLE
                it.tvTitle.text = title
                it.line.visibility = View.VISIBLE
            } else {
                it.tvTitle.visibility = View.GONE
                it.line.visibility = View.GONE
            }
            if (!TextUtils.isEmpty(content)) {
                it.tvContent.gravity = Gravity.CENTER
                it.tvContent.visibility = View.VISIBLE
                it.tvContent.text = content
                it.line.visibility = View.GONE
            } else {
                it.tvContent.visibility = View.GONE
                it.tvContent.text = content
            }
            if (!TextUtils.isEmpty(leftStr)) {
                it.tvLeft.text = leftStr
            } else {
                it.tvLeft.visibility = View.GONE
                it.vLine.visibility = View.GONE
            }
            if (!TextUtils.isEmpty(rightStr)) {
                it.tvRight.text = rightStr
            } else {
                it.tvRight.visibility = View.GONE
                it.vLine.visibility = View.GONE
            }

            it.tvLeft.singleClick {
                btnLeft?.invoke()
                dismiss()
            }
            it.tvRight.singleClick {
                btnRight?.invoke()
                dismiss()
            }
            setBackPressEnable(isCancel)
            setOutSideDismiss(isCancel)
        }
    }
}