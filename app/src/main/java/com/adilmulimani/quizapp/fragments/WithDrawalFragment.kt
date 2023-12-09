package com.adilmulimani.quizapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.adilmulimani.quizapp.R
import com.adilmulimani.quizapp.databinding.FragmentWithDrawalBinding
import com.adilmulimani.quizapp.model.HistoryModelClasa
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.currentCoroutineContext


class WithDrawalFragment : BottomSheetDialogFragment() {

private lateinit var binding: FragmentWithDrawalBinding
var currentCoin = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
    binding=FragmentWithDrawalBinding.inflate(inflater,container,false)

        Firebase.database.reference.child("playerCoin")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists())
                        currentCoin = snapshot.value as Long
                }

                override fun onCancelled(error: DatabaseError) {
                   // TODO("Not yet implemented")
                }


            })

        binding.transferButton.setOnClickListener {
            if(binding.EnteredAmount.text.toString()=="" || binding.PaytmNumber.text.toString()=="")
            {
                Toast.makeText(requireContext(),"Enter All Fields",Toast.LENGTH_SHORT).show()
            }
            else if(binding.EnteredAmount.text.toString().toDouble()<=currentCoin.toDouble())
            {
                Firebase.database.reference.child("playerCoin")
                    .child(Firebase.auth.currentUser!!.uid)
                    .setValue(currentCoin.toDouble()-(binding.EnteredAmount.text.toString().toDouble()))

                    var historyModelClass
                    =HistoryModelClasa(System.currentTimeMillis().toString(),
                        binding.EnteredAmount.text.toString()
                    ,true)
                Firebase.database.reference.child("playerCoinHistory")
                    .child(Firebase.auth.currentUser!!.uid)
                    .push()
                    .setValue(historyModelClass)


            }
            else
            {
                Toast.makeText(requireContext(),"Low Balance",Toast.LENGTH_SHORT).show()
            }
        }


        return binding.root


    }

}