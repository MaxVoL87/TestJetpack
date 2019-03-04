package com.example.testjetpack.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.testjetpack.R

internal const val NO_FLAGS = 0
internal const val FAKE_URL = "http://google.com"

inline fun <T, R> withNotNull(receiver: T?, block: T.() -> R): R? = receiver?.block()

fun Context.browseWithoutCurrentApp(url: String, newTask: Boolean = false): Boolean {
    val uri = Uri.parse(url)

    // It resolves issue with Samsung phones. So to have all possible activities and not only CurrentApp - don't remove it
    val fakeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(FAKE_URL))

    return packageManager
        .queryIntentActivities(fakeIntent, 0)
        .filter { it.activityInfo.packageName != packageName }
        .map {
            Intent(Intent.ACTION_VIEW).apply {
                data = uri
                `package` = it.activityInfo.packageName
                if (newTask) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
        }.takeIf { it.isNotEmpty() }
        ?.toMutableList()
        ?.let { intentList ->
            startActivity(
                Intent.createChooser(intentList.removeAt(0), "")
                    .apply {
                        if (intentList.isNotEmpty()) putExtra(
                            Intent.EXTRA_INITIAL_INTENTS,
                            arrayOf<Parcelable>(*intentList.toTypedArray())
                        )
                    })
            true
        } ?: false
}

fun Fragment.permissionsGranted(permissions: Array<String>, shouldAsk: Boolean, requestCode: Int = 0): Boolean {
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
        return true
    }

    val notGrantedPermissions = kotlin.collections.mutableListOf<String>()
    permissions.forEach {
        if (context?.checkSelfPermission(it) != android.content.pm.PackageManager.PERMISSION_GRANTED) notGrantedPermissions.add(
            it
        )
    }
    if (notGrantedPermissions.isEmpty()) return true

    if (!shouldAsk) return false

    val shouldShowRequestPermissionRationale = kotlin.collections.mutableListOf<String>()
    notGrantedPermissions.forEach {
        if (shouldShowRequestPermissionRationale(it)) shouldShowRequestPermissionRationale.add(it)
    }

    if (!shouldShowRequestPermissionRationale.isEmpty()) {
        //todo: change? maybe use interface with setAction?
//        CallbackDialog.newInstance(
//            getString(R.string.PleaseProvidePermissionsForContinueTheApplicationWorking),
//            buttonText = getString(R.string.ok)
//        )
//            .setAction { requestPermissions(notGrantedPermissions.toTypedArray(), requestCode) }
//            .show(fragmentManager, CallbackDialog::class.java.simpleName)
    } else {
        requestPermissions(notGrantedPermissions.toTypedArray(), requestCode)
    }
    return false
}

fun FragmentManager.clearBackStack() {
    for (i in 0 until backStackEntryCount) popBackStack()
}

fun Context.showNotImplemented() {
    Toast.makeText(this, "Not Implemented!", Toast.LENGTH_SHORT).show()
}


/**
 * Hide soft keyboard. Do nothing if keyboard is not opened
 */
fun EditText.hideKeyboard(): Boolean {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return inputManager.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Force open keyboard for given [EditText]
 */
fun EditText.showKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}