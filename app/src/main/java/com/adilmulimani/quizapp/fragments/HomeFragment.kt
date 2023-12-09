package com.adilmulimani.quizapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.adilmulimani.quizapp.R
import com.adilmulimani.quizapp.adapter.CategoryAdapter
import com.adilmulimani.quizapp.databinding.FragmentHomeBinding
import com.adilmulimani.quizapp.model.CategoryModelClass
import com.adilmulimani.quizapp.model.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue


class HomeFragment : Fragment() {

    private val binding:FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    private var categoryList = ArrayList<CategoryModelClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryList.add(CategoryModelClass(R.drawable.scince,"Science"))
        categoryList.add(CategoryModelClass(R.drawable.english1,"English"))
        categoryList.add(CategoryModelClass(R.drawable.geography,"Geo"))
        categoryList.add(CategoryModelClass(R.drawable.math,"Math"))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Firebase.database.reference.child("playerCoin")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                       var currentCoins = snapshot.value as Long
                        binding.coinstext.text=currentCoins.toString()
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


        binding.coin.setOnClickListener{
            val bottomSheetDialogFragment:BottomSheetDialogFragment = WithDrawalFragment()
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager,"test")
            bottomSheetDialogFragment.enterTransition
        }
        binding.coinstext.setOnClickListener{
            val bottomSheetDialogFragment:BottomSheetDialogFragment = WithDrawalFragment()
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager,"test")
            bottomSheetDialogFragment.enterTransition
        }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //categoryList.clear()
        
        binding.categoryRecyclerView.layoutManager=GridLayoutManager(requireContext(),2)
        val categoryAdapter = CategoryAdapter(categoryList,requireActivity())
        binding.categoryRecyclerView.adapter=categoryAdapter
    }

//    companion object
//    {}

}