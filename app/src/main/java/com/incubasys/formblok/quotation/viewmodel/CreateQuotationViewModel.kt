package com.incubasys.formblok.quotation.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.incubasys.formblok.common.data.model.MaterialData
import com.incubasys.formblok.common.data.model.RoomOutput
import com.incubasys.formblok.common.data.remote.ApiResponse
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.viewmodel.BaseViewModel
import com.incubasys.formblok.projects.data.model.PropertyOutput
import com.incubasys.formblok.projects.data.model.PropertyRoomOutput
import com.incubasys.formblok.quotation.data.QuotationRepository
import com.incubasys.formblok.quotation.data.model.BusinessRuleOutput
import com.incubasys.formblok.util.ParseErrors
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CreateQuotationViewModel @Inject constructor(
    application: Application,
    private val parseErrors: ParseErrors,
    private val quotationRepository: QuotationRepository
) :
    BaseViewModel(application, parseErrors) {

    var propertyOutput = PropertyOutput()
    var originalTotalLiveableArea = MutableLiveData<Double>()
    var originalTotalLiveableAreaAfterRenovation = MutableLiveData<Double>()

    var isRennovated = false

    var builtArea = ObservableField("")
    var demolisedArea = ObservableField("")

    val apiResponse = MutableLiveData<ApiResponse<Any>>()
    val roomList = MutableLiveData<ArrayList<RoomOutput>>()
    var room: MutableLiveData<RoomOutput> = MutableLiveData()

    var businessRuleOutput = BusinessRuleOutput()

    var customWidth = MutableLiveData<Double>()
    var customLength = MutableLiveData<Double>()
    var customArea = MutableLiveData<Double>()
    var customCalculatedAreaToShow = MutableLiveData<Double>()
    var actualRoomArea = 0.0
    var roomPosition = MutableLiveData<Int>()

    var calculatedAreaToShow = MutableLiveData<Double>()
    var calculatedAreaExceeds = ObservableBoolean(false)

    init {
        calculatedAreaToShow.value = 0.0
    }

    val materialData = MutableLiveData<ApiResponse<List<MaterialData>>>()

    private var timer: Timer = Timer()
    private val delay: Long = 600 // milliseconds


    fun addNewItem(position: Int) {
        if (!calculatedAreaExceeds.get()) {
            val newPosition = position + 1
            val tempList = roomList.value
            val tempItem = tempList?.get(position)
            tempItem?.let {
                tempList.add(
                    newPosition,
                    RoomOutput(
                        tempItem.id,
                        tempItem.icon,
                        tempItem.createdAt,
                        tempItem.name,
                        tempItem.length,
                        tempItem.width,
                        tempItem.isChecked
                    )
                )
            }
            roomList.value = tempList
            getCalculatedArea()
        }
    }

    fun updateItem(position: Int, isChecked: Boolean) {
        val tempList = roomList.value
        val tempItem = tempList?.get(position)
        if (!calculatedAreaExceeds.get()) {
            tempItem?.isChecked = !isChecked
            tempItem?.let { tempList.set(position, it) }
            roomList.value = tempList
            getCalculatedArea()
        } else {
            if (calculatedAreaExceeds.get() && isChecked) {
                tempItem?.isChecked = !isChecked
                tempItem?.let { tempList.set(position, it) }
                roomList.value = tempList
                getCalculatedArea()
            }
        }
    }

    private fun getCalculatedArea() {
        compositeDisposable.add(Observable.fromIterable(roomList.value).flatMap {
            Observable.just(it.length.times(it.width) * (if (it.isChecked) 1.0 else 0.0))
        }.toList().map {
            var total = 0.0
            it.forEach { item ->
                total += item
            }
            return@map total
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy {
                calculatedAreaToShow.value = it
            })
    }

    val isBuiltAreaCorrect = ObservableBoolean(false)
    val isDemolishCorrect = ObservableBoolean(false)
    val builtAreaError = ObservableField<String>()
    val demolishAreaError = ObservableField<String>()

    fun validateBuiltArea() {
        timer.cancel()
        timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    val actualBuiltArea =
                    if(builtArea.get()!!.contains("m2")){
                        builtArea.get()?.dropLast(2)
                    }else{
                        builtArea.get()
                    }
                    if (actualBuiltArea!!.isNotEmpty() && actualBuiltArea.toDouble() > 0.0  && actualBuiltArea.toDouble() <= propertyOutput.total_liveable_area) {
                        builtAreaError.set(null)
                        isBuiltAreaCorrect.set(true)
                    } else {
                        builtAreaError.set("Enter a valid Build Area")
                        isBuiltAreaCorrect.set(false)
                    }
                }
            },
            delay
        )
    }

    fun validateDemolishArea() {
        timer.cancel()
        timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    val actualBuiltArea =
                        if(builtArea.get()!!.contains("m2")){
                            builtArea.get()?.dropLast(2)
                        }else{
                            builtArea.get()
                        }
                    val actualDemolishedArea =
                        if(demolisedArea.get()!!.contains("m2")){
                            demolisedArea.get()?.dropLast(2)
                        }else{
                            demolisedArea.get()
                        }
                    if (actualDemolishedArea!!.isNotEmpty() && actualDemolishedArea.toDouble() > 0.0  && (actualBuiltArea!!.isNotEmpty()
                                && actualDemolishedArea.toDouble() <= actualBuiltArea.toDouble())) {
                        demolishAreaError.set(null)
                        isDemolishCorrect.set(true)
                    } else {
                        demolishAreaError.set("Enter a valid demolish Area")
                        isDemolishCorrect.set(false)
                    }
                }
            },
            delay
        )
    }

    fun bothFieldsValidate(): Boolean {
        return (isBuiltAreaCorrect.get() && isDemolishCorrect.get())
    }

    fun changeRennovatedLiveableArea() {
        propertyOutput.total_liveable_area =
            propertyOutput.total_liveable_area - propertyOutput.already_built_area + propertyOutput.demolish_area

        originalTotalLiveableAreaAfterRenovation.value = propertyOutput.total_liveable_area
    }

    private var propertySingleCopy = PropertyOutput()
    private var propertyDoubleCopy = PropertyOutput()

    fun changeValuesAccordingToDevelopmentType() {
        propertySingleCopy = propertyOutput
        propertyDoubleCopy = propertyOutput
        if (propertyOutput.development_type == 0) { // single
            if(isRennovated){
                propertyOutput.total_liveable_area = originalTotalLiveableArea.value!!
            }else{
            propertyOutput.total_liveable_area = originalTotalLiveableArea.value!!
            }
            propertyOutput = propertySingleCopy
        } else if (propertyOutput.development_type == 1) { // double
            if(isRennovated){
                propertyOutput.total_liveable_area = originalTotalLiveableArea.value!!
            }else{
                propertyOutput.total_liveable_area = originalTotalLiveableArea.value!!
            }
            val percentageValue = businessRuleOutput.drivewayDouble
            propertyDoubleCopy.driveway_area = propertyDoubleCopy.intermediate_area * (percentageValue.div(100))
            propertyDoubleCopy.ground_floor_area =
                propertyDoubleCopy.intermediate_area - propertyDoubleCopy.driveway_area
            propertyDoubleCopy.open_liveable_area = businessRuleOutput.alfrescoDouble
            propertyDoubleCopy.total_liveable_area =
                propertyDoubleCopy.ground_floor_area - propertyDoubleCopy.open_liveable_area
            propertyOutput = propertyDoubleCopy
        }
    }

    fun fillRooms() {
        roomList.value?.filter {
            it.isChecked
        }?.forEach { item ->
            val propertyRoomOutput = PropertyRoomOutput()
            propertyRoomOutput.roomId = item.id
            propertyRoomOutput.length = item.length
            propertyRoomOutput.width = item.width
            propertyRoomOutput.area = item.length.times(item.width)
            propertyOutput.property_rooms?.add(propertyRoomOutput)
        }
    }

    fun getBusinessRules() {
        compositeDisposable.add(quotationRepository.getBusinessRules()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                apiResponse.value = ApiResponse(ApiStatus.LOADING)
            }.subscribeBy(onNext = {
                apiResponse.value = ApiResponse(ApiStatus.SUCCESS)
                businessRuleOutput = it
            }, onError = {
                apiResponse.value = ApiResponse(ApiStatus.ERROR, parseErrors.interpretErrors(it))
            })
        )
    }

    fun getRooms() {
        if (roomList.value == null) {
            compositeDisposable.add(
                quotationRepository.getRooms().observeOn(AndroidSchedulers.mainThread())
                    .map { list ->
                        val list2 = ArrayList<RoomOutput>()
                        list.forEach {
                            list2.add(it)
                        }
                        return@map list2
                    }
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe {
                        apiResponse.value = ApiResponse(ApiStatus.LOADING)
                    }.subscribeBy(onNext = {
                        apiResponse.value = ApiResponse(ApiStatus.SUCCESS)
                        roomList.value = it
                        getCalculatedArea()
                    }, onError = {
                        apiResponse.value = ApiResponse(ApiStatus.ERROR, parseErrors.interpretErrors(it))
                    })
            )
        }
    }

    fun getMaterialData() {
        compositeDisposable.add(
            quotationRepository.getMaterialData().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    materialData.value = ApiResponse(ApiStatus.LOADING)
                }
                .subscribeBy(onNext = {
                    materialData.value = ApiResponse(ApiStatus.SUCCESS, it)
                }, onError = {
                    materialData.value = ApiResponse(ApiStatus.ERROR, parseErrors.interpretErrors(it))
                })
        )
    }
}
