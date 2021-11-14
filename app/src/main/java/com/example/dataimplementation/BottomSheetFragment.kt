package com.example.dataimplementation

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.dataimplementation.databinding.BottomSheetFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BottomSheetFragment : BottomSheetDialogFragment(){
    private val rootReference=Firebase.database.reference.child("data")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<BottomSheetFragmentBinding>(
            inflater,
            R.layout.bottom_sheet_fragment,
            container,
            false
        )
        binding.buttonEdit.setOnClickListener {
            editButton()
        }
        binding.buttonDelete.setOnClickListener {
            deleteButton()
        }
        return binding.root
    }

    private fun deleteButton() {
         val productId=arguments?.getString("productId")
         rootReference.child(productId!!).removeValue()
         dismiss()
         Toast.makeText(requireContext(),"Delete Successfully",Toast.LENGTH_SHORT).show()


    }


    private fun editButton() {

        val productId = arguments?.getString("productId")
        val productName = arguments?.getString("productName")
        val price = arguments?.getLong("price")
        val status = arguments?.getString("status")
        dismiss()
        findNavController().navigate(
            TitleFragmentDirections.actionTitleFragmentToEditDataFragment(
                productId!!,productName!!,price!!,status!!
            )
        )
    }
}

