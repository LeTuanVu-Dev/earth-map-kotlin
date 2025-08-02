package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.earthmap.map.ltv.tracker.R
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

@SuppressLint("StringFormatInvalid")
fun Context.shareApp() {
    try {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            getString(
                R.string.check_out_my_app_at,
                "https://play.google.com/store/apps/details?id=${packageName}"
            )
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    } catch (e: Exception) {
        e.toString()
    }
}

fun Context.rateApp() {
    val applicationID = this.packageName
    val playStoreUri = Uri.parse("market://details?id=$applicationID")

    val rateIntent = Intent(Intent.ACTION_VIEW, playStoreUri)
    rateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)

    try {
        this.startActivity(rateIntent)
    } catch (e: ActivityNotFoundException) {
        val webPlayStoreUri =
            Uri.parse("https://play.google.com/store/apps/details?id=$applicationID")
        val webRateIntent = Intent(Intent.ACTION_VIEW, webPlayStoreUri)
        this.startActivity(webRateIntent)
    }
}

fun Context.composeEmail(recipient: String, subject: String) {
    val emailIntent = Intent(Intent.ACTION_SENDTO)
    emailIntent.data = Uri.parse("mailto:")
    emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)

    try {
        this.startActivity(Intent.createChooser(emailIntent, "Send Email"))
    } catch (e: ActivityNotFoundException) {
        // Handle case where no email app is available
    }
}

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}
