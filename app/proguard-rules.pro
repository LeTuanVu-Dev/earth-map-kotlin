# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
########################################
# Firebase
########################################
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
-keepattributes Signature
-keepattributes *Annotation*

########################################
# Glide
########################################
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep class com.bumptech.glide.** { *; }
-dontwarn com.bumptech.glide.**

########################################
# Gson
########################################
-keep class com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

########################################
# Retrofit + Gson Converter
########################################
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

########################################
# Koin (Dependency Injection)
########################################
-dontwarn org.koin.**
-keep class org.koin.** { *; }

########################################
# Lottie
########################################
-keep class com.airbnb.lottie.** { *; }
-dontwarn com.airbnb.lottie.**

########################################
# Facebook Mediation (AdMob)
########################################
-dontwarn com.facebook.**
-keep class com.facebook.** { *; }

########################################
# Mintegral Mediation
########################################
-dontwarn com.mintegral.**
-keep class com.mintegral.** { *; }

########################################
# Pangle Mediation
########################################
-dontwarn com.bytedance.**
-keep class com.bytedance.** { *; }

########################################
# Vungle Mediation
########################################
-dontwarn com.vungle.**
-keep class com.vungle.** { *; }

########################################
# AndroidX (Lifecycle, Navigation, etc.)
########################################
-keep class androidx.lifecycle.** { *; }
-keep class androidx.navigation.** { *; }

########################################
# Google Maps & Location
########################################
-dontwarn com.google.android.gms.maps.**
-keep class com.google.android.gms.maps.** { *; }

-dontwarn com.google.android.gms.location.**
-keep class com.google.android.gms.location.** { *; }

########################################
# WorldWind 3D Earth
########################################
-dontwarn gov.nasa.**
-keep class gov.nasa.** { *; }

########################################
# PanoramaGL
########################################
-keep class com.panoramagl.** { *; }
-dontwarn com.panoramagl.**

########################################
# Room (AndroidX)
########################################
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**
-keepclassmembers class * {
    @androidx.room.* <methods>;
}

########################################
# Google Play Billing
########################################
-keep class com.android.billingclient.** { *; }
-dontwarn com.android.billingclient.**

########################################
# Google Play Core (in-app updates & reviews)
########################################
-dontwarn com.google.android.play.core.**
-keep class com.google.android.play.core.** { *; }

########################################
# AzModuleAds & Shimmer
########################################
-keep class com.facebook.shimmer.** { *; }
-dontwarn com.facebook.shimmer.**

# Nếu AzModuleAds là lib bạn tự viết hoặc bên thứ 3 không phổ biến
-keep class **.azmoduleads.** { *; }
-dontwarn **.azmoduleads.**

# Giữ Parcelable
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

# Giữ tất cả model/data class
-keep class com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.** { *; }
-keep class com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.db.** { *; }
-keep class com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.** { *; }

# Giữ lại tất cả constructor của các class có annotation của Koin
-keepclassmembers class * {
    @org.koin.core.annotation.* *;
}

# Nếu bạn dùng ViewModel với Koin
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}
-keep class * extends androidx.lifecycle.ViewModel

# Giữ lại metadata cho Kotlin reflection (đôi khi Koin cần để resolve deps)
-keep class kotlin.Metadata { *; }
