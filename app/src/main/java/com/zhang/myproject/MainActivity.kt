package com.zhang.myproject

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import com.angcyo.tablayout.delegate2.ViewPager2Delegate
import com.hjq.permissions.XXPermissions
import com.zhang.amap.AMapFragment
import com.zhang.found.FoundFragment
import com.zhang.home.fragment.HomeFragment
import com.zhang.mine.MineFragment
import com.zhang.myproject.adapter.ViewPager2Adapter
import com.zhang.myproject.base.activity.BaseNetWorkActivity
import com.zhang.myproject.base.manager.ActivityManager
import com.zhang.myproject.base.utils.toast.Toasty
import com.zhang.myproject.common.helple.MMkvHelperUtils
import com.zhang.myproject.common.helple.PermissionConstant
import com.zhang.myproject.common.utils.MainHandlerUtils
import com.zhang.myproject.common.utils.PermissionUtils.checkLocationPermission
import com.zhang.myproject.databinding.ActivityMainBinding

/**
 * Date: 2023/7/6
 * Author : Zhang
 * Description :
 */
class MainActivity :
    BaseNetWorkActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main),
    SensorEventListener {

    override fun isLayoutToolbar(): Boolean = false

    private var sensorManager: SensorManager? = null

    private var mExitTime: Long = 0

    private var fragmentList = mutableListOf<Fragment>(
        HomeFragment.newInstance(),
        FoundFragment.newInstance(),
        AMapFragment.newInstance(),
        MineFragment.newInstance(),
    )

    override fun initView(savedInstanceState: Bundle?) {
        mViewBinding.apply {
//            ImmersionBar.with(this@MainActivity)
//                .fitsSystemWindows(false)
//                .statusBarDarkFont(true, 0.2f)
//                .init()
            ViewPager2Delegate.install(mainViewPager, tabLayout)

            val viewPager2Adapter = ViewPager2Adapter(this@MainActivity, fragmentList)
            mainViewPager.isUserInputEnabled = false
            mainViewPager.adapter = viewPager2Adapter
            checkLocationPermission {
                if (!it) {
                    MainHandlerUtils.postDelay(2000) {
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(
                            this@MainActivity,
                            PermissionConstant.locationList
                        )
                    }
                }
            }
        }
    }

    override fun setOnViewClick() {
    }

    override fun createObserver() {
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    private fun exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toasty.info("再按一次退出应用")
            mExitTime = System.currentTimeMillis()
        } else {
            ActivityManager.instance.finishAllActivity()
        }
    }

    override fun onResume() {
        super.onResume()
        startOrientationChangListener()
        sensorManager?.registerListener(
            this,
            sensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_GAME
        )
    }


    private fun startOrientationChangListener() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager  //获取传感器服务
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.run {
            if (sensor.type == Sensor.TYPE_ORIENTATION) {
                if (MMkvHelperUtils.getGyroAngle() == values[0]) {
                    return
                } else {
                    MMkvHelperUtils.setGyroAngle(values[0])
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onDestroy() {
        sensorManager?.unregisterListener(this)
        super.onDestroy()
    }
}