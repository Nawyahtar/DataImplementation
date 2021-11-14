package com.example.dataimplementation

import android.content.SharedPreferences
import android.os.Bundle


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment

import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.dataimplementation.databinding.LoginFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sharePreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<LoginFragmentBinding>(
            inflater,
            R.layout.login_fragment,
            container,
            false
        )

        auth = Firebase.auth
        binding.buttonLogin.setOnClickListener {
            val email = binding.textInputEditTextEmail.text.toString()
            val password = binding.textInputEditTextPassword.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                               sharePreferences =
                                   PreferenceManager.getDefaultSharedPreferences(requireContext())
                               sharePreferences.edit().putString("email", email).apply()
                               sharePreferences.edit().putString("password",password).apply()
                               (requireActivity() as MainActivity).setUserEmail(email)
                               findNavController().navigate(
                                   LoginFragmentDirections.actionLoginFragmentToTitleFragment(
                                   )
                               )



                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Your email or password does not invalid",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
        binding.buttonRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        return binding.root
    }
}