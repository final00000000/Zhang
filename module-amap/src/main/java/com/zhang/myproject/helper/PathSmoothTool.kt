package com.zhang.myproject.helper

import android.util.Log
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.amap.api.trace.TraceLocation

class PathSmoothTool {

    val CAR_MAX_SPEED = 120

    /**
     * 是否是第一次定位点
     */
    private var isFirst = true

    /**
     * 权重点1
     */
    private var weight1 = TraceLocation()

    /**
     * 权重点2
     */
    private var weight2: TraceLocation? = null

    /**
     * w1的临时定位点集合
     */
    private val w1TempList: MutableList<TraceLocation> = ArrayList()

    /**
     * w2的临时定位点集合
     */
    private val w2TempList: MutableList<TraceLocation> = ArrayList()

    /**
     * 统计w1Count所统计过的点数
     */
    private var w1Count = 0

    /**
     * 返回集合点
     */
    var mListPoint: MutableList<TraceLocation> = ArrayList()

    /**
     * @param aMapLocation
     * @return 是否获得有效点，需要存储和计算距离
     */
    fun filterPos(aMapLocation: TraceLocation, isPaused: Boolean): Pair<Boolean,Int> {
        var filterString = ""
        return try {
            // 获取的第一个定位点不进行过滤
            if (isFirst) {
                isFirst = false
                weight1.latitude = aMapLocation.latitude
                weight1.longitude = aMapLocation.longitude
                weight1.time = aMapLocation.time
                /****************存储数据到文件中，后面好分析 */
                filterString += "第一次" + " : "
                /** */

                // 将得到的第一个点存储入w1的缓存集合
                val traceLocation = TraceLocation()
                traceLocation.latitude = aMapLocation.latitude
                traceLocation.longitude = aMapLocation.longitude
                traceLocation.time = aMapLocation.time
                w1TempList.add(traceLocation)
                w1Count++
                Pair(true,0)
            } else {
                filterString += "非第一次" + " : "
                // 过滤静止时的偏点，在静止时速度小于1米就算做静止状态
                if (aMapLocation.speed < 1) {
                    return   Pair(false,0)
                }
                /****************存储数据到文件中，后面好分析 */
//				SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
//				Date date1 = new Date(aMapLocation.getTime());
//				String time1 = df1.format(date1); // 定位时间
//				String testString = time1 + ":" + aMapLocation.getTime() + "," + aMapLocation.getLatitude() + "," + aMapLocation.getLongitude() + "," + aMapLocation.getSpeed() + "\r\n";
////				FileWriteUtil.getInstance().save("tutu_driver_true.txt", testString);
//				Log.d("wsh",testString);
                if (weight2 == null) {
                    filterString += "weight2=null" + " : "
                    // 计算w1与当前定位点p1的时间差并得到最大偏移距离D
                    val offsetTimeMils = aMapLocation.time - weight1.time
                    val offsetTimes = offsetTimeMils / 1000
                    val MaxDistance = offsetTimes * CAR_MAX_SPEED
                    val distance = AMapUtils.calculateLineDistance(
                        LatLng(weight1.latitude, weight1.longitude),
                        LatLng(aMapLocation.latitude, aMapLocation.longitude)
                    )
                    val speed = distance*3.6

                    filterString += "distance = $distance,MaxDistance = $MaxDistance : "
                    if (speed > CAR_MAX_SPEED) {
                        filterString += "distance > MaxDistance" + "当前点 距离大: 设置w2位新的点，并添加到w2TempList"
                        // 将设置w2位新的点，并存储入w2临时缓存
                        weight2 = TraceLocation()
                        weight2!!.latitude = aMapLocation.latitude
                        weight2!!.longitude = aMapLocation.longitude
                        weight2!!.time = aMapLocation.time
                        w2TempList.add(weight2!!)
                        Pair(false,1)
                    } else {
                        filterString += "distance < MaxDistance" + "当前点 距离小 : 添加到w1TempList"
                        // 将p1加入到做坐标集合w1TempList
                        val traceLocation = TraceLocation()
                        traceLocation.latitude = aMapLocation.latitude
                        traceLocation.longitude = aMapLocation.longitude
                        traceLocation.time = aMapLocation.time
                        w1TempList.add(traceLocation)
                        w1Count++
                        // 更新w1权值点
                        weight1.latitude = weight1.latitude * 0.2 + aMapLocation.latitude * 0.8
                        weight1.longitude = weight1.longitude * 0.2 + aMapLocation.longitude * 0.8
                        weight1.time = aMapLocation.time
                        weight1.speed = aMapLocation.speed

//						if (w1TempList.size() > 3) {
//							filterString += "d1TempList.size() > 3" + " : 更新";
//							// 将w1TempList中的数据放入finalList，并将w1TempList清空
//							mListPoint.addAll(w1TempList);
//							w1TempList.clear();
//							return true;
//						} else {
//							filterString += "d1TempList.size() < 3" + " : 不更新";
//							return false;
//						}
                        if (w1Count > 3) {
                            filterString += " : 更新"
                            if (!isPaused) {
                                mListPoint.addAll(w1TempList)
                            }
                            w1TempList.clear()
                             Pair(true,0)
                        } else {
                            filterString += " w1Count<3: 不更新"
                            Pair(false,0)
                        }
                    }
                } else {
                    filterString += "weight2 != null" + " : "
                    // 计算w2与当前定位点p1的时间差并得到最大偏移距离D
                    val offsetTimeMils = aMapLocation.time - weight2!!.time
                    val offsetTimes = offsetTimeMils / 1000
                    val MaxDistance = offsetTimes * 16
                    val distance = AMapUtils.calculateLineDistance(
                        LatLng(weight2!!.latitude, weight2!!.longitude),
                        LatLng(aMapLocation.latitude, aMapLocation.longitude)
                    )
                    val speed = distance*3.6
                    filterString += "distance = $distance,MaxDistance = $MaxDistance : "
                    if (speed > CAR_MAX_SPEED) {
                        filterString += "当前点 距离大: weight2 更新"
                        w2TempList.clear()
                        // 将设置w2位新的点，并存储入w2临时缓存
                        weight2 = TraceLocation()
                        weight2!!.latitude = aMapLocation.latitude
                        weight2!!.longitude = aMapLocation.longitude
                        weight2!!.time = aMapLocation.time
                        w2TempList.add(weight2!!)
                        Pair(false,1)
                    } else {
                        filterString += "当前点 距离小: 添加到w2TempList"

                        // 将p1加入到做坐标集合w2TempList
                        val traceLocation = TraceLocation()
                        traceLocation.latitude = aMapLocation.latitude
                        traceLocation.longitude = aMapLocation.longitude
                        traceLocation.time = aMapLocation.time
                        w2TempList.add(traceLocation)

                        // 更新w2权值点
                        weight2!!.latitude = weight2!!.latitude * 0.2 + aMapLocation.latitude * 0.8
                        weight2!!.longitude =
                            weight2!!.longitude * 0.2 + aMapLocation.longitude * 0.8
                        weight2!!.time = aMapLocation.time
                        weight2!!.speed = aMapLocation.speed
                        if (w2TempList.size > 4) {
                            // 判断w1所代表的定位点数是否>4,小于说明w1之前的点为从一开始就有偏移的点
                            if (w1Count > 4) {
                                filterString += "w1Count > 4" + "计算增加W1"
                                if (!isPaused) {
                                    mListPoint.addAll(w1TempList)
                                }
                            } else {
                                filterString += "w1Count < 4" + "计算丢弃W1"
                                w1TempList.clear()
                            }
                            filterString += "w2TempList.size() > 4" + " : 更新到偏移点"

                            // 将w2TempList集合中数据放入finalList中
                            if (!isPaused) {
                                mListPoint.addAll(w2TempList)
                            }

                            // 1、清空w2TempList集合 2、更新w1的权值点为w2的值 3、将w2置为null
                            w2TempList.clear()
                            weight2?.run {
                                weight1 = this
                            }
                            weight2 = null
                            Pair(true,0)
                        } else {
                            filterString += """
                            w2TempList.size() < 4
                            
                            """.trimIndent()
                            Pair(false,0)
                        }
                    }
                }
            }
        } finally {
            Log.d("wsh", filterString)
        }
    }

    fun reset() {
        isFirst = true
        weight1 = TraceLocation()
        w1TempList.clear()
        w2TempList.clear()
        w1Count = 0
        mListPoint.clear()
    }
}