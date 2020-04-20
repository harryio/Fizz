package com.harryio.fizz.common_feature

import androidx.core.content.edit

fun BaseFragment.getPrefString(key: String, defVal: String? = null) = prefs.getString(key, defVal)

fun BaseFragment.savePrefString(key: String, value: String) = prefs.edit { putString(key, value) }

fun BaseFragment.hasPref(key: String) = prefs.contains(key)