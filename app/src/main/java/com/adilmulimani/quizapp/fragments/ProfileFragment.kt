package com.adilmulimani.quizapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adilmulimani.quizapp.R
import com.adilmulimani.quizapp.databinding.FragmentProfileBinding
import com.adilmulimani.quizapp.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue


class ProfileFragment : Fragment() {

    private val binding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    private var isExpand = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.imageButton.setOnClickListener {
            if(isExpand)
            {
                binding.expandableLayout.visibility=View.VISIBLE
                binding.imageButton.setImageResource(R.drawable.arrowup)
            }
            else
            {
                binding.expandableLayout.visibility=View.GONE
                binding.imageButton.setImageResource(R.drawable.downarrow)

            }
            isExpand=!isExpand


        }

        Firebase.database.reference.child("Users")
            .child(Firebase.auth.currentUser!!.uid).addListenerForSingleValueEvent(
                object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var user = snapshot.getValue<User>()
                        binding.name.text=user?.name
                        binding.mainName.text=user?.name
                        binding.age.text=user?.age.toString()
                        binding.email.text=user?.email
                        binding.password.text=user?.password
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )




        // Inflate the layout for this fragment
       return binding.root
    }

}