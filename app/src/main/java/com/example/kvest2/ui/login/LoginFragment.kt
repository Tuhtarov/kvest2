package com.example.kvest2.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kvest2.data.model.AppUserSingleton
import com.example.kvest2.databinding.LoginFragmentBinding
import com.example.kvest2.ui.MainActivity
import com.example.kvest2.ui.afterTextChanged

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
        val loginBtn = binding.login

        username.afterTextChanged {
            viewModel.loginDataChanged(username.text.toString())
        }

        // активируем кнопку в случае если вводимые данные корректны
        viewModel.loginFormState.observe(viewLifecycleOwner) {
            loginBtn.isEnabled = it.isDataValid == true
        }

        // логинимся по клику на кнопку
        loginBtn.setOnClickListener {
            viewModel.signIn(username.text.toString())
        }

        // выполняем поиск текущего пользователя (зарегистрированного)
        viewModel.findCurrentUser()

        // отслеживаем состояние юзера, авторизован или нет
        viewModel.loggedUser.observe(viewLifecycleOwner) {
            if (it.user != null) {
                AppUserSingleton.setUser(it.user)

                // переходим в главный экран приложения
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            } else {
                // отключаем прогресс бар, включаем форму авторизации
                binding.progressCV.visibility = CardView.INVISIBLE
                binding.loginFormCV.visibility = CardView.VISIBLE
            }
        }

        return binding.root
    }
}
