package com.freelances.earthmap3d.presentation.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.freelances.earthmap3d.base.BaseActivity
import com.freelances.earthmap3d.databinding.ActivitySplashBinding
import com.freelances.earthmap3d.presentation.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private var isNextAction = false

    companion object {
        const val TIME_DELAY = 3_000L
    }

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun isDisplayCutout(): Boolean = true

    override fun updateUI(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            delay(TIME_DELAY)
            navigateToNextScreen()
        }
    }

    private fun navigateToNextScreen() {
        if (isNextAction) return
        isNextAction = true
        navigateTo(MainActivity::class.java, isFinish = true)
    }

    override fun onDestroy() {
        isNextAction = false
        super.onDestroy()
    }
}
