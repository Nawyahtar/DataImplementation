package com.example.dataimplementation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import java.sql.Array

class TitleViewModel:ViewModel() {
    val dataListObservable=MutableLiveData<List<Data>>()
    val dataList = mutableListOf<Data>()

    private   val dataNodeReference: DatabaseReference=Firebase.database.reference.child("data")
    init {

        dataNodeReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val newList: List<Data> = snapshot.children.mapIndexed { index, dataSnapshot ->

                    val productName = dataSnapshot.child("productName").getValue<String>().orEmpty()
                    val price = dataSnapshot.child("price").getValue<Long>() ?: 0L
                    val status = dataSnapshot.child("status").getValue<String>().orEmpty()
                    val productId = dataSnapshot.key!!
                    Data(
                        position = index + 1,
                        productId = productId,
                        productName = productName,
                        price = price,
                        status = status
                    )


                }
                dataList.clear()
                dataList.addAll(newList)
                dataListObservable.postValue(newList)

            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }

        })
    }
      fun filterName(text:String):List<Data>{
       val filterList =if (text.isNullOrEmpty()){
             dataList

        }else{
             dataList.filter { it.productName.contains(text.toString(),true) }

        }
          return filterList
    }



}