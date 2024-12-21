package com.example.nutrimamadaily

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()

        // Fetch data and populate fields
        fetchUserData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Log Out Button
        val buttonLogout: Button = findViewById(R.id.buttonSave)
        buttonLogout.setOnClickListener {
            auth.signOut() // Sign out user from Firebase Authentication
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchUserData() {
        // Get current user
        val user = auth.currentUser
        if (user != null) {
           // val nameField: EditText = findViewById(R.id.editTextName)
            val emailField: EditText = findViewById(R.id.editTextEmail)

            // Set user's name and email
            //nameField.setText(user.displayName ?: "Nama Tidak Tersedia")
            emailField.setText(user.email ?: "Email Tidak Tersedia")
        }
    }
}
