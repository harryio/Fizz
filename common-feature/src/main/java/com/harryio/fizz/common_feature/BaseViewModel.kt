package com.harryio.fizz.common_feature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harryio.fizz.common.FizzNetworkException
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {

    protected val disposables = CompositeDisposable()

    protected val _errorMsgLiveData = MutableLiveData<Event<Int>>()
    val errorMsgLiveData: LiveData<Event<Int>>
        get() = _errorMsgLiveData

    override fun onCleared() {
        super.onCleared()

        disposables.clear()
    }

    protected fun getNetworkExceptionHandler(defaultErrorId: Int = R.string.error_generic): (Throwable) -> Unit =
        { throwable: Throwable ->
            _errorMsgLiveData.value =
                Event((throwable as? FizzNetworkException)?.errorResId ?: defaultErrorId)
        }
}