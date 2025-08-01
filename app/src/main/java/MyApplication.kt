package com.freelances.earthmap3d
import android.app.Application
import com.freelances.earthmap3d.di.appModule
import com.freelances.earthmap3d.di.roomModule
import com.freelances.earthmap3d.di.viewModelModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                listOf(
                    appModule,
                    viewModelModule,
                    roomModule
                )
            )
        }
        FirebaseApp.initializeApp(this)
    }
}
