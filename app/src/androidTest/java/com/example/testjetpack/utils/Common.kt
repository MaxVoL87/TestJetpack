package com.example.testjetpack.utils

import android.content.Context
import android.telephony.TelephonyManager

//need     <uses-permission android:name="android.permission.MODIFY_PHONE_STATE"/> with can be granted only on Root devices
fun setMobileDataEnabled(context: Context, enabled: Boolean) {
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
    val setMobileDataEnabledMethod =
        Class.forName(telephonyManager!!.javaClass.name).getDeclaredMethod("setDataEnabled", java.lang.Boolean.TYPE).apply {
                isAccessible = true
            }
    setMobileDataEnabledMethod.invoke(telephonyManager, enabled)
}