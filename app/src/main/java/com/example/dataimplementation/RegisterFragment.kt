package com.example.dataimplementation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dataimplementation.databinding.RegisterFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterFragment :Fragment(){
    private lateinit var auth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding=DataBindingUtil.inflate<RegisterFragmentBinding>(inflater,R.layout.register_fragment,container,false)
        auth=Firebase.auth
        binding.buttonRegisterConfirm.setOnClickListener{
            val email=binding.textInputEditTextEmailRegister.text.toString()
            val password=binding.textInputEditTextPasswordRegister.text.toString()
            val passwordAgain=binding.textInputEditTextPasswordAgainRegister.text.toString()

            if (password!=passwordAgain){
                Toast.makeText(requireContext(),"The password does not match",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{task->
                    if(task.isSuccessful){

                            Toast.makeText(requireContext(),"Register Successful",Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()


                    }else{
                        Toast.makeText(requireContext(),"Register Fail",Toast.LENGTH_SHORT).show()
                    }
                }

        }

        return binding.root
    }
}