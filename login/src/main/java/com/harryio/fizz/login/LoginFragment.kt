package com.harryio.fizz.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.harryio.fizz.common.KEY_SESSION_ID
import com.harryio.fizz.common_feature.EventObserver
import com.harryio.fizz.common_feature.savePrefString
import com.harryio.fizz.login.databinding.FragmentLoginBinding

const val LOGIN_DEEPLINK = "harryio://fizz.authentication/allow"

class LoginFragment : Fragment() {

    private val viewModel by activityViewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.model = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.openUrl.observe(
            viewLifecycleOwner,
            EventObserver {
                requireActivity().startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(it)
                    )
                )
            })

        viewModel.sessionIdLiveData.observe(viewLifecycleOwner, EventObserver {
            savePrefString(KEY_SESSION_ID, it)
        })
    }
}