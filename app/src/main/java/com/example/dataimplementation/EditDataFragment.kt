package com.example.dataimplementation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dataimplementation.databinding.EditDataFragmentBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EditDataFragment :Fragment(){

    private val rootReference = Firebase.database.reference



    private val args: EditDataFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding=DataBindingUtil.inflate<EditDataFragmentBinding>(inflater,R.layout.edit_data_fragment,container,false)

        binding.textViewEditProductId.setText(args.productId)
        binding.textViewEditProductId.isEnabled=false
        binding.textViewEditProductName.setText(args.productName)
        binding.textViewEditPrice.setText(args.price.toString())
        binding.textViewEditStatus.setText(args.status)

        binding.buttonEditProduct.setOnClickListener{

            val productName=binding.textViewEditProductName.text.toString()
            val price=binding.textViewEditPrice.text.toString().toLong()
            val status=binding.textViewEditStatus.text.toString()
            updateData(productName,price,status)
        }

        return binding.root
    }

    private fun updateData(productName: String, price: Long, status: String) {

        val data = mapOf(
            "productName" to productName,
            "price" to price,
            "status" to status
        )
        val childUpdates = hashMapOf<String, Any>(
            "/data/${args.productId}" to data,

            )

        rootReference.updateChildren(childUpdates)
        Toast.makeText(requireContext(),"Update Successful",Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }
}
