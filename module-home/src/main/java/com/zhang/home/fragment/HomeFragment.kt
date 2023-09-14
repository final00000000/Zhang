package com.zhang.home.fragment

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import com.makeramen.roundedimageview.RoundedImageView
import com.permissionx.guolindev.PermissionX
import com.zhang.home.R
import com.zhang.home.databinding.FragmentHomeBinding
import com.zhang.myproject.base.fragment.BaseFragment
import com.zhang.myproject.base.utils.AutoSizeUtils
import com.zhang.myproject.common.helple.MMkvHelperUtils

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home),
    AMapLocationListener {

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply { arguments = Bundle().apply {} }
    }

    private var aMap: AMap? = null

    private var mLocationClient: AMapLocationClient? = null

    private var mLocationMarker: Marker? = null

    private var mLatLng: LatLng? = null

    override fun initView(savedInstanceState: Bundle?) {
        mViewBinding.apply {
            val list = mutableListOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            PermissionX.init(this@HomeFragment).permissions(list)
                .request { allGranted, _, deniedList ->
                    if (allGranted) {
                        initMap(savedInstanceState)
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "这些权限被拒绝: $deniedList",
                            Toast.LENGTH_LONG
                        ).show()
                    }
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

    private fun initMap(savedInstanceState: Bundle?) {
        mViewBinding.apply {
            mapView.onCreate(savedInstanceState)
            mapSetting()
            aMap = mapView.map
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
        val myLocationStyle = MyLocationStyle()
        myLocationStyle.showMyLocation(true)
        // 圆圈的边框颜色
        myLocationStyle.strokeColor(Color.TRANSPARENT)
        // 圆圈的填充颜色
        myLocationStyle.radiusFillColor(Color.TRANSPARENT)
        // 圆圈的边框宽度
        myLocationStyle.strokeWidth(0f)
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
        aMap?.myLocationStyle = myLocationStyle
        aMap?.isMyLocationEnabled = false
        mLocationClient?.setLocationListener(this)
        val locationOption = AMapLocationClientOption()
        locationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        locationOption.isOnceLocation = true
        locationOption.isOnceLocationLatest = true
        locationOption.interval = 1000
        locationOption.isSensorEnable = true
        mLocationClient?.setLocationOption(locationOption)
        startLocation()
    }

    private fun mapSetting() {
        mViewBinding.apply {
            aMap?.uiSettings?.apply {
                isZoomControlsEnabled = false
                isRotateGesturesEnabled = false
                isTiltGesturesEnabled = false
                logoPosition = AMapOptions.LOGO_POSITION_BOTTOM_CENTER
                setLogoBottomMargin(AutoSizeUtils.dp2px(requireActivity(), -20f))
            }
        }
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
            mLatLng = LatLng(it.latitude, it.longitude)
            updateLocationMarker(mLatLng)
            aMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 17f))
        }
    }

    private fun updateLocationMarker(latLng: LatLng?) {
        mLocationMarker?.run {
            latLng?.run {
                position = latLng
            }
        } ?: kotlin.run {
            latLng?.run {
                mLocationMarker = aMap?.addMarker(
                    MarkerOptions().anchor(0.5f, 0.5f).position(mLatLng)
                        .icon(BitmapDescriptorFactory.fromView(getLocationMarkerView(MMkvHelperUtils.getGyroAngle())))
                )
            }
        }
    }

    private fun getLocationMarkerView(angle: Float): View {
        val view = View.inflate(requireActivity(), R.layout.layout_map_marker_view, null)
        val pointerView = view.findViewById<AppCompatImageView>(R.id.iv_pointer)
        val headerView = view.findViewById<RoundedImageView>(R.id.iv_head)
//        headerDrawable?.run {
//            headerView.setImageDrawable(headerDrawable)
//        }
        pointerView.rotation = angle
        return view
    }

    override fun onResume() {
        super.onResume()
        mViewBinding.apply {
            mapView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        mViewBinding.apply {
            mapView.onPause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewBinding.apply {
            mapView.onDestroy()
        }
    }

}