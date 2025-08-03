package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.ARG_DATA
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.click
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.parcelable
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.models.ModelFamousPlace
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.FamousDetailViewModel
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail.adapter.PageAdapter
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail.fragment.InfoFragment
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail.fragment.TravelFragment
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.famous.detail.fragment.WeatherFragment
import com.earthmap.map.ltv.tracker.databinding.ActivityFamousDetailBinding
import com.earthmap.map.ltv.tracker.databinding.LayoutExtensionsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FamousDetailActivity : BaseActivity<ActivityFamousDetailBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityFamousDetailBinding {
        return ActivityFamousDetailBinding.inflate(layoutInflater)
    }

    private val famousDetailViewModel: FamousDetailViewModel by viewModel()

    private val itemDataPreview by lazy {
        runCatching {
            intent.extras?.parcelable<ModelFamousPlace>(ARG_DATA)
        }.getOrNull()
    }
    private val listFragment by lazy {
        mutableListOf<Fragment>(
            InfoFragment.newInstance(),
            WeatherFragment.newInstance(),
            TravelFragment.newInstance(),
        )
    }

    private val adapter by lazy {
        PageAdapter(this, listFragment)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        initData()
        binding.apply {
            ivBack.safeClick {
                finish()
            }

            ivExt.click {
                showPopupMore(it)
            }
        }
    }
    private fun initData() {
        itemDataPreview?.let { item ->
            binding.tvTitle.text = item.name
            famousDetailViewModel.setItemCurrent(item)
            famousDetailViewModel.fetchIsoCode(item.latitude , item.longitude,item.name)
        }

        setupViewPager()
    }


    private fun setupViewPager() {
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.setCurrentItem(0, false)
        binding.viewPager.isUserInputEnabled = false
    }

    private fun showPopupMore(view: View) {
        val layoutInflater = LayoutInflater.from(this)
        val binding1 = LayoutExtensionsBinding.inflate(layoutInflater)
        val popupMenu = PopupWindow(this)
        popupMenu.contentView = binding1.root
        popupMenu.width = LinearLayout.LayoutParams.WRAP_CONTENT
        popupMenu.height = LinearLayout.LayoutParams.WRAP_CONTENT
        popupMenu.isFocusable = true
        popupMenu.isOutsideTouchable = true
        popupMenu.elevation = 100f

        popupMenu.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Đo kích thước của PopupWindow để tính toán chiều cao cần thiết
        binding1.root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val popupHeight = binding1.root.measuredHeight

        // Lấy vị trí của view gốc trên màn hình
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val yPos = location[1] + view.height // Vị trí y của view gốc
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        // Kiểm tra nếu không gian phía dưới không đủ để hiển thị toàn bộ PopupWindow
        if (yPos + popupHeight > screenHeight) {
            // Hiển thị PopupWindow phía trên view gốc nếu không đủ không gian bên dưới
            popupMenu.showAsDropDown(view, -350, -(popupHeight + view.height), Gravity.NO_GRAVITY)
        } else {
            // Hiển thị PopupWindow phía dưới view gốc nếu đủ không gian
            popupMenu.showAsDropDown(view, -350, 30, Gravity.NO_GRAVITY)
        }

        binding1.lnInfo.safeClick {
            binding.viewPager.setCurrentItem(0, false)
            popupMenu.dismiss()
        }
        binding1.lnWeather.safeClick {
            binding.viewPager.setCurrentItem(1, false)
            popupMenu.dismiss()
        }
        binding1.lnTravels.safeClick {
            binding.viewPager.setCurrentItem(2, false)
            popupMenu.dismiss()
        }

        popupMenu.showAsDropDown(view, -350, 30, Gravity.NO_GRAVITY)
    }

}