package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.language

import android.content.res.Resources
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.Language
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.Language.listLanguage
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.visible
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.LanguageItem
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.main.MainActivity
import com.earthmap.map.ltv.tracker.databinding.ActivityLanguageBinding

class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {
    private val languageAdapter by lazy { LfoAdapter() }

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityLanguageBinding {
        return ActivityLanguageBinding.inflate(layoutInflater)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        binding.btnDone.safeClick {
            languageAdapter.getLanguageSelected()?.let {
                navigateToNextScreen(it)
            }
        }
        setupListLanguage()
    }

    private fun setupListLanguage() {
        val listLfo = getListLanguageLfo()
        languageAdapter.submitList(listLfo)
        binding.rcvLanguage.adapter = languageAdapter
        languageAdapter.setOnItemSelected { item ->
            languageAdapter.selectedItem(item)
            binding.btnDone.visible()
        }
    }

    private fun getListLanguageLfo(): List<LanguageItem> {
        val deviceLanguage = Resources.getSystem().configuration.locales[0].language
        val indexLanguageDevice = listLanguage.indexOfFirst { it.code == deviceLanguage }
        val listLfo = if (indexLanguageDevice != -1) {
            listLanguage[indexLanguageDevice].isDefault = true
            listLanguage
        } else {
            listLanguage[0].isDefault = true
            listLanguage
        }
        return listLfo
    }

    private fun navigateToNextScreen(language: LanguageItem) {
        Language.changeLanguage(this, language.code)
        navigateTo(MainActivity::class.java, isFinish = true)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishAffinity()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }
}
