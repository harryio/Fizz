package com.harryio.fizz.common_feature

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

open class BaseFragment : Fragment() {

    internal val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(requireContext().applicationContext) }

    private val loadingDialog by lazy {
        MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_Fizz_Loading)
            .setCancelable(false)
            .setView(R.layout.dialog_loading)
            .show()
    }

    protected val defaultLoadingObserver = Observer<Boolean> { show ->
        if (show) {
            loadingDialog.show()
        } else {
            loadingDialog.dismiss()
        }
    }
}