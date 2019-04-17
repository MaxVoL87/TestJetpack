package com.example.testjetpack.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import android.graphics.drawable.Drawable
import android.R
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import com.google.android.gms.maps.model.BitmapDescriptor


internal const val NO_FLAGS = 0
internal const val FAKE_URL = "http://google.com"

inline fun <T, R> withNotNull(receiver: T?, block: T.() -> R): R? = receiver?.block()
inline fun <T : List<*>, R> withNotNullOrEmpty(receiver: T?, block: T.() -> R): R? =
    if (receiver.isNullOrEmpty()) null else receiver.block()

inline fun <A, B, R> withNotNull(arg1: A?, arg2: B?, block: (A, B) -> R): R? =
    if (arg1 != null && arg2 != null) block(arg1, arg2) else null

inline fun <A, B, C, R> withNotNull(arg1: A?, arg2: B?, arg3: C?, block: (A, B, C) -> R): R? =
    if (arg1 != null && arg2 != null && arg3 != null) block(arg1, arg2, arg3) else null

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

    val notGrantedPermissions =
        permissions.filter { context?.checkSelfPermission(it) != android.content.pm.PackageManager.PERMISSION_GRANTED }
    if (notGrantedPermissions.isEmpty()) return true

    if (!shouldAsk) return false

    val shouldShowRequestPermissionRationale = notGrantedPermissions.filter { shouldShowRequestPermissionRationale(it) }
    if (shouldShowRequestPermissionRationale.isNotEmpty()) {
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

fun Context.convertDpToPixels(dp: Int): Int {
    val resources = resources
    val metrics = resources.displayMetrics
    return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}

/**
 * Calculate ActionBar height
 */
fun Context.getActionBarHeightPx(): Int? {
    val tv = TypedValue()
    if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        return TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
    }
    return null
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


fun Context.bitmapDescriptorFromVector(@DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(this, vectorDrawableResourceId)!!.apply {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    }
    val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
