package com.adilmulimani.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.adilmulimani.quizapp.databinding.ActivityQuizBinding
import com.adilmulimani.quizapp.fragments.WithDrawalFragment
import com.adilmulimani.quizapp.model.Question
import com.adilmulimani.quizapp.model.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.firestore.firestore

class QuizActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityQuizBinding.inflate(layoutInflater)
    }

    var currentChance = 0L
    private lateinit var questionList:ArrayList<Question>
    var currentQuestion = 0
    var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Firebase.database.reference.child("playerCoin")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                      var  currentCoins = snapshot.value as Long
                        binding.coinstext.text=currentCoins.toString()
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })








        questionList = ArrayList<Question>()
        var image = intent.getIntExtra("quizImage",0)
        binding.quizImage.setImageResource(image)
        var catText = intent.getStringExtra("questionType")
        binding.coin.setOnClickListener{
            val bottomSheetDialogFragment: BottomSheetDialogFragment = WithDrawalFragment()
            bottomSheetDialogFragment.show(this@QuizActivity.supportFragmentManager,"test")
            bottomSheetDialogFragment.enterTransition
        }
        binding.coinstext.setOnClickListener{
            val bottomSheetDialogFragment: BottomSheetDialogFragment = WithDrawalFragment()
            bottomSheetDialogFragment.show(this@QuizActivity.supportFragmentManager,"test")
            bottomSheetDialogFragment.enterTransition

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
        }

        Firebase.firestore.collection("Questions")
            .document(catText.toString())
            .collection("Question1")
            .get()
            .addOnSuccessListener {
                questionData->
                questionList.clear()
                for(data in questionData.documents)
                {
                    var question: Question? =data.toObject(Question::class.java)
                    questionList.add(question!!)
                }
                if(questionList.size>0)
                {
                    binding.QuestionText.text=questionList[currentQuestion].question
                    binding.optionOne.text=questionList[currentQuestion].option1
                    binding.optionTwo.text=questionList[currentQuestion].option2
                    binding.optionThree.text=questionList[currentQuestion].option3
                    binding.optionFour.text=questionList[currentQuestion].option4
                }
            }

        binding.optionOne.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.optionOne.text.toString())

        }
        binding.optionTwo.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.optionTwo.text.toString())
        }
        binding.optionThree.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.optionThree.text.toString())
        }
        binding.optionFour.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.optionFour.text.toString())
        }

    }

    private fun nextQuestionAndScoreUpdate(s: String) {

        if(s==questionList[currentQuestion].ans)
        {
            score+=10
           // Toast.makeText(this,score.toString(),Toast.LENGTH_SHORT).show()
        }

        currentQuestion++
         if(currentQuestion<questionList.size)
        {
            binding.QuestionText.text=questionList[currentQuestion].question
            binding.optionOne.text=questionList[currentQuestion].option1
            binding.optionTwo.text=questionList[currentQuestion].option2
            binding.optionThree.text=questionList[currentQuestion].option3
            binding.optionFour.text=questionList[currentQuestion].option4
        }
        else
         {
             //Toast.makeText(this,"You have answered all the Questions!",Toast.LENGTH_SHORT).show()
             if(score>=30)
             {
                 binding.winnerView.visibility=View.VISIBLE

                 var isUpdated = false
                 if(isUpdated)
                 {

                 }
                 else
                 {
                     Firebase.database.reference.child("PlayChance")
                         .child(Firebase.auth.currentUser!!.uid)
                         .addValueEventListener(object :ValueEventListener
                         {
                             override fun onDataChange(snapshot: DataSnapshot) {
                                 if(snapshot.exists()){
                                     currentChance = snapshot.value as Long
                                 }
                             }
                             override fun onCancelled(error: DatabaseError) {
                                 TODO("Not yet implemented")
                             }

                         })

                     Firebase.database.reference.child("PlayChance")
                         .child(Firebase.auth.currentUser!!.uid)
                         .setValue(currentChance+1)
                         .addOnSuccessListener{
                             isUpdated = true
                         }

                 }
             }
             else
             { binding.sorryView.visibility=View.VISIBLE

             }
         }


    }
}