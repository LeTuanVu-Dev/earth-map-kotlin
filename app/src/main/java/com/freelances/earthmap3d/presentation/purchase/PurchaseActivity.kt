package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.presentation.purchase

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.ProductDetails
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.base.BaseActivity
import com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils.safeClick
import com.earthmap.map.ltv.tracker.databinding.ActivityPurchaseBinding
import com.freelances.earthmap3d.presentation.purchase.AppPurchase
import com.freelances.earthmap3d.presentation.purchase.PurchaseListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PurchaseActivity : BaseActivity<ActivityPurchaseBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityPurchaseBinding {
        return ActivityPurchaseBinding.inflate(layoutInflater)
    }

    private val premiumAdapter by lazy {
        PremiumAdapter {
            handlePurchase(it)
        }
    }

    private val mListDetails = ArrayList<ProductDetails>()

    override fun updateUI(savedInstanceState: Bundle?) {
        binding.rvCoin.adapter = premiumAdapter
        binding.tvCoin.text = preferenceHelper.coinNumber.toString()
        binding.ivBack.safeClick { finish() }
        setPurchaseListener()
        initPurchase()
    }

    private fun setPurchaseListener() {
        AppPurchase.getInstance(this).setPurchaseListener(object : PurchaseListener {
            override fun onProductPurchased(productId: String, transactionDetails: String) {
                setValuePurchase(productId)
            }

            override fun displayErrorMessage(errorMsg: String) {}

            override fun onUserCancelBilling() {}
            override fun onUserPurchaseConsumable() {}
        })
    }

    private fun setValuePurchase(productId: String) {
        when (productId) {
            "iap.coin.subs1" -> preferenceHelper.coinNumber += 50
            "iap.coin.subs2" -> preferenceHelper.coinNumber += 100
            "iap.coin.subs3" -> preferenceHelper.coinNumber += 200
            "iap.coin.subs4" -> preferenceHelper.coinNumber += 300
            "iap.coin.subs5" -> preferenceHelper.coinNumber += 500
            "iap.coin.subs6" -> preferenceHelper.coinNumber += 1000
        }
        binding.tvCoin.text = preferenceHelper.coinNumber.toString()
    }

    private fun initPurchase() {
        try {
            lifecycleScope.launch(Dispatchers.IO) {
                mListDetails.clear()
                // Sắp xếp theo giá tăng dần
                val skuList = AppPurchase.getInstance(this@PurchaseActivity).skuListINAPFromStore
                val sortedList = skuList.sortedBy {
                    it.oneTimePurchaseOfferDetails?.priceAmountMicros ?: Long.MAX_VALUE
                }
                mListDetails.addAll(sortedList)
                Log.e("VuLT", "initPurchase: skuListSubsFromStore = $mListDetails")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handlePurchase(product: ProductDetails) {
        Log.d("VuLT", "handlePurchase: $product")
        AppPurchase.getInstance(this).purchase(this, product)
    }
}