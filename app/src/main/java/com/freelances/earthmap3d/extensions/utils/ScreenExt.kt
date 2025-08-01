package com.freelances.earthmap3d.extensions.utils

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun EditText.onTextChange(callBack:(String?)->Unit){
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int,count: Int,after: Int) = Unit
        override fun afterTextChanged(s: Editable?) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            callBack(s.toString())

        }

    })
}
fun Context.dpToPx(number: Number): Float {
    return number.toFloat() * (this.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Calendar.getTimeFormattedString(simpleDateFormatString: String): String {
    val format = SimpleDateFormat(simpleDateFormatString, Locale.getDefault())

    return format.format(time)
}

fun Context.pxToDp(number: Number): Float {
    return number.toFloat() / (this.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(messageRes: Int) {
    Toast.makeText(this, this.getString(messageRes), Toast.LENGTH_SHORT).show()
}

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}
