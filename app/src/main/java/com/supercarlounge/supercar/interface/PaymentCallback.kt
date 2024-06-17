package com.supercarlounge.supercar.`interface`

import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.SkuDetails

interface PaymentCallback {
    fun SetItemList(list:List<ProductDetails>)
}