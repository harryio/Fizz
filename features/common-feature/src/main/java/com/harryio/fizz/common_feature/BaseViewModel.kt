package com.harryio.fizz.common_feature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harryio.fizz.common.FizzNetworkException

open class BaseViewModel : ViewModel() {

    protected val _errorMsgLiveData = MutableLiveData<Event<Int>>()
    val errorMsgLiveData: LiveData<Event<Int>>
        get() = _errorMsgLiveData

    protected fun getNetworkExceptionHandler(defaultErrorId: Int = R.string.error_generic): (Throwable) -> Unit =
        { throwable: Throwable ->
            if (BuildConfig.DEBUG) {
                throwable.printStackTrace()
            }

            _errorMsgLiveData.value =
                Event((throwable as? FizzNetworkException)?.errorResId ?: defaultErrorId)
        }
}