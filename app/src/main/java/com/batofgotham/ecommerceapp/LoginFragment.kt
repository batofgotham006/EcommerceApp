package com.batofgotham.ecommerceapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.batofgotham.ecommerceapp.databinding.FragmentLoginBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: FragmentLoginBinding

    companion object{
        const val SIGN_IN_RESULT_CODE = 1000
        const val SIGN_IN_REQUEST_CODE = 1001
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_login,container,false)

        binding.loginButton.setOnClickListener{
            authorize()
        }
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer {
            when(it){
                LoginViewModel.AuthenticationState.AUTHENTICATED->{
                    binding.loginButton.text = getString(R.string.log_out_text)
                    binding.loginButton.setOnClickListener{
                        AuthUI.getInstance().signOut(requireContext())
                    }
                    binding.textView.text = resources.getString(R.string.welcome_message,
                        FirebaseAuth.getInstance().currentUser?.displayName)
                }

                else ->{
                    binding.loginButton.text = getString(R.string.register_log_in_text)
                    binding.loginButton.setOnClickListener {
                        authorize()
                    }
                    binding.textView.text = resources.getString(R.string.login_instruction)
                }
            }
        })
    }

    private fun authorize(){

        val providers = arrayListOf(
            AuthUI.IdpConfig.PhoneBuilder().build(), AuthUI.IdpConfig.EmailBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            SIGN_IN_RESULT_CODE
        )
    }

}