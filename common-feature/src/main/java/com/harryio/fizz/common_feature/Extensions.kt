package com.harryio.fizz.common_feature

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager

val Fragment.prefs: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(requireContext().applicationContext)

fun Fragment.getPrefString(key: String, defVal: String? = null) = prefs.getString(key, defVal)

fun Fragment.savePrefString(key: String, value: String) = prefs.edit { putString(key, value) }

fun Fragment.hasPref(key: String) = prefs.contains(key)