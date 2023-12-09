package com.adilmulimani.quizapp.fragments

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.adilmulimani.quizapp.databinding.FragmentSpinBinding
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
import kotlin.random.Random


class SpinFragment : Fragment() {
    private lateinit var binding:FragmentSpinBinding
    private lateinit var timer:CountDownTimer
     val itemTitle = arrayOf("100","Try Again","500","Try Again","200","Try Again")
    var currentChance = 0L
    var currentCoins = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSpinBinding.inflate(inflater,container,false)

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

        Firebase.database.reference.child("PlayChance")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                       currentChance = snapshot.value as Long
                        binding.spinChance.text = (snapshot.value as Long).toString()

                    }
                    else
                    {
                        binding.spinChance.text ="0"
                        binding.spinButton.isEnabled=false
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


        Firebase.database.reference.child("playerCoin")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        currentCoins = snapshot.value as Long
                        binding.coinstext.text=currentCoins.toString()
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        return binding.root
    }

    private fun showResult(itemTitle: String, spinValue: Int)
    {
        if(spinValue%2==0)
        {
            var winCoins=itemTitle.toInt()
            Firebase.database.reference.child("playerCoin")
                .child(Firebase.auth.currentUser!!.uid)
                .setValue(winCoins+currentCoins)

            var historyModelClass=
                HistoryModelClasa(
                    timeAndDate = System.currentTimeMillis().toString(),
                    coin =winCoins.toString(),
                    isWithDrawal =false)
            Firebase.database.reference.child("playerCoinHistory")
                .child(Firebase.auth.currentUser!!.uid)
                .push()
                .setValue(historyModelClass)


            binding.coinstext.text=(winCoins+currentCoins).toString()

        }

        Toast.makeText(requireContext(),itemTitle,Toast.LENGTH_SHORT).show()

        currentChance -= 1
        Firebase.database.reference.child("PlayChance")
            .child(Firebase.auth.currentUser!!.uid)
            .setValue(currentChance)

        //Enable the wheel to try again
        binding.spinButton.isEnabled=true

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinButton.setOnClickListener {
            //disabling the button while the wheel is spinning
            binding.spinButton.isEnabled = false

            if(currentChance>0)
            {

                //generate a random value between 0 too 5
                val spin = Random.nextInt(6)
                val degrees = 60f*spin //calcualte the degree of rotation based on random value

                timer = object :CountDownTimer(5000,50)
                {
                    var rotation = 0f
                    override fun onTick(millisUntilFinished: Long) {
                        rotation += 5f //rotate the wheel
                        if(rotation>=degrees)
                        {
                            rotation = degrees
                            timer.cancel()
                            showResult(itemTitle[spin],spin)
                        }
                        binding.Wheel.rotation = rotation

                    }

                    override fun onFinish() {}
                }.start()
            }
            else
            {
                Toast.makeText(requireContext(),"You Are Out Of Spin Chances",Toast.LENGTH_SHORT).show()
            }


        }
    }
    }




