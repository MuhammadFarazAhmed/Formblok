package com.incubasys.formblok.explore.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode.OK
import com.android.billingclient.api.BillingClient.BillingResponseCode.USER_CANCELED
import com.google.android.gms.maps.model.LatLng
import com.incubasys.formblok.common.data.model.SubscriptionInput
import com.incubasys.formblok.common.data.model.SubscriptionOutput
import com.incubasys.formblok.common.data.remote.ApiResponse
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.viewmodel.BaseViewModel
import com.incubasys.formblok.explore.data.ExploreRepository
import com.incubasys.formblok.explore.data.model.AddressMapItem
import com.incubasys.formblok.util.ParseErrors
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ExploreViewModel @Inject constructor(
    application: Application,
    private val parseErrors: ParseErrors,
    private val exploreRepository: ExploreRepository
) : BaseViewModel(application, parseErrors) {

    var currentLatLng = MutableLiveData<LatLng>()
    var addressListResponse = MutableLiveData<ApiResponse<List<AddressMapItem>>>()
    var selectedProjectId : Int? = -1

    /*private var subscriptionInput = SubscriptionInput()
    var subscriptionResponse = MutableLiveData<ApiResponse<SubscriptionOutput>>()*/
    private var disposable: Disposable? = null


    fun fetchAddresses(latLng: LatLng) {
        if (disposable != null) {
            disposable?.dispose()
        }
        disposable = exploreRepository.fetchAddressesForaArea(latLng).map { list ->
            val addressList = mutableListOf<AddressMapItem>()
            list.forEach {
                addressList.add(AddressMapItem(it))
            }
            addressList
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                addressListResponse.value = ApiResponse(ApiStatus.LOADING)
            }
            .subscribeBy(
                onNext = {
                    addressListResponse.value = ApiResponse(ApiStatus.SUCCESS, it)

                }, onError = {
                    addressListResponse.value = ApiResponse(ApiStatus.ERROR, parseErrors.interpretErrors(it))
                })
    }

    /*private fun sendSubscriptionDataToServer() {
        compositeDisposable.add(exploreRepository.pOSTSubscription(subscriptionInput)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                subscriptionResponse.value = ApiResponse(ApiStatus.LOADING)
            }
            .subscribeBy(
                onNext = {
                    subscriptionResponse.value = ApiResponse(ApiStatus.SUCCESS, it)

                }, onError = {
                    subscriptionResponse.value = ApiResponse(ApiStatus.ERROR, parseErrors.interpretErrors(it))
                }
            ))
    }*/


   /* private val mBillingClient: BillingClient =
        BillingClient.newBuilder(application).enablePendingPurchases().setListener(this).build()

    fun getBillingClient() = mBillingClient

    private var skus: HashMap<String, List<String>> = HashMap()

    init {
        skus[BillingClient.SkuType.SUBS] = listOf("unlimited_searches")

        mBillingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                Log.i("billingResult", "onBillingServiceDisconnected()")
            }

            override fun onBillingSetupFinished(billingResult: BillingResult?) {
                if (billingResult?.responseCode == OK) {
                    Log.i("billingResult", "onBillingSetupFinished() response:" + billingResult.responseCode)
                } else {
                    Log.i("billingResult", "onBillingSetupFinished() error code:" + billingResult?.responseCode)
                }
            }

        })
    }

    val gotASkuLiveData = MutableLiveData<Boolean>()


    override fun onPurchasesUpdated(billingResult: BillingResult?, purchases: MutableList<Purchase>?) {
        Log.d("billingResult", "onPurchasesUpdated() response: " + billingResult?.responseCode)
        if (billingResult?.responseCode == OK && purchases != null) {
            for (purchase in purchases) {
                Log.i("OrderId", purchase.orderId)
                Log.i("Purchase Token", purchase.purchaseToken)
                Log.i("sku", purchase.sku)
                with(subscriptionInput) {
                    product_id = purchase.sku
                    purchase_token = purchase.purchaseToken
                }
                sendSubscriptionDataToServer()
            }
        } else if (billingResult?.responseCode == USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
        } else {
            // Handle any other error codes.
        }
    }


    private fun getSkus(@BillingClient.SkuType type: String): List<String>? {
        return skus[type]
    }

    private fun querySkuDetailsAsync(
        @BillingClient.SkuType itemType: String,
        skuList: List<String>, listener: SkuDetailsResponseListener
    ) {
        val skuDetailsParams = SkuDetailsParams.newBuilder()
            .setSkusList(skuList).setType(itemType).build()
        mBillingClient.querySkuDetailsAsync(
            skuDetailsParams
        ) { billingResult, skuDetailsList -> listener.onSkuDetailsResponse(billingResult, skuDetailsList) }
    }

    fun fetchSkuForSubscription() {
        // Start querying for SKUs
        val inAppSkus = getSkus(BillingClient.SkuType.SUBS)
        if (inAppSkus != null) {
            querySkuDetailsAsync(
                BillingClient.SkuType.SUBS,
                inAppSkus,
                SkuDetailsResponseListener { billingResult, skuDetailsList ->
                    if (billingResult.responseCode == OK && skuDetailsList != null) {
                        for (details in skuDetailsList) {
                            Log.i("billingResult", "Got a SKU: $details")
                        }
                        if(skuDetailsList.size >= 0) {
                            setSku(skuDetailsList[0])
                        }
                        gotASkuLiveData.value = true
                    }
                })
        }
    }

    private var skuDetails: SkuDetails? = null

    private fun setSku(skuDetails: SkuDetails) {
        this.skuDetails = skuDetails
    }

    fun getSku() = skuDetails*/

    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable?.dispose()
        }
    }

}
