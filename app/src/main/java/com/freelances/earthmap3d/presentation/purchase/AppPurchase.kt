package com.freelances.earthmap3d.presentation.purchase

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.IntDef
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams

/**
 * Kotlin conversion of Java AppPurchase class
 */
class AppPurchase(private val mContext: Context) {

    companion object {
        private const val TAG = "PurchaseEG"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: AppPurchase? = null

        /**
         * Singleton access
         */
        fun getInstance(context: Context): AppPurchase =
            instance ?: synchronized(this) {
                instance ?: AppPurchase(context.applicationContext).also { instance = it }
            }

        /**
         * Remove IAP version flag
         */
        var REMOVE_IAP_VERSION: Boolean = false

        // Product IDs
        const val IAP_COIN_SUBS_1 = "iap.coin.subs1"
        const val IAP_COIN_SUBS_2 = "iap.coin.subs2"
        const val IAP_COIN_SUBS_3 = "iap.coin.subs3"
        const val IAP_COIN_SUBS_4 = "iap.coin.subs4"
        const val IAP_COIN_SUBS_5 = "iap.coin.subs5"
        const val IAP_COIN_SUBS_6 = "iap.coin.subs6"
    }

    /**
     * Allow repeated purchase flows
     */
    var IAP_BUY_REPEAT: Boolean = false

    private val skuDetailsINAPMap = mutableMapOf<String, ProductDetails>()
    private lateinit var listINAPId: List<String>
    var listIdReStore = mutableListOf<String>()
    private var purchaseListener: PurchaseListener? = null
    private var isInitBillingFinish: Boolean = false
    private lateinit var billingClient: BillingClient
    val skuListINAPFromStore = mutableListOf<ProductDetails>()

    private var verified = false
    private var verifiedINAP = false
    private var verifiedSUBS = false
    private var isPurchase = false
    private var latestPurchase: Purchase? = null

    /**
     * Listener for purchase updates
     */
    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        Log.e(TAG, "onPurchasesUpdated code: ${billingResult.responseCode}")
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> purchases?.forEach { purchase ->
                latestPurchase = purchase
                if (IAP_BUY_REPEAT) {
                    consumePurchaseIAP(purchase)
                    Log.d(TAG, "IAP_BUY_REPEAT:1")
                } else {
                    Log.d(TAG, "IAP_BUY_REPEAT:0")
                    handlePurchase(purchase)
                    acknowledgePurchase(purchase)
                }
            }

            BillingClient.BillingResponseCode.USER_CANCELED -> {
                purchaseListener?.onUserCancelBilling()
                Log.d(TAG, "onPurchasesUpdated: USER_CANCELED")
            }

            else -> Log.d(TAG, "onPurchasesUpdated: ...")
        }
    }

    /**
     * Initialize BillingClient
     */
    fun initBilling(application: Application) {
        listINAPId = listOf(
            IAP_COIN_SUBS_1, IAP_COIN_SUBS_2, IAP_COIN_SUBS_3,
            IAP_COIN_SUBS_4, IAP_COIN_SUBS_5, IAP_COIN_SUBS_6
        )

        billingClient = BillingClient.newBuilder(application)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {}

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (!isInitBillingFinish) {
                    verifyPurchased()
                }
                isInitBillingFinish = true
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryINAPP()
                }
            }
        })
    }

    /** Handle a successful purchase */
    private fun handlePurchase(purchase: Purchase) {
        purchaseListener?.onProductPurchased(purchase.orderId, purchase.originalJson)
        if (REMOVE_IAP_VERSION) {
            consumePurchase(purchase)
        } else if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            isPurchase = true
        }
    }

    /** Query in-app products */
    private fun queryINAPP() {
        val productList = listINAPId.map { sku ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(sku)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { _, detailsList ->
            skuListINAPFromStore.apply {
                clear()
                addAll(detailsList)
            }
            addSkuINAPPToMap(detailsList)
        }
    }

    private fun addSkuINAPPToMap(skuList: List<ProductDetails>) {
        skuList.forEach { skuDetailsINAPMap[it.productId] = it }
    }

    /** Verify previous purchases (INAPP) */
    fun verifyPurchased() {
        verified = false
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { billingResult, purchases ->
            purchases.forEach { acknowledgePurchase(it) }
            (mContext as? Activity)?.runOnUiThread {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    if (!verifiedSUBS || !verified) isPurchase = false
                    if (REMOVE_IAP_VERSION) purchases.forEach { consumePurchase(it) }
                    else purchases.forEach { purchase ->
                        listINAPId.forEach { id ->
                            if (purchase.products.contains(id)) {
                                isPurchase = true
                                if (!verified) {
                                    verified = true
                                    verifiedINAP = true
                                    return@forEach
                                }
                            }
                        }
                    }
                }
                verifiedINAP = true
            }
        }
    }

    /** Verify and run callback on purchase found */
    fun verifyPurchased(runnable: Runnable) {
        verified = false
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { billingResult, purchases ->
            purchases.forEach { acknowledgePurchase(it) }
            (mContext as? Activity)?.runOnUiThread {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    if (!verifiedSUBS || !verified) isPurchase = false
                    if (REMOVE_IAP_VERSION) purchases.forEach { consumePurchase(it) }
                    else purchases.forEach { purchase ->
                        listINAPId.forEach { id ->
                            if (purchase.products.contains(id)) {
                                isPurchase = true
                                listIdReStore.add(id)
                                runnable.run()
                                if (!verified) {
                                    verified = true
                                    verifiedINAP = true
                                    return@forEach
                                }
                            }
                        }
                    }
                }
                verifiedINAP = true
            }
        }
    }

    /** Launch purchase flow */
    fun purchase(activity: Activity, productDetails: ProductDetails) {
        purchaseInApp(activity, productDetails)
    }

    fun purchaseInApp(activity: Activity, productDetails: ProductDetails): String {
        purchaseListener?.displayErrorMessage("Billing error init")
        return ""
        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .build()
                )
            )
            .build()

        val result = billingClient.launchBillingFlow(activity, flowParams)
        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
            latestPurchase?.let { consumePurchaseIAP(it) }
            return "Subscribed Successfully"
        }
        return ""
    }

    /** Launch subscription flow */
    fun subscribe(activity: Activity, productDetails: ProductDetails): String {
        val offerToken =
            productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken.orEmpty()
        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .setOfferToken(offerToken)
                        .build()
                )
            )
            .build()

        return when (val code =
            billingClient.launchBillingFlow(activity, flowParams).responseCode) {
            BillingClient.BillingResponseCode.OK -> "Subscribed Successfully"
            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> "Billing not supported"
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> "Item already owned"
            BillingClient.BillingResponseCode.ITEM_UNAVAILABLE -> "Item not available"
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> "Service disconnected"
            BillingClient.BillingResponseCode.SERVICE_TIMEOUT -> "Timeout"
            BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE -> "Network error"
            BillingClient.BillingResponseCode.USER_CANCELED -> "Request Canceled"
            else -> "Error completing request"
        }
    }

    /** Consume a consumable purchase for repeat buys */
    fun consumePurchaseIAP(pc: Purchase) {
        val params = ConsumeParams.newBuilder()
            .setPurchaseToken(pc.purchaseToken)
            .build()
        billingClient.consumeAsync(params) { result, _ ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                purchaseListener?.onUserPurchaseConsumable()
            }
        }
    }

    /** Consume non-consumable, then re-verify */
    fun consumePurchase(pc: Purchase) {
        val params = ConsumeParams.newBuilder()
            .setPurchaseToken(pc.purchaseToken)
            .build()
        billingClient.consumeAsync(params) { result, _ ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                verifyPurchased()
            }
        }
    }

    /** Utility to format prices */
    fun getPrice(productDetails: ProductDetails?): String = when {
        productDetails == null -> ""
        productDetails.productType == BillingClient.ProductType.SUBS ->
            productDetails.subscriptionOfferDetails?.firstOrNull()
                ?.pricingPhases?.pricingPhaseList?.firstOrNull()
                ?.formattedPrice.orEmpty()

        productDetails.oneTimePurchaseOfferDetails != null ->
            productDetails.oneTimePurchaseOfferDetails?.formattedPrice.orEmpty()

        else -> ""
    }

    /** Acknowledge a purchase if not yet done */
    private fun acknowledgePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
            val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            billingClient.acknowledgePurchase(params) { result ->
                Log.e(TAG, "acknowledgePurchase: ${result.responseCode}")
            }
        }
    }

    /** IntDef for IAP types */
    @IntDef(TYPE_IAP.PURCHASE, TYPE_IAP.SUBSCRIPTION)
    @Retention(AnnotationRetention.SOURCE)
    annotation class TYPE_IAP {
        companion object {
            const val PURCHASE = 1
            const val SUBSCRIPTION = 2
        }
    }

    fun setPurchaseListener(listener: PurchaseListener?) {
        purchaseListener = listener
    }

    fun isPurchased(): Boolean = isPurchase
    fun setIsPurchased(purchased: Boolean) {
        isPurchase = purchased
    }
}
