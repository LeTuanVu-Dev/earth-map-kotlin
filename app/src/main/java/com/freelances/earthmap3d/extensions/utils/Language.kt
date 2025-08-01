package com.freelances.earthmap3d.extensions.utils

import android.content.Context
import android.content.res.Configuration
import com.freelances.earthmap3d.R
import com.freelances.earthmap3d.extensions.PreferenceHelper
import com.freelances.earthmap3d.models.LanguageItem
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Locale

object Language : KoinComponent {
    private val preferenceHelper: PreferenceHelper by inject()

    val listLanguage = listOf(
        LanguageItem(flagId = R.drawable.ic_flag_uk, name = "English", code = "en"),
        LanguageItem(flagId = R.drawable.ic_flag_vietnam, name = "Tiếng Việt", code = "vi"),
    )

    fun changeLanguage(context: Context, language: String): Context {
        if (language.equals("", ignoreCase = true)) return context
        var myLocale = Locale(language)
        if (language.contains("_")) {
            myLocale =
                Locale(
                    language.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0],
                    language.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1],
                )
        }
        preferenceHelper.languageSelected = language
        Locale.setDefault(myLocale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(myLocale)
        configuration.setLayoutDirection(myLocale)
        return context.createConfigurationContext(configuration)
    }

    fun createContextLocale(context: Context): Context {
        val configuration = Configuration(context.resources.configuration)
        val selectedLanguage = preferenceHelper.languageSelected
        val locale = if (selectedLanguage.contains("_")) {
            Locale(selectedLanguage.split("_".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0],
                selectedLanguage.split("_".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1])
        } else {
            Locale(selectedLanguage)
        }
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }
}
