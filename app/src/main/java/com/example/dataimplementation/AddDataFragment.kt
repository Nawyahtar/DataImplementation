package com.example.dataimplementation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dataimplementation.databinding.AddDataFragmentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.security.Key

class AddDataFragment : Fragment() {

    private val rootReference = Firebase.database.reference

    private val dataNodeReference = rootReference.child("data")
    private val args:AddDataFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<AddDataFragmentBinding>(
            inflater,
            R.layout.add_data_fragment,
            container,
            false
        )
        binding.textViewAddProductId.apply {
            setText(args.productId)
        }


        binding.buttonAddProduct.setOnClickListener {
            val productId = binding.textViewAddProductId.text.toString()
            val productName = binding.textViewAddProductName.text.toString()
            val price = binding.textViewAddPrice.text.toString().toLong()
            val status = binding.textViewAddStatus.text.toString()
            var numeric=true

            numeric = productId.matches("-?\\d+(\\.\\d+)?".toRegex())
            if (numeric){
                addData(productId,productName, price, status)
            }else{
                Toast.makeText(requireContext(),"ProductId must have only number",Toast.LENGTH_SHORT).show()
            }
 
        }

        return binding.root
    }


    private fun addData(productId:String, productName: String, price: Long, status: String) {


       dataNodeReference.child(productId).get().addOnCompleteListener {

            if (it.isSuccessful) {
               if (it.result?.value==null){
                   dataNodeReference.child(productId).apply {
                        child("productName").setValue(productName)
                       child("price").setValue(price)
                       child("status").setValue(status)
                   }
                   Toast.makeText(requireContext(), "Creating Successful", Toast.LENGTH_SHORT).show()
                   findNavController().popBackStack()

               }else {
                   Toast.makeText(requireContext(), "ProductId is already exist", Toast.LENGTH_SHORT).show()
               }

            }
        }


    }
}