package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

fun Context.getIntentSettingsPermission(): Intent {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts(
        "package",
        packageName, null
    )
    intent.setData(uri)
    return intent
}

