package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.capstone.databinding.ActivitySignupPasswordBinding
import com.example.capstone.model.UserModel
import com.example.capstone.util.UiUtil
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class SignupPasswordActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignupPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.passwordContinueButton.setOnClickListener {
            signup()
        }
    }

    fun setInProgress(inProgress : Boolean) {
        if (inProgress) {
            binding.progressBar.visibility = View.VISIBLE
            binding.passwordContinueButton.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.passwordContinueButton.visibility = View.VISIBLE
        }
    }

    fun signup() {
        val email = if (intent != null)
        intent.extras?.getString("email_input") ?: "default_email"
        else
            "null_value"
        val password = binding.passwordInput.text.toString()
        val confirmPassword = binding.confirmPasswordInput.text.toString()

        if (password.length < 8) {
            binding.passwordInput.setError("Minimum of 8 characters")
            return
        }
        if (password != confirmPassword) {
            binding.confirmPasswordInput.setError("Password does not matched")
            return
        }

        // IF EMAIL AND PASS IS GOOD, FIRE IT UP TO FIREBASE's ASS
        signupWithFirebase(email, password)
    }

    fun signupWithFirebase(email : String, password : String) {
        val title = if (intent != null)
            intent.extras?.getString("title") ?: "default_email"
        else
            "null_value"
        val firstName = if (intent != null)
            intent.extras?.getString("first_name") ?: "default_last_name"
        else
            "null_value"
        val lastName = if (intent != null)
            intent.extras?.getString("last_name") ?: "default_first_name"
        else
            "null_value"

        setInProgress(true)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            email, password
        ).addOnSuccessListener {
            it.user?.let { user->
//                ADD MORE DATA INFO HERE (NATITIRA: PROFILE PIC, CIRCLE)
                val userModel = UserModel(user.uid, email, title, firstName, lastName)

                Firebase.firestore.collection("user")
                    .document(user.uid)
                    .set(userModel)
                    .addOnSuccessListener {
                        UiUtil.showToast(applicationContext, "Account created successfully")
                        setInProgress(false)
                        startActivity(Intent(this, CircleWelcomeActivity::class.java))
                        finish()
                    }
            }
        }.addOnFailureListener {
            UiUtil.showToast(applicationContext, it.localizedMessage?: "Something went wrong")
            setInProgress(false)
        }
    }
}