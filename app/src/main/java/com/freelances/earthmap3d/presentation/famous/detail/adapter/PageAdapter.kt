package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PageAdapter(
    activity: FragmentActivity,
    private val listFragment: List<Fragment>,
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = listFragment.size

    override fun createFragment(position: Int): Fragment {
        return listFragment[position]
    }
}