package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.setting

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.shareApp
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.dialog.RatingAppPopup
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.language.LanguageSettingActivity
import com.earthmap.map.ltv.tracker.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    private val ratingAppPopup by lazy { RatingAppPopup() }

    override fun updateUI(savedInstanceState: Bundle?) {
        binding.apply {
            lnRate.isVisible = !preferenceHelper.isRated

            ivBack.safeClick {
                finish()
            }

            lnLanguage.safeClick {
                navigateToLanguageScreen()
            }


            lnShareApp.safeClick {
                shareApp()
            }

            lnRate.safeClick {
                if (!ratingAppPopup.isAdded) {
                    ratingAppPopup.apply {
                        setFinishRate {
                            preferenceHelper.isRated = true
                            lnRate.isVisible = !preferenceHelper.isRated
                        }
                    }.show(supportFragmentManager, RatingAppPopup::class.java.simpleName)
                }
            }
        }
    }

    private fun navigateToLanguageScreen() {
        navigateTo(
            LanguageSettingActivity::class.java
        )
    }
}