package com.incubasys.formblok.common.data.pagination

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.incubasys.formblok.common.data.model.Message
import com.incubasys.formblok.common.data.model.PageInfo
import com.incubasys.formblok.common.data.model.SimpleResponse
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.util.ParseErrors
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class BaseDataSource<Item, API>(open var parseErrors: ParseErrors, var api: API) :
    PageKeyedDataSource<PageInfo, Item>() {
    var disposable: CompositeDisposable = CompositeDisposable()
    var initLoad = MutableLiveData<SimpleResponse>()
    var load = MutableLiveData<SimpleResponse>()
    var currentPageInfo: PageInfo? = null
    private lateinit var params: LoadParams<PageInfo>
    protected lateinit var callback: LoadCallback<PageInfo, Item>

    private val observable: Observable<List<Item>>? = null

    val paginatedNetworkStateLiveData: LiveData<SimpleResponse>
        get() = load

    val initialLoadStateLiveData: LiveData<SimpleResponse>
        get() = initLoad

    abstract fun getObservable(params: PageInfo): Observable<List<Item>>


    override fun loadInitial(
        params: LoadInitialParams<PageInfo>,
        callback: LoadInitialCallback<PageInfo, Item>
    ) {
        val pageInfo = PageInfo()
        pageInfo.page = 1
        pageInfo.timestamp = ""
        apiCall(pageInfo, callback, null)
    }

    override fun loadBefore(
        params: LoadParams<PageInfo>,
        callback: LoadCallback<PageInfo, Item>
    ) {

    }

    override fun loadAfter(
        params: LoadParams<PageInfo>,
        callback: LoadCallback<PageInfo, Item>
    ) {
        this.params = params
        this.callback = callback
        if (params.key.moreAvailable) {
            apiCall(params.key, null, callback)
        } else {
            callback.onResult(emptyList(), params.key)
        }
    }

    private fun apiCall(
        params: PageInfo,
        initialCallback: LoadInitialCallback<PageInfo, Item>?,
        callback: LoadCallback<PageInfo, Item>?
    ) {
        getObservable(params).subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe(object : Observer<List<Item>> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                    if (initialCallback != null) {
                        initLoad.postValue(SimpleResponse(ApiStatus.LOADING))
                    } else {
                        load.postValue(SimpleResponse(ApiStatus.LOADING))
                    }

                }

                override fun onNext(drinkItems: List<Item>) {
                    if (initialCallback != null) {
                        if (drinkItems.isEmpty()) {
                            initLoad.postValue(
                                SimpleResponse(
                                    ApiStatus.EMPTY,
                                    Message(code = 701, message = "No Items available :(").message
                                )
                            )
                        } else {
                            initLoad.postValue(SimpleResponse(ApiStatus.SUCCESS))
                        }
                        initialCallback.onResult(
                            drinkItems,
                            params,
                            currentPageInfo?.apply { page++ }
                        )
                    } else if (callback != null) {
                        load.postValue(SimpleResponse(ApiStatus.SUCCESS))
                        callback.onResult(drinkItems, currentPageInfo?.apply { page++ })
                    }
                }

                override fun onError(e: Throwable) {
                    if (initialCallback != null) {
                        //   parseError(e, initLoad)
                    } else {
                        // parseError(e, load)
                    }
                }

                override fun onComplete() {

                }
            })
    }

    fun retryPagination() {
        loadAfter(params, callback)
    }

    override fun invalidate() {
        disposable.dispose()
        super.invalidate()
    }
}
