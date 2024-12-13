package com.example.nutrimamadaily

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    //ini untuk firebaseauth
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var signupName: EditText
    private lateinit var signupEmail: EditText
    private lateinit var signupUsername: EditText
    private lateinit var signupPassword: EditText
    private lateinit var loginRedirectText: TextView
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Initialize the views
        signupName = findViewById(R.id.signup_name)
        signupEmail = findViewById(R.id.signup_email)
        signupUsername = findViewById(R.id.signup_username)
        signupPassword = findViewById(R.id.signup_password)
        loginRedirectText = findViewById(R.id.loginRedirectText)
        signupButton = findViewById(R.id.signup_button)

        // Set up the signup button
        signupButton.setOnClickListener {
            // Get input data
            val name = signupName.text.toString()
            val email = signupEmail.text.toString()
            val username = signupUsername.text.toString()
            val password = signupPassword.text.toString()

            // Check if fields are not empty
            if (name.isNotEmpty() && email.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                // Create user with Firebase Authentication
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Get the current user
                            val firebaseUser = auth.currentUser

                            // Inisialisasi Realtime Database
                            database = FirebaseDatabase.getInstance().reference

                            // Create a HelperClass object
                            val helperClass = HelperClass(name, email, username, password)

                            // Save the user data in Firebase Realtime Database
                            firebaseUser?.uid?.let { uid ->
                                database.child("users").child(uid).setValue(helperClass)
                                    .addOnCompleteListener { dbTask ->
                                        if (dbTask.isSuccessful) {
                                            Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            Toast.makeText(this, "Database error: ${dbTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill all the fields!", Toast.LENGTH_SHORT).show()
            }
        }

        // Redirect to login activity
        loginRedirectText.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}


