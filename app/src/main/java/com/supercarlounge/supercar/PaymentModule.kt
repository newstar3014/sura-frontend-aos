package com.supercarlounge.supercar

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import com.supercarlounge.supercar.*
import com.supercarlounge.supercar.`interface`.PaymentCallback
import java.util.Arrays


val SERVICE_TIMEOUT = -3
val SERVICE_DISCONNECTED = -1
val OK = 0
val USER_CANCELED = 1
val FEATURE_NOT_SUPPORTED = 1
val SERVICE_UNAVAILABLE = 2
val BILLING_UNAVAILABLE = 3
val ITEM_UNAVAILABLE = 4
val DEVELOPER_ERROR = 5
val ERROR = 6
val ITEM_ALREADY_OWNED = 7
val ITEM_NOT_OWNED = 8
val productList: ArrayList<QueryProductDetailsParams.Product> = ArrayList<QueryProductDetailsParams.Product>()
class PaymentModule(val context: Context, var listener: PurchasesUpdatedListener,val paymentCallback: PaymentCallback) : PurchasesResponseListener, BillingClientStateListener {
    var application = context.applicationContext as MainApplication
    var billingClient: BillingClient = BillingClient.newBuilder(context)
            .setListener(listener)
            .enablePendingPurchases()
            .build()
    var productDetailsList: List<ProductDetails> = mutableListOf()

    fun initpay() {
        val skuList = listOf<String>("sura_goods_hart_143", "sura_goods_hart_66", "sura_goods_hart_31","sura_goods_hart_17","sura_goods_hart_2700","sura_goods_hart_1470","sura_goods_hart_614")
        for (i in skuList){
            productList.add(QueryProductDetailsParams.Product.newBuilder()
                .setProductId(i)
                .setProductType(BillingClient.ProductType.INAPP)
                .build())
        }
        billingClient.startConnection(this)
    }
    fun querySkuDetails() {
        val params = QueryProductDetailsParams.newBuilder().setProductList(productList).build()
        Log.d("params", params.toString())
        billingClient.queryProductDetailsAsync(params) { p0, p1 ->
            BillingClient.BillingResponseCode.OK
            productDetailsList = p1
            Log.d("결제아이템 리스트1", productDetailsList.toString())
            Log.d("결제아이템 타입", p0.responseCode.toString())
            paymentCallback.SetItemList(productDetailsList)
        }
    }



    fun getskudata(code: String): ProductDetails? {
        for (i in productDetailsList) {
            Log.d("아이템", i.productId.toString())
            if (i.productId == code) {

                return i
            }
        }
        return null
    }

    /**
     * 구매 여부 체크, 소비성 구매가 아닌 항목에 한정.
     * @param sku
     */
    fun checkPurchased(
            sku: String,
            resultBlock: (purchased: Boolean) -> Unit,
    ) {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        ) { billingResult, purchaseList ->
            for (purchase in purchaseList) {
                if (purchase.skus.get(0) == sku && purchase.isPurchaseConfirmed()) {

                }
            }
        }
    }


    // 구매 확인 검사 Extension
    private fun Purchase.isPurchaseConfirmed(): Boolean {
        return this.isAcknowledged && this.purchaseState == Purchase.PurchaseState.PURCHASED
    }

    fun aknow(token: String) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(token)
                .build()
        billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult: BillingResult? ->
            var code = billingResult!!.responseCode
            statuspayment(code)
        }
    }
    @SuppressLint("SuspiciousIndentation")
    fun settingrecume(listener: PurchaseHistoryResponseListener){
        if( application!!.billingClient!=null)
        application!!.billingClient!!.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP,listener)
    }

    fun statuspayment(code: Int) {
        if (code == OK) {
            Log.d("결제상태", "구매완료")
        } else {
            when (code) {
                USER_CANCELED -> {
                    Log.d("결제상태", "유저취소")
                }
                ITEM_ALREADY_OWNED -> {
                    Log.d("결제상태", "이미가지고있음")
                }
                FEATURE_NOT_SUPPORTED -> {
                    Log.d("결제상태", "지원하지않는 기능")
                }
                ITEM_NOT_OWNED -> {
                    Log.d("결제상태", "소유하지않는 기능")
                }
                ITEM_UNAVAILABLE -> {
                    Log.d("결제상태", "구매불가능한 상품")
                }
                ERROR -> {
                    Log.d("결제상태", "에러")
                }
                SERVICE_DISCONNECTED -> {
                    Log.d("결제상태", "서비스 연견되어있지않음")
                }
                SERVICE_TIMEOUT -> {
                    Log.d("결제상태", "시간초과")
                }
                else -> {}
            }

        }
    }

    override fun onQueryPurchasesResponse(p0: BillingResult, p1: MutableList<Purchase>) {
        var code = p0.responseCode
        if (code == OK) {
            if (p1.size != 0) {
                var data = p1.get(0)
                Log.d("결제상태", "결제있음")
            } else {
                Log.d("결제상태", "결제없음")
            }
        } else {
            when (code) {
                USER_CANCELED -> {
                    Log.d("결제상태", "유저취소")
                }
                ITEM_ALREADY_OWNED -> {
                    Log.d("결제상태", "이미가지고있음")
                }
                FEATURE_NOT_SUPPORTED -> {
                    Log.d("결제상태", "지원하지않는 기능")
                }
                ITEM_NOT_OWNED -> {
                    Log.d("결제상태", "소유하지않는 기능")
                }
                ITEM_UNAVAILABLE -> {
                    Log.d("결제상태", "구매불가능한 상품")
                }
                ERROR -> {
                    Log.d("결제상태", "에러")
                }
                SERVICE_DISCONNECTED -> {
                    Log.d("결제상태", "서비스 연견되어있지않음")
                }
                SERVICE_TIMEOUT -> {
                    Log.d("결제상태", "시간초과")
                }
                else -> {}
            }
        }
    }

    override fun onBillingSetupFinished(p0: BillingResult) {
        if (p0.responseCode == BillingClient.BillingResponseCode.OK) {
            Log.d("MYAPP", "onBillingSetupFinished: ")
            if (productDetailsList.isEmpty()) {
                querySkuDetails()

            }

        }
    }

    override fun onBillingServiceDisconnected() {
        Log.d("결제상태", "결제연결해제")
    }
}