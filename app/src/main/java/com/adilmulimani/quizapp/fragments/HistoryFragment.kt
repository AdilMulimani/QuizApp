package com.adilmulimani.quizapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.adilmulimani.quizapp.R
import com.adilmulimani.quizapp.adapter.HistoryAdapter
import com.adilmulimani.quizapp.databinding.FragmentHistoryBinding
import com.adilmulimani.quizapp.model.HistoryModelClasa
import com.adilmulimani.quizapp.model.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue


class HistoryFragment : Fragment() {

 private val binding by lazy {
     FragmentHistoryBinding.inflate(layoutInflater)
 }
   lateinit var historyAdapter:HistoryAdapter

       private var listHistory = ArrayList<HistoryModelClasa>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.database.reference.child("playerCoinHistory")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                        listHistory.clear()
                    for(dataSnapShot in snapshot.children)
                    {
                        var data = dataSnapShot.getValue(HistoryModelClasa::class.java)
                        listHistory.add(data!!)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    //TODO("Not yet implemented")
                }

            })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Firebase.database.reference.child("playerCoin")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                       var currentCoins = snapshot.value as Long
                        binding.coinstext.text=currentCoins.toString()
                        historyAdapter.notifyDataSetChanged()
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })





        binding.coin.setOnClickListener{
            val bottomSheetDialogFragment: BottomSheetDialogFragment = WithDrawalFragment()
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager,"test")
            bottomSheetDialogFragment.enterTransition
        }
        binding.coinstext.setOnClickListener{
            val bottomSheetDialogFragment: BottomSheetDialogFragment = WithDrawalFragment()
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager,"test")
            bottomSheetDialogFragment.enterTransition
        }


         historyAdapter = HistoryAdapter(listHistory)
        binding.historyRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.historyRecyclerView.adapter = historyAdapter
        binding.historyRecyclerView.setHasFixedSize(true)

        Firebase.database.reference.child("Users")
            .child(Firebase.auth.currentUser!!.uid).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var user = snapshot.getValue<User>()
                        binding.name.text=user?.name
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )


        return binding.root
    }
}

