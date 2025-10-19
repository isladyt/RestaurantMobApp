package com.example.restaurant.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.restaurant.R
import com.example.restaurant.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private val vm: AuthViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ИСПРАВЛЕНО: Переходим на экран регистрации по нажатию
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }

        binding.btnLogin.setOnClickListener {
            val login = binding.etLogin.text.toString()
            val pass = binding.etPassword.text.toString()
            vm.login(login, pass)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            vm.loginState.collect { state ->
                when (state) {
                    is LoginState.Success -> {
                        findNavController().navigate(R.id.action_login_to_menu)
                    }
                    is LoginState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    is LoginState.RegistrationSuccess -> {
                        Toast.makeText(requireContext(), "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show()
                    }
                    is LoginState.Loading -> {
                        // Можно показать индикатор загрузки
                    }
                    is LoginState.Idle -> {
                        // Ничего не делаем
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
