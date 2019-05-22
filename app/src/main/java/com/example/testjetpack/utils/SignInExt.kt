package com.example.testjetpack.utils

import android.text.TextUtils


fun CharSequence?.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

/**
 * check password chars for:
 * should contain at least one digit
 * should contain at least one special char
 * should contain at least one lower case
 * should contain at least one upper case
 * should contain at least 8 from the mentioned characters
 **/
fun CharSequence?.isValidPassword(): Boolean {
    return if (this != null) Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$").find(this) != null
    else false
}