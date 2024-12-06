package com.zhang.myproject.amap.view.fragment

import android.content.Intent
import android.os.Bundle
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.LatLng
import com.drake.logcat.LogCat
import com.zhang.myproject.amap.R
import com.zhang.myproject.amap.databinding.FragmentAMapBinding
import com.zhang.myproject.amap.view.activity.RecordTrackActivity
import com.zhang.myproject.amap.viewmodel.AMapViewModel
import com.zhang.myproject.base.fragment.BaseVBVMFragment
import com.zhang.myproject.base.utils.dpToPx
import com.zhang.myproject.base.utils.singleClick
import com.zhang.myproject.common.ktx.requestLocationPermission
import com.zhang.myproject.common.ktx.setMapAvatar
import com.zhang.myproject.common.ktx.setMapSkinModel

class AMapFragment : BaseVBVMFragment<FragmentAMapBinding, AMapViewModel>(R.layout.fragment_a_map),
    AMapLocationListener {

    companion object {
        @JvmStatic
        fun newInstance() = AMapFragment().apply { arguments = Bundle().apply {} }
    }

    private var aMap: AMap? = null

    private var mLocationClient: AMapLocationClient? = null

    override fun initView(savedInstanceState: Bundle?) {
        mViewBinding.apply {
            context?.requestLocationPermission {
                if (it) {
                    initMap(savedInstanceState)
                }
            }
        }

    }

    override fun setOnViewClick() {
        mViewBinding.apply {
            startRecordTrack.singleClick {
                startActivity(Intent(context, RecordTrackActivity::class.java))
            }
        }
    }

    override fun createObserver() {
        mViewModel.locationData.observe(this) {
            finishLoading()
        }
    }

    private fun initMap(savedInstanceState: Bundle?) {
        mViewBinding.apply {
            mapView.onCreate(savedInstanceState)
            aMap = mapView.map
            aMap?.uiSettings?.let {
                it.isZoomControlsEnabled = false
                it.isRotateGesturesEnabled = false
                it.isTiltGesturesEnabled = false
                it.logoPosition = AMapOptions.LOGO_POSITION_BOTTOM_CENTER
                it.setLogoBottomMargin(dpToPx(-20f))
            }
            aMap?.setLoadOfflineData(true)
            aMap?.setMapSkinModel()
            aMap?.isTrafficEnabled = false
            try {
                mLocationClient = AMapLocationClient(requireContext())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            initLocation()
        }
    }

    private fun initLocation() {
        stopLocation()
        requireContext().setMapAvatar(aMap)
        mLocationClient?.setLocationListener(this)
        val locationOption = AMapLocationClientOption()
        locationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        locationOption.isOnceLocation = true
        locationOption.isOnceLocationLatest = true
        locationOption.interval = 1000
        locationOption.isSensorEnable = true
        locationOption.isSelfStartServiceEnable = true
        locationOption.setSensorEnable(true)
        mLocationClient?.setLocationOption(locationOption)
        startLocation()
    }

    private fun stopLocation() {
        mLocationClient?.stopLocation()
    }

    /**
     * 开启定位
     */
    private fun startLocation() {
        mLocationClient?.startLocation()
    }

    override fun onLocationChanged(p0: AMapLocation?) {
        p0?.let {
            mViewModel.getLatLng(it.latitude, it.longitude)
            aMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 17f))
            LogCat.e("测试", "${LatLng(it.latitude, it.longitude)}")
        }
    }

    override fun onResume() {
        super.onResume()
        mViewBinding.apply {
            mapView.onResume()
        }
    }

    override fun onPause() {
        mViewBinding.apply {
            mapView.onPause()
        }
        super.onPause()
    }

    override fun onDestroy() {
        mViewBinding.apply {
            mapView.onDestroy()
        }
        super.onDestroy()
    }

}