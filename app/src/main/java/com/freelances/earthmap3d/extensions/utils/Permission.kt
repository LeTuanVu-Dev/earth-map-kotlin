package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat

fun Context.checkPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.arePermissionsGranted(permissions: List<String>): Boolean {
    for (permission in permissions) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
    }
    return true
}

@SuppressLint("InlinedApi")
const val postNotification = Manifest.permission.POST_NOTIFICATIONS

val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)
} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
} else {
    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
}


const val cameraPermission = Manifest.permission.CAMERA
const val recordPermission = Manifest.permission.RECORD_AUDIO
const val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

fun Context.arePermissionsGranted(permissions: Array<String>): Boolean {
    return permissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
}


fun Context.isGrantCameraPermission(): Boolean {
    return checkPermissionGranted(cameraPermission)
}

fun Context.isGrantRecordPermission(): Boolean {
    return checkPermissionGranted(recordPermission)
}

fun Context.requestPermissionSetting() {
    runCatching {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri =
            Uri.fromParts("package", packageName, null)
        intent.setData(uri)
        startActivity(intent)
    }
}

fun Context.isGrantedPostNotification(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        checkPermissionGranted(postNotification)
    } else true
}

fun Context.checkLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}

