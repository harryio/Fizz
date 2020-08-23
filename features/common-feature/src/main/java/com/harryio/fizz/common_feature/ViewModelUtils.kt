package com.harryio.fizz.common_feature

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner

inline fun <reified T : ViewModel> SavedStateRegistryOwner.createAbstractSavedStateViewModelFactory(
    arguments: Bundle,
    crossinline creator: (SavedStateHandle) -> T
): ViewModelProvider.Factory = object : AbstractSavedStateViewModelFactory(this, arguments) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T = creator(handle) as T
}

inline fun <reified T : ViewModel> Fragment.fragmentSavedStateViewModels(
    noinline creator: (SavedStateHandle) -> T
): Lazy<T> {
    return createViewModelLazy(T::class, storeProducer = {
        viewModelStore
    }, factoryProducer = {
        createAbstractSavedStateViewModelFactory(arguments ?: Bundle(), creator)
    })
}

inline fun <reified T: ViewModel> Fragment.fragmentSavedStateViewModelsForActivity(
    noinline creator: (SavedStateHandle) -> T
): Lazy<T> {
    return activityViewModels {
        createAbstractSavedStateViewModelFactory(arguments ?: Bundle(), creator)
    }
}

inline fun <reified T : ViewModel> AppCompatActivity.activitySavedStateViewModels(
    noinline creator: (SavedStateHandle) -> T
): Lazy<T> {
    return viewModels(factoryProducer = {
        createAbstractSavedStateViewModelFactory(intent?.extras ?: Bundle(), creator)
    })
}