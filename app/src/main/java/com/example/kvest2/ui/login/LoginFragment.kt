package com.example.kvest2.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kvest2.R
import com.example.kvest2.databinding.LoginFragmentBinding
import com.example.kvest2.ui.MainActivity
import com.example.kvest2.ui.afterTextChanged
import java.util.zip.Inflater

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, LoginViewModelFactory(requireContext()))
            .get(LoginViewModel::class.java)

        val username = binding.username
        val password = binding.password
        val loginBtn = binding.login

        arrayOf(username, password).forEach {
            it.afterTextChanged {
                viewModel.loginDataChanged(username.text.toString(), password.text.toString())
            }
        }

        // активируем кнопку в случае если вводимые данные корректны
        viewModel.loginFormState.observe(viewLifecycleOwner) {
            loginBtn.isEnabled = it.isDataValid == true
        }

        // логинимся по клику на кнопку, если вводимые данные корректны
        loginBtn.setOnClickListener {
            viewModel.login(username.text.toString(), password.text.toString())
        }

        // переход на страницу регистрации, по клику на кнопку регистрации
        binding.toRegistration.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        // отслеживаем юзера, авторизован или нет
        viewModel.loggedUser.observe(viewLifecycleOwner) {
            if (it.user != null) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }
        }

        return binding.root
    }
}
