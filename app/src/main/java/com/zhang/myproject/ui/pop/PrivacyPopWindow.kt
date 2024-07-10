package com.zhang.myproject.ui.pop

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.view.Gravity
import android.view.animation.Animation
import com.drake.spannable.movement.ClickableMovementMethod
import com.drake.spannable.replaceSpan
import com.zhang.myproject.R
import com.zhang.myproject.base.utils.singleClick
import com.zhang.myproject.base.utils.toast.Toasty
import com.zhang.myproject.common.ktx.getColorRes
import com.zhang.myproject.common.ktx.getMargin32
import com.zhang.myproject.common.ktx.getScreenWidth
import com.zhang.myproject.common.ktx.getStringRes
import com.zhang.myproject.common.ktx.viewBindViewBinding
import com.zhang.myproject.common.utils.SimpleSpStringBuilder
import com.zhang.myproject.databinding.PopWindowPrivacyBinding
import razerdp.basepopup.BasePopupWindow
import razerdp.util.animation.AnimationHelper
import razerdp.util.animation.Direction
import razerdp.util.animation.TranslationConfig
import timber.log.Timber

/**
 * Date: 2023/10/11
 * Author : Zhang
 * Description :
 */
class PrivacyPopWindow(context: Context, private var success: (Boolean?) -> Unit) :
    BasePopupWindow(context) {

    private var mBinding: PopWindowPrivacyBinding? = null

    init {
        mBinding =
            context.viewBindViewBinding(R.layout.pop_window_privacy) as PopWindowPrivacyBinding
        contentView = mBinding?.root
        popupGravity = Gravity.CENTER
        width = getScreenWidth() - getMargin32()
        height = WRAP_CONTENT
        setOutSideDismiss(false)
        setBackPressEnable(false)
        showAnimation = AnimationHelper.asAnimation()
            .withTranslation(TranslationConfig().from(Direction.BOTTOM)).toShow()
        dismissAnimation = AnimationHelper.asAnimation()
            .withTranslation(TranslationConfig.TO_TOP).toDismiss()

        mBinding?.apply {
            tvContent.movementMethod = ClickableMovementMethod.getInstance()
            tvContent.text = getStringRes(com.zhang.myproject.resource.R.string.main_privacy_info)
                .replaceSpan(getStringRes(com.zhang.myproject.resource.R.string.main_privacy_service_agreement)) {
                    URLSpan("https://www.baidu.com")
                }
                .replaceSpan(getStringRes(com.zhang.myproject.resource.R.string.main_privacy_privacy_policy)) {
                    URLSpan("https://www.baidu.com")
                }

            tvExit.singleClick {
                success.invoke(null)
                dismiss()
            }

            tvConfirm.singleClick {
                success.invoke(true)
                dismiss()
            }
        }
        showPopupWindow()
    }

    private fun getStrBuilder(): SpannableStringBuilder {
        val boldText = getStringRes(com.zhang.myproject.resource.R.string.main_privacy_info)
        val boldText1 =
            getStringRes(com.zhang.myproject.resource.R.string.main_privacy_service_agreement)
        val boldText2 =
            getStringRes(com.zhang.myproject.resource.R.string.main_privacy_privacy_policy)
        return SimpleSpStringBuilder(context)
            .create()
            .setText(
                text = boldText,
                textColor = getColorRes(com.zhang.myproject.resource.R.color.color_222222)
            )
            .setText(
                text = boldText1,
                styleSpan = StyleSpan(Typeface.BOLD),
                textColor = getColorRes(com.zhang.myproject.resource.R.color.color_5A),
                clickable = {
                    Timber.d("测试getStrBuilder_73：${1111}")
                    Toasty.info("服务协议")
                },
            )
            .setText(
                text = boldText2,
                styleSpan = StyleSpan(Typeface.BOLD),
                textColor = getColorRes(com.zhang.myproject.resource.R.color.color_5A),
                clickable = {
                    Timber.d("测试getStrBuilder_73：${2222}")
                    Toasty.info("隐私政策")
                }
            )
            .builder()
    }

    override fun onCreateShowAnimation(): Animation? {
        return AnimationHelper.asAnimation()
            .withTranslation(TranslationConfig().from(Direction.BOTTOM)).toShow()
    }

    override fun onCreateDismissAnimation(): Animation? {
        return AnimationHelper.asAnimation().withTranslation(TranslationConfig.TO_BOTTOM)
            .toDismiss()
    }
}