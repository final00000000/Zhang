package com.zhang.home.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import com.hjq.permissions.XXPermissions
import com.makeramen.roundedimageview.RoundedImageView
import com.zhang.home.R
import com.zhang.home.databinding.FragmentHomeBinding
import com.zhang.myproject.base.AppGlobals
import com.zhang.myproject.base.fragment.BaseFragment
import com.zhang.myproject.base.utils.singleClick
import com.zhang.myproject.common.helple.MMkvHelperUtils
import com.zhang.myproject.common.utils.PermissionUtils.checkLocationPermission
import com.zhang.myproject.common.utils.mApplication
import me.jessyan.autosize.utils.AutoSizeUtils

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply { arguments = Bundle().apply {} }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBinding.apply {
            tvHome.singleClick {
            }
        }
    }

    override fun initData() {
        mViewBinding.apply {

        }
    }

    override fun setOnViewClick() {
        mViewBinding.apply {

        }
    }

}