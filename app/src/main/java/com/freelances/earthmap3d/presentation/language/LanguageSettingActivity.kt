package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.language

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.Language.listLanguage
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.visible
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.LanguageItem
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.main.MainActivity
import com.earthmap.map.ltv.tracker.databinding.ActivityLanguageBinding

open class LanguageSettingActivity :
    BaseActivity<ActivityLanguageBinding>() {
    companion object {
        var scrollOffsetY: Int = 0
    }

    private val languageAdapter: LfoAdapter by lazy {
        LfoAdapter()
    }

    open val listData: List<LanguageItem> = listLanguage

    override fun updateUI(savedInstanceState: Bundle?) {
        binding.ivBack.visible()
        binding.btnDone.visible()
        setUpAdapter()
        binding.ivBack.safeClick {
            finish()
        }
        binding.btnDone.safeClick {
            navigateToNextScreen()
        }
    }


    private fun navigateToNextScreen() {
        preferenceHelper.languageSelected = languageAdapter.getLanguageSelected()?.code.toString()
        navigateThenClearTask(MainActivity::class.java)
    }

    private fun setUpAdapter() {
        binding.rcvLanguage.apply {
            adapter = languageAdapter
            layoutManager = LinearLayoutManager(this@LanguageSettingActivity).also {
                it.scrollToPositionWithOffset(0, -1 * scrollOffsetY)
            }
            languageAdapter.setOnItemSelected { item ->
                languageAdapter.selectedItem(item)
                binding.btnDone.visible()
            }
            languageAdapter.submitList(listData)
            languageAdapter.selectedLanguage(preferenceHelper.languageSelected)
        }
    }


    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityLanguageBinding {
        return ActivityLanguageBinding.inflate(layoutInflater)
    }

}