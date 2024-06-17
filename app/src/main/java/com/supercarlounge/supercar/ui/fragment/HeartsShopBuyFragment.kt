package com.supercarlounge.supercar.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.android.billingclient.api.*
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.PaymentModule
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.`interface`.PaymentCallback
import com.supercarlounge.supercar.databinding.FragmentHeartsShopBuyBinding
import com.supercarlounge.supercar.dialog.DrivePassPayDialog
import com.supercarlounge.supercar.dialog.PrvMiniProfileDialog
import com.supercarlounge.supercar.enumset.DrivePassDialogStatus
import com.supercarlounge.supercar.ui.activity.DrivePassGuideActivity
import com.supercarlounge.supercar.ui.activity.DrivePassVipGuideActivity
import com.supercarlounge.supercar.ui.activity.HeartsShopActivity
import com.supercarlounge.supercar.ui.activity.HeartsShopInfoActivity
import com.supercarlounge.supercar.viewmodel.HeartsShopViewModel


class HeartsShopBuyFragment : Fragment(), PurchasesUpdatedListener {
    var application: MainApplication? = null
    private var _binding: FragmentHeartsShopBuyBinding? = null
    var d = ""
    private lateinit var consumeListenser : ConsumeResponseListener
    private val viewModel: HeartsShopViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )

    // private var viewModel: HeartsShopViewModel? = null
    var paymodule: PaymentModule? = null
    private val binding get() = _binding!!
    override fun onResume() {
        super.onResume()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // viewModel = ViewModelProvider(this).get(HeartsShopViewModel::class.java)
        _binding = FragmentHeartsShopBuyBinding.inflate(inflater, container, false)
        application = requireContext().applicationContext as MainApplication
        //viewModel!!.my_seq.value = application!!.userData!!.u_seq.toString()
        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paymodule = PaymentModule(requireContext(), this, ob)
        paymodule!!.initpay()
        paymodule?.billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // Connection successful
//
//                    querySkuDetails()

                    paymodule?.querySkuDetails()

                    Log.d("MYAPP-TEST---SUCCESS","")
                } else {

                }
            }

            override fun onBillingServiceDisconnected() {
                // Connection to billing service lost
                Log.d("MYAPP-TEST---DISCONNECTED","")
            }
        })
        observe()
        setOnClicks()
        consumeListenser = ConsumeResponseListener { billingResult, purchaseToken ->
            Log.d("결제", "billingResult.responseCode : ${billingResult.responseCode}")
            if(billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.d("결제", "소모 성공")

            } else {
                viewModel.toast.value = "결제가 실패 되었습니다."
                Log.d("결제", "소모 실패")
            }
        }
    }

    fun observe() {
        viewModel!!.toast.observe(viewLifecycleOwner, Observer
        { toast ->
            if (!toast.isNullOrEmpty()) {
                Toast.makeText(requireContext(), toast, Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setOnClicks() {
        val i = Intent(requireActivity(), HeartsShopInfoActivity::class.java)
        binding.ll1.setOnClickListener {
            var paydata = paymodule?.getskudata("sura_goods_hart_143")
            if (paymodule!!.productDetailsList.isNotEmpty())
                paydata?.let { it1 -> paystart(0, it1) }

        }
        binding.ll2.setOnClickListener {
            var paydata = paymodule?.getskudata("sura_goods_hart_66")
            if (paymodule!!.productDetailsList.isNotEmpty())
                paydata?.let { it1 -> paystart(1, it1) }

        }
        binding.ll3.setOnClickListener {
            var paydata = paymodule!!.getskudata("sura_goods_hart_31")
            if (paymodule!!.productDetailsList.isNotEmpty())
                paydata?.let { it1 -> paystart(1, it1) }

        }
        binding.ll4.setOnClickListener {
            var paydata = paymodule?.getskudata("sura_goods_hart_17")
            if (paymodule!!.productDetailsList.isNotEmpty())
                paydata?.let { it1 -> paystart(2, it1) }

        }
        binding.ll5.setOnClickListener {
            var paydata = paymodule?.getskudata("sura_goods_hart_2700")
            if (paymodule!!.productDetailsList.isNotEmpty())
                paydata?.let { it1 -> paystart(3, it1) }

        }
        binding.ll6.setOnClickListener {
            var paydata = paymodule?.getskudata("sura_goods_hart_1470")!!
            if (paymodule!!.productDetailsList.isNotEmpty())
                paystart(0, paydata)
        }
        binding.ll7.setOnClickListener {
            var paydata = paymodule?.getskudata("sura_goods_hart_614")!!
            if (paymodule!!.productDetailsList.isNotEmpty())
                paystart(0, paydata)
        }
        binding.llDrivepassOne.setOnClickListener {
            var drivePassPayDialog = DrivePassPayDialog(
                requireContext(),
                DrivePassDialogStatus.DAY1,
                "",
                0
            ) { type, isok ->
                if (isok) {
                    viewModel.DrivePassPayment1(
                        DrivePassDialogStatus.DAY1,
                        childFragmentManager,
                        requireContext()
                    )
                }

            }
            drivePassPayDialog!!.show(childFragmentManager, "")
        }
        binding!!.llDrivepass30.setOnClickListener {
            var drivePassPayDialog = DrivePassPayDialog(
                requireContext(),
                DrivePassDialogStatus.DAY30,
                "",
                0
            ) { type, isok ->
                if (isok) {
                    viewModel.DrivePassPayment1(
                        DrivePassDialogStatus.DAY30,
                        childFragmentManager,
                        requireContext()
                    )
                }

            }
            drivePassPayDialog!!.show(childFragmentManager, "")

        }
        binding.heartBuyTerms.setOnClickListener {
            i.putExtra("viewType", 0)
            startActivity(i)
            requireActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        }
        binding.heartBuyRefund.setOnClickListener {
            i.putExtra("viewType", 1)
            startActivity(i)
            requireActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        }

        binding.tvDriveGuide.setOnClickListener {
            val i = Intent(requireContext(), DrivePassGuideActivity::class.java)
            startActivity(i)
            requireActivity().overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }

        binding.ivVipGuide.setOnClickListener {
            val i = Intent(requireContext(), DrivePassVipGuideActivity::class.java)
            startActivity(i)
            requireActivity().overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }

//        binding.llNoti.setOnClickListener {
//            var d= PrvMiniProfileDialog(requireContext()){
//
//            }
//            d.show(childFragmentManager,"")
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun newInstance(): HeartsShopBuyFragment {
        val args = Bundle()

        val fragment = HeartsShopBuyFragment()
        fragment.arguments = args
        return fragment
    }

    fun paystart(selectedOfferIndex: Int, productDetails: ProductDetails) {
        Log.d("결제 시작 아이템", productDetails.toString())
        val offerToken = productDetails.subscriptionOfferDetails?.get(selectedOfferIndex)?.offerToken

        val productDetailsParamsList =
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
//                    .setOfferToken(offerToken.toString())
                    .build()
            )
        val billingFlowParams =
            BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()

// Launch the billing flow
        val billingResult =
            paymodule?.billingClient?.launchBillingFlow(requireActivity(), billingFlowParams)

        if (billingResult?.responseCode != BillingClient.BillingResponseCode.OK) {
            Log.d("결제", "paystart: 구매완료")
//
//        }

//        var param: BillingFlowParams? = null
//        var responseCode = 0
//        param = BillingFlowParams.newBuilder().setSkuDetails(sku).build()
//        responseCode =
//            paymodule!!.billingClient!!.launchBillingFlow(requireActivity(), param).responseCode
//        if (responseCode != BillingClient.BillingResponseCode.OK) {
//
//        }
        }
    }


    fun confirmPerchase(purchase_token: String) {
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase_token)
            .build()

        paymodule?.billingClient?.consumeAsync(consumeParams, consumeListenser)
    }

    var consumeResponseListener = object : ConsumeResponseListener {
        override fun onConsumeResponse(p0: BillingResult, p1: String) {
            if (p0.getResponseCode() == BillingClient.BillingResponseCode.OK && p1 != null) {

                Log.d("success", "success")
            }
        }
    }
    var ob = object : PaymentCallback {
        override fun SetItemList(list: List<ProductDetails>) {
            Log.d("결제아이템1", "SetItemList: ")
            Log.d("결제아이템2", list.toString())
            list.forEach { skdata ->
                var pid = skdata.productId

                var s =skdata?.oneTimePurchaseOfferDetails
                var price = s?.formattedPrice?.replace("₩", "")
                price = "₩ ${price}"
                var heart = skdata.title.replace("[^0-9]".toRegex(), "")
                Log.d("결제아이템 아이디", pid)
                Log.d("결제아이템 heart", heart)
                Log.d("결제아이템 price", price.toString())

                if (pid.equals("sura_goods_hart_143")) {
                    viewModel!!.tv_heart1.postValue(heart)
                    viewModel!!.tv_heart_pay1.postValue(price)
                } else if (pid.equals("sura_goods_hart_66")) {
                    viewModel!!.tv_heart2.postValue(heart)
                    viewModel!!.tv_heart_pay2.postValue(price)
                } else if (pid.equals("sura_goods_hart_31")) {
                    viewModel!!.tv_heart3.postValue(heart)
                    viewModel!!.tv_heart_pay3.postValue(price)
                } else if (pid.equals("sura_goods_hart_17")) {
                    viewModel!!.tv_heart4.postValue(heart)
                    viewModel!!.tv_heart_pay4.postValue(price)
                } else if (pid.equals("sura_goods_hart_2700")) {
                    viewModel!!.tv_heart5.postValue(heart)
                    viewModel!!.tv_heart_pay5.postValue(price)
                } else if (pid.equals("sura_goods_hart_1470")) {
                    viewModel!!.tv_heart6.postValue(heart)
                    viewModel!!.tv_heart_pay6.postValue(price)
                }else if (pid.equals("sura_goods_hart_614")) {
                    viewModel!!.tv_heart7.postValue(heart)
                    viewModel!!.tv_heart_pay7.postValue(price)
                }
                Log.d("CHECK_PAY_ITEM", list.size.toString())
            }

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        paymodule!!.billingClient.endConnection()
    }


    override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {
        Log.d("TEST_PURCHASE", p0.toString())
        when (p0.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (p1 != null) {
                    for ((count, purchase) in p1.withIndex()) {
                        if (count == 0) {

                            var order_id = purchase.orderId
                            var purtoken = purchase.purchaseToken
                            var p_id = purchase.skus.get(0)
                            var skudata = paymodule!!.getskudata(p_id)
                            var s =skudata?.oneTimePurchaseOfferDetails
                            Log.d("페이", s.toString())
                            var price = s?.formattedPrice
                            var heart = skudata!!.title.replace("[^0-9]".toRegex(), "")
                            if (order_id != null) {
                                Log.d("TEST_PURCHASE11 order_id", order_id)
                            }
                            Log.d("TEST_PURCHASE22  d", d)
                            if (order_id.equals(d) == false) {
                                if (order_id != null) {
                                    Log.d("TEST_PURCHASE order_id", order_id)
                                }
                                confirmPerchase(purtoken)
                                if (order_id != null) {
                                    viewModel!!.HeartPayment(
                                        p_id,
                                        purtoken,
                                        order_id,
                                        price.toString(),
                                        heart
                                    )
                                }
                            }
                            if (order_id != null) {
                                d = order_id
                            }
                            Log.d("TEST_PURCHASE  d", d)
                            Log.d("TEST_PURCHASE", "PUR" + purchase)
                        }
                    }
                }
            }

            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
//                val purchasesResult: Purchase.PurchasesResult =
//                    paymodule!!.billingClient!!.queryPurchasesAsync(BillingClient.SkuType.INAPP)
                paymodule?.billingClient?.queryPurchasesAsync(
                    QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.SUBS).build()
                ) { billingResult, purchaseList ->
                    for (p in purchaseList) {
//                        confirmPerchase(p.purchaseToken)
                    }
                }


                if (p1 != null) {
                    for (purchase in p1) {
                        Log.d("TEST_PURCHASE_OWNED", "PUR" + purchase)
                    }
                }
            }

            BillingClient.BillingResponseCode.USER_CANCELED -> {

            }
        }

    }
}
