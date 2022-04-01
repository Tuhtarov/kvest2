package com.example.kvest2.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kvest2.databinding.RegistrationFragmentBinding
import com.example.kvest2.ui.MainActivity
import com.example.kvest2.ui.afterTextChanged

class RegistrationFragment : Fragment() {

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    private lateinit var viewModel: RegistrationViewModel
    private var _binding: RegistrationFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegistrationFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, RegistrationViewModelFactory(requireContext()))
            .get(RegistrationViewModel::class.java)

        val username = binding.username
        val password = binding.password
        val name = binding.name
        val phone = binding.phone

        fun dataChanged() = viewModel.registrationDataChanged (
            username.text.toString(),
            password.text.toString(),
            name.text.toString(),
            phone.text.toString()
        )

        arrayOf(username, password, name, phone).forEach {
            it.afterTextChanged {
                dataChanged()
            }
        }

        // если форма корректно заполняется, то кнопка становится доступной
        viewModel.registrationFormState.observe(viewLifecycleOwner) {
            binding.registration.isEnabled = it.isDataValid
        }

        // регистрируем если данные гуд
        binding.registration.setOnClickListener {
            viewModel.registrationFormState.observe(viewLifecycleOwner) {
                if (it.isDataValid) {
                    viewModel.registration (
                        username.text.toString(),
                        password.text.toString(),
                        name.text.toString(),
                        phone.text.toString()
                    )
                }
            }
        }

        // отслеживаем юзера, если он был зареган, считаем его авторизовавшимся
        viewModel.loggedUser.observe(viewLifecycleOwner) {
            if (it.user != null) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }
        }

        return binding.root
    }
}
