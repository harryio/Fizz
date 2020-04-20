package com.harryio.fizz.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.harryio.fizz.common_feature.EventObserver

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel.errorMsgLiveData.observe(this, EventObserver {
            Snackbar.make(findViewById(R.id.fragment_container_view), it, Snackbar.LENGTH_SHORT)
                .show()
        })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val approved = intent?.data?.getQueryParameter(viewModel.approvedKey)?.toBoolean()
        val requestToken = intent?.data?.getQueryParameter(viewModel.requestTokenKey)
        viewModel.handleLoginDeeplinkResponse(approved, requestToken)

        viewModel.loginCompleteLiveData.observe(this, EventObserver {
            Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show()
            finish()
        })
    }
}