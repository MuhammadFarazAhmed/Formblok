package com.incubasys.formblok.common.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.incubasys.formblok.common.data.model.Message
import com.incubasys.formblok.common.data.remote.ApiResponse
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.util.ParseErrors
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

abstract class BaseViewModel(application: Application, private val parseErrors: ParseErrors) :
    AndroidViewModel(application) {

    internal var compositeDisposable: CompositeDisposable = CompositeDisposable()


    private fun disposeComposite() = compositeDisposable.dispose()

   /* internal fun parseError(throwable: Throwable, data: MutableLiveData<Message>) {
        parseError(throwable,
            Consumer { message -> data.postValue(ApiResponse<Message>(ApiStatus.ERROR, message)) },
            Consumer { throwable ->
                throwable.printStackTrace()
                data.postValue(ApiResponse<Throwable>(ApiStatus.ERROR, Message("Error", 800)))
            })
    }

    private fun parseError(throwable: Throwable, consumer: Consumer<Message>, consumer2: Consumer<Throwable>) {
        compositeDisposable.add(
            Observable.just(throwable).map<Message> { throwable ->
                parseErrors.interpretErrors(throwable)
            }
                .subscribeOn(
                    Schedulers.io()
                )
                .observeOn(Schedulers.computation()).subscribe(consumer, consumer2)
        )
    }*/

    /*protected fun observableSimpleCall(messageObservable: Observable<Message>): LiveData<ApiResponse<Message>> {
        val liveData = MutableLiveData<ApiResponse<Message>>()
        messageObservable
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe(object : Observer<Message> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                    liveData.postValue(ApiResponse(ApiStatus.LOADING))
                }

                override fun onNext(message: Message) {
                    liveData.postValue(ApiResponse<Message>(ApiStatus.SUCCESS).data(message))
                }

                override fun onError(e: Throwable) {
                    parseError(e, liveData)
                }

                override fun onComplete() {

                }
            })
        return liveData
    }*/

    override fun onCleared() {
        super.onCleared()
        disposeComposite()
    }
}

