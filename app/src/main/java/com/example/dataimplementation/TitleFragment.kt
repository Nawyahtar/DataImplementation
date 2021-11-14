package com.example.dataimplementation


import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*

import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment

import androidx.navigation.fragment.findNavController

import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dataimplementation.databinding.TitleFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator


class TitleFragment : Fragment(), RecyclerViewAdapterData.onLongItemClickListener {


    private val recyclerViewAdapterData = RecyclerViewAdapterData(this)

    private lateinit var binding: TitleFragmentBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private val dataNodeReference: DatabaseReference = Firebase.database.reference.child("data")
    private var productId: Long = 0
    private val checkEmail: String = "phoneminag99@gmail.com"
    private val checkPassword: String = "phoneminag"

    private val viewModel: TitleViewModel by viewModels<TitleViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = Firebase.auth
        if (auth.currentUser == null) {
            findNavController().navigate(R.id.action_titleFragment_to_loginFragment)

        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        binding = DataBindingUtil.inflate<TitleFragmentBinding>(
            inflater,
            R.layout.title_fragment,
            container,
            false
        )


        binding.recyclerViewData.apply {
            adapter = recyclerViewAdapterData
            layoutManager = LinearLayoutManager(
                requireContext(), RecyclerView.VERTICAL, false

            )
        }

            binding.editTextFilterName.addTextChangedListener {text->
                recyclerViewAdapterData.submitList(viewModel.filterName(text.toString()))
            }

        //show data from firebase
        viewModel.dataListObservable.observe(this, Observer {
            val text=binding.editTextFilterName.text.toString()
            recyclerViewAdapterData.submitList(viewModel.filterName(text))

        })

        setHasOptionsMenu(true)

        return binding.root
    }




    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.add_menu, menu)
        val email = sharedPreferences.getString("email", " ")
        val password = sharedPreferences.getString("password", "")
        if (email != null && password != null) {
            if (email != checkEmail && password != checkPassword) {
                menu.findItem(R.id.addDataFragment).setVisible(false)

            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.scanFragment -> scan()
            R.id.addDataFragment -> {
                lastData()

            }

        }
        return super.onOptionsItemSelected(item)

    }

    private fun lastData() {

        val lastData = dataNodeReference.orderByKey().limitToLast(1)
        lastData.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach() {
                    val lastId = it.key!!
                    productId = lastId.toLong().plus(1)
                }
                findNavController().navigate(
                    TitleFragmentDirections.actionTitleFragmentToAddDataFragment(
                        productId.toString()
                    )
                )
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }

        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
                dataNodeReference.child(result.contents).get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (it.result?.value != null) {
                            val productName = it.result!!.child("productName").getValue()
                            val price = it.result!!.child("price").getValue()
                            val status = it.result!!.child("status").getValue()
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setTitle("Scan Data")
                            builder.setMessage("ProductId=${result.contents} \n\nProductName=$productName \n\nPrice=$price \n\nStatus=$status")
                            builder.setPositiveButton("Ok", null)
                            builder.show()


                        } else {
                            Toast.makeText(
                                requireContext(),
                                "ProductId does not exist",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun scan() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setOrientationLocked(true);
        integrator.setPrompt("Scan QR code");
        integrator.setBeepEnabled(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);


        integrator.initiateScan();
    }


    override fun onLongClick(item: Data, position: Int) {

        val email = sharedPreferences.getString("email", " ")
        val password = sharedPreferences.getString("password", "")
        if (email != null && password != null) {
            if (email == checkEmail && password == checkPassword) {
                val bottomSheetFragment = BottomSheetFragment()
                val bundle = Bundle()
                bundle.putString("productId", item.productId)
                bundle.putString("productName", item.productName)
                bundle.putLong("price", item.price)
                bundle.putString("status", item.status)
                bottomSheetFragment.arguments = bundle
                bottomSheetFragment.show(childFragmentManager, null)
            } else {
                Toast.makeText(requireContext(), "You can't edit", Toast.LENGTH_SHORT).show()
            }
        }


    }


}


