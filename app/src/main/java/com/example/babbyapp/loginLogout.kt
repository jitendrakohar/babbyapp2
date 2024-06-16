package com.example.babbyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import com.example.babbyapp.databinding.ActivityLoginLogoutBinding
import com.example.yourapp.LoginManager
import com.google.firebase.auth.FirebaseAuth

class loginLogout : AppCompatActivity() {
    private lateinit var binding:ActivityLoginLogoutBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.buttonToggle.setOnClickListener {
            Toast.makeText(this, "Toggle", Toast.LENGTH_SHORT).show()
            binding.loginForm.isVisible = false
            binding.signUpForm.isVisible = true
        }
        binding.toogleLogin.setOnClickListener {
            Toast.makeText(this, "Toggle", Toast.LENGTH_SHORT).show()
            binding.loginForm.isVisible = true
            binding.signUpForm.isVisible = false
        }

        binding. buttonLogin.setOnClickListener {
            val email = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signIn(email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonSignUp.setOnClickListener {
            val email = binding.editTextNewUsername.text.toString()
            val password = binding.editTextNewPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                if(password==binding.editTextConfirmPassword.text.toString()){
                    signUp(email, password)}
                else{
                    binding.editTextConfirmPassword.error="Passwords do not match"
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun signIn(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    LoginManager.setLoginStatus(this@loginLogout, true);
                    LoginManager.saveUsername(this@loginLogout, email)
                   val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    LoginManager.setLoginStatus(this@loginLogout, true);
                    LoginManager.saveUsername(this@loginLogout, email)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Sign up successful ", Toast.LENGTH_SHORT).show()
                } else {
                    // If sign up fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}