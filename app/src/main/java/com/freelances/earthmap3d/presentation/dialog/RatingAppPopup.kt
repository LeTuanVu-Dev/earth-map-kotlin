package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.dialog

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.earthmap.map.ltv.tracker.R
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseDialogFragment
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.CustomRatingBar
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.rateApp
import com.earthmap.map.ltv.tracker.databinding.DialogRateAppBinding
import com.earthmap.map.ltv.tracker.extensions.PreferenceHelper
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RatingAppPopup :
    BaseDialogFragment<DialogRateAppBinding>(), KoinComponent {
    private val sharedPreference: PreferenceHelper by inject()
    private var onFinishRate: () -> Unit = {}
    private var onCancelRate: () -> Unit = {}
    private var isClickRate = false
    override fun isFullHeight(): Boolean {
        return true
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogRateAppBinding {
        return DialogRateAppBinding.inflate(inflater, container, false)
    }

    override fun getLayout(): Int {
        return R.layout.dialog_rate_app
    }

    override fun isFullWidth(): Boolean {
        return true
    }


    private lateinit var reviewManagerInstance: ReviewManager
    private var reviewInfoInstance: ReviewInfo? = null
    var userRate: Int = 0

    private fun requestReview() {
        reviewInfoInstance?.let {
            val flow = reviewManagerInstance.launchReviewFlow(requireActivity(), it)
            flow.addOnCompleteListener { _ ->
                onFinishRate()
                dismissAllowingStateLoss()
            }
        } ?: run {
            requireActivity().rateApp()
            onFinishRate()
            dismissAllowingStateLoss()
        }
        sharedPreference.isRated = true
    }

    fun setCancelRate(onCancel: () -> Unit) = apply {
        this.onCancelRate = onCancel
    }

    fun setFinishRate(onFinish: () -> Unit) = apply {
        this.onFinishRate = onFinish
    }

    @SuppressLint("StringFormatMatches", "StringFormatInvalid")
    override fun updateUI(savedInstanceState: Bundle?) {
        reviewManagerInstance = ReviewManagerFactory.create(baseContext)
        reviewManagerInstance.requestReviewFlow().addOnCompleteListener {
            if (it.isSuccessful)
                reviewInfoInstance = it.result
        }
        binding.apply {
            userRate = 0
            ratingBar.setRatingChangeListener(object : CustomRatingBar.RatingChangeListener {
                override fun onRatingChanged(rating: Int) {
                    userRate = rating

                    when (rating) {
                        0 -> {
                            binding.tvBestRating.isVisible = true
                            binding.arrowUp.isVisible = true
                            binding.rateUsButton.text = baseContext.getString(R.string.rate_us)
                            imvIcRate.setImageResource(R.drawable.rate_0)
                            val appName = baseContext.getString(R.string.app_name)
                        }

                        1 -> {
                            binding.tvBestRating.isVisible = true
                            binding.arrowUp.isVisible = true
                            binding.rateUsButton.text = baseContext.getString(R.string.rate_us)
                            imvIcRate.setImageResource(R.drawable.rate_1)
                            title.text = baseContext.getString(R.string.title_rate_1)
                            youRateUs.text = baseContext.getString(R.string.content_rate_1)
                        }

                        2 -> {
                            binding.tvBestRating.isVisible = true
                            binding.arrowUp.isVisible = true
                            binding.rateUsButton.text = baseContext.getString(R.string.rate_us)
                            imvIcRate.setImageResource(R.drawable.rate_2)
                            title.text = baseContext.getString(R.string.title_rate_2)
                            youRateUs.text = baseContext.getString(R.string.content_rate_2)
                        }

                        3 -> {
                            binding.tvBestRating.isVisible = true
                            binding.arrowUp.isVisible = true
                            binding.rateUsButton.text = baseContext.getString(R.string.rate_us)
                            imvIcRate.setImageResource(R.drawable.rate_3)
                            title.text = baseContext.getString(R.string.title_rate_3)
                            youRateUs.text = baseContext.getString(R.string.content_rate_3)
                        }

                        4 -> {
                            binding.tvBestRating.isVisible = false
                            binding.arrowUp.isVisible = false
                            binding.rateUsButton.text =
                                baseContext.getString(R.string.rate_on_google_play)
                            imvIcRate.setImageResource(R.drawable.rate_4)
                            title.text = baseContext.getString(R.string.title_rate_4)
                            youRateUs.text = baseContext.getString(R.string.content_rate_4)
                        }

                        else -> {
                            binding.tvBestRating.isVisible = false
                            binding.arrowUp.isVisible = false
                            binding.rateUsButton.text =
                                baseContext.getString(R.string.rate_on_google_play)
                            imvIcRate.setImageResource(R.drawable.rate_5)
                            title.text = baseContext.getString(R.string.title_rate_5)
                            youRateUs.text = baseContext.getString(R.string.content_rate_5)
                        }
                    }
                }
            })

            rateUsButton.setOnClickListener {
                isClickRate = true
                requestReview()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (!isClickRate) {
            onCancelRate()
        }
    }


    override fun onClickView() {
        super.onClickView()
        binding.rootView.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }
}
