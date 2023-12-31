package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.capstone.databinding.ActivitySignupBinding
import com.example.capstone.util.UiUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupEmailActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.emailContinueButton.setOnClickListener {
            signup()
        }


    }
    fun signup() {
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
        val email = binding.emailInput.text.toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInput.setError("Email not valid")
            return
        }
//        TODO WORKS BUT NOT REALLY WORKING
//        var wew = FirebaseAuth.getInstance().uid
//        if (wew != null) {
//            FirebaseFirestore.getInstance().collection("users").document(wew).get()
//                .addOnCompleteListener {task ->
//                    if (task.getResult().exists()) {
//                        UiUtil.showToast(applicationContext, "EXISTS")
//                        binding.emailInput.setError("exist")
//
//                    } else {
//                        UiUtil.showToast(applicationContext, "NOT EXIST")
//                        binding.emailInput.setError("not exist")
//
//                    }
//                }
//        }
        val intent = Intent(this, SignupPasswordActivity::class.java)
        intent.putExtra("email_input", email)
        intent.putExtra("title", title)
        intent.putExtra("first_name", firstName)
        intent.putExtra("last_name", lastName)
        startActivity(intent)
    }
}