package com.example.restaurant.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.restaurant.ui.theme.RestaurantAppTheme

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                RestaurantAppTheme {
                    RegisterScreen(onRegisterSuccess = { findNavController().popBackStack() })
                }
            }
        }
    }
}
