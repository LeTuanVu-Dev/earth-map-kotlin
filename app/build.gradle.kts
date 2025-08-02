import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.gmsGoogleServices)
    alias(libs.plugins.firebaseCrashlytics)
    alias(libs.plugins.map.secret)
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    namespace = "com.earthmap.map.ltv.tracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.earthmap.map.ltv.tracker"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val formatDate = SimpleDateFormat("MMM.dd.yyyy").format(Date())
        base.archivesName = "Earth-Map-3d-v${versionName}($versionCode)_$formatDate"
    }

    bundle {
        language {
            enableSplit = false
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            //signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
           // signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    flavorDimensions.add("version")
    productFlavors {
        create("dev") {
            applicationId = "com.earthmap.map.ltv.tracker"
            manifestPlaceholders["map_id"] = "AIzaSyC6LQVdLBoRya8PHhM3EJiGIdQswuGwUFA"
            buildConfigField("boolean", "build_debug", "true")
        }

        create("product") {
            applicationId = "com.earthmap.map.ltv.tracker"
            manifestPlaceholders["map_id"] = "AIzaSyC6LQVdLBoRya8PHhM3EJiGIdQswuGwUFA"
            buildConfigField("boolean", "build_debug", "false")
        }
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.multidex)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)
    implementation(libs.glide)
    implementation(libs.gson)
    implementation(libs.koin.android)
    implementation(libs.koin.android.compat)
    implementation(libs.koin.core)
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //3d earth
    implementation (libs.worldwind)
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    //map
    implementation (libs.play.services.maps)
    implementation (libs.play.services.location)

//    implementation (libs.places)
    implementation (libs.panoramagl)
    implementation (libs.core)

    // retrofit
    implementation (libs.retrofit)
    // GSON
    implementation (libs.converter.gson)

    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    implementation(libs.review.ktx)

}