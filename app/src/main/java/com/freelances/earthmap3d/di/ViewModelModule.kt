package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.di

import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.FamousDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::FamousDetailViewModel)

}
