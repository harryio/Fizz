package com.harryio.fizz.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.harryio.fizz.common_feature.EventObserver
import com.harryio.fizz.common_feature.activitySavedStateViewModels

class LoginActivity : AppCompatActivity() {

    private lateinit var loginComponent: LoginComponent

    private val viewModel by activitySavedStateViewModels { handle ->
        loginComponent.loginViewModelFactory().create(handle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginComponent =
            (applicationContext as LoginComponentProvider).provideLoginComponentFactory().create()

        viewModel.errorMsgLiveData.observe(this, EventObserver {
            Snackbar.make(findViewById(R.id.fragment_container_view), it, Snackbar.LENGTH_SHORT)
                .show()
        })

        viewModel.loginCompleteLiveData.observe(this, EventObserver {
            Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show()
            finish()
        })

        handleDeeplinkIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeeplinkIntent(intent)
    }

    private fun handleDeeplinkIntent(intent: Intent?) {
        val approved = intent?.data?.getQueryParameter(viewModel.approvedKey)?.toBoolean()
        val requestToken = intent?.data?.getQueryParameter(viewModel.requestTokenKey)
        if (approved != null && requestToken != null) {
            viewModel.handleLoginDeeplinkResponse(approved, requestToken)
        }
    }
}