package com.adilmulimani.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.adilmulimani.quizapp.databinding.ActivityMainBinding
import com.adilmulimani.quizapp.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signUpBButton.setOnClickListener {
              if( binding.nameEditText.text.toString()==""
                ||binding.ageEditText.text.toString()==""
                ||binding.EmailIdEditText.text.toString()==""
                ||binding.PasswordEditText.text.toString()=="")
            {
                Toast.makeText(this,"Please Fill All the details",Toast.LENGTH_LONG).show()
            }
            else
            { Firebase.
            auth.createUserWithEmailAndPassword(binding.EmailIdEditText.text.toString(),
                binding.PasswordEditText.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        var user = User(
                            binding.nameEditText.text.toString(),
                            binding.ageEditText.text.toString().toInt(),
                            binding.EmailIdEditText.text.toString(),
                            binding.PasswordEditText.text.toString()
                           )

                        Firebase.database.reference.child("Users")
                                .child(Firebase.auth.currentUser!!.uid).push().setValue(user)
                            .addOnSuccessListener {
                                startActivity(Intent(this,HomeActivity::class.java))
                                finish()
                            }

                    }

                    else
                    {
                        Toast.makeText(this, it.exception?.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
    }
    //if user already Exists
    override fun onStart() {
        super.onStart()
        if(Firebase.auth.currentUser!=null)
        {
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
    }
}