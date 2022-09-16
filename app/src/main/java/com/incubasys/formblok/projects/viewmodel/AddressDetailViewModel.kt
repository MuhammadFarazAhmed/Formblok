package com.incubasys.formblok.projects.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*
import com.google.android.gms.maps.model.LatLng
import com.incubasys.formblok.common.data.model.Message
import com.incubasys.formblok.common.data.model.SubscriptionInput
import com.incubasys.formblok.common.data.model.SubscriptionOutput
import com.incubasys.formblok.common.data.remote.ApiResponse
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.viewmodel.BaseViewModel
import com.incubasys.formblok.projects.data.AddressRepository
import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.projects.data.model.PropertyInput
import com.incubasys.formblok.projects.data.model.PropertyOutput
import com.incubasys.formblok.util.ParseErrors
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddressDetailViewModel @Inject constructor(
    application: Application,
    private val parseErrors: ParseErrors,
    private val addressRepository: AddressRepository
) :
    BaseViewModel(application, parseErrors), PurchasesUpdatedListener {


    private var subscriptionInput = SubscriptionInput()
    var subscriptionResponse = MutableLiveData<ApiResponse<SubscriptionOutput>>()
    var isAddressQuoted = false

    var addressId: Int? = null
    var projectId: Int = -1
    var latLng: LatLng? = null


    val addressDetailLivedata: LiveData<ApiResponse<PropertyOutput>>
        get() = _addressDetailLiveData

    private var _addressDetailLiveData = MutableLiveData<ApiResponse<PropertyOutput>>()
    var checkSubscriptionLiveData = MutableLiveData<ApiResponse<Message>>()

    var propertyOutput = MutableLiveData<PropertyOutput>()

    fun getAddressDetail(addressId: String) =
        addressRepository.getAddressDetail(addressId).map { data ->
            propertyOutput.postValue(data)
            propertyOutput
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                _addressDetailLiveData.value = ApiResponse(ApiStatus.LOADING)
            }
            .subscribeBy(onNext = {
                _addressDetailLiveData.value = ApiResponse(ApiStatus.SUCCESS)
            }, onError = {
                _addressDetailLiveData.value = ApiResponse(ApiStatus.ERROR, parseErrors.interpretErrors(it))
            })

    val apiResponse = MutableLiveData<ApiResponse<Project>>()
    fun addAPropertyToProject(id: Int, propertyInput: PropertyInput) {
        compositeDisposable.add(addressRepository.addPropertyToProject(id, propertyInput)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                apiResponse.value = ApiResponse(ApiStatus.LOADING)
            }.subscribeBy(onNext = {
                apiResponse.value = ApiResponse(ApiStatus.SUCCESS,it)
            }, onError = {
                apiResponse.value = ApiResponse(ApiStatus.ERROR,parseErrors.interpretErrors(it))
            })
        )
    }

    private val mBillingClient: BillingClient =
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
                if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.i("billingResult", "onBillingSetupFinished() response:" + billingResult.responseCode)
                } else {
                    Log.i("billingResult", "onBillingSetupFinished() error code:" + billingResult?.responseCode)
                }
            }

        })
    }

    private fun sendSubscriptionDataToServer() {
        compositeDisposable.add(addressRepository.pOSTSubscription(subscriptionInput)
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
    }

    val gotASkuLiveData = MutableLiveData<Boolean>()


    override fun onPurchasesUpdated(billingResult: BillingResult?, purchases: MutableList<Purchase>?) {
        Log.d("billingResult", "onPurchasesUpdated() response: " + billingResult?.responseCode)
        if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
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
        } else if (billingResult?.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
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
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
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

    fun getSku() = skuDetails

    fun checkSubscription(){
        compositeDisposable.add(
            addressRepository.getSubscription().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    checkSubscriptionLiveData.value = ApiResponse(ApiStatus.LOADING)
                }
                .subscribeBy(onNext = {
                    checkSubscriptionLiveData.value = ApiResponse(ApiStatus.SUCCESS)
                }, onError = {
                    checkSubscriptionLiveData.value = ApiResponse(ApiStatus.ERROR, parseErrors.interpretErrors(it))
                })
        )
    }
}
