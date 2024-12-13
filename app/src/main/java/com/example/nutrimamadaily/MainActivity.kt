package com.example.nutrimamadaily

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    // Deklarasi variabel untuk views
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var registerText: TextView
    private lateinit var forgotPassword: TextView
    private lateinit var sapaanText: TextView
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi views
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_btn)
        registerText = findViewById(R.id.register_text)
        forgotPassword = findViewById(R.id.forgot_password)
        sapaanText = findViewById(R.id.sapaan)

        // inisialisasi Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Ambil data dari intent (jika ada)
        val intent = intent
        val name = intent.getStringExtra("name")

        // Update sapaan di UI
        if (!name.isNullOrEmpty()) {
            sapaanText.text = "Hello, $name! Selamat Datang!"
        } else {
            sapaanText.text = "Hello, Selamat Datang!"
        }

        // Aksi tombol Login
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password wajib diisi!", Toast.LENGTH_SHORT).show()
            } else {
                // Panggil AsyncTask untuk proses login
                LoginTask().execute(email, password)
            }
        }

        // Navigasi ke halaman Signup
        registerText.setOnClickListener {
            try {
                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Aksi untuk lupa password
        forgotPassword.setOnClickListener {
            Toast.makeText(this, "Fitur lupa password belum diimplementasikan", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // AsyncTask untuk login
    private inner class LoginTask : AsyncTask<String, Void, Boolean>() {
        private var errorMessage: String? = null

        override fun doInBackground(vararg params: String?): Boolean {
            val email = params[0] ?: return false
            val password = params[1] ?: return false

            var isSuccess = false

            // Gunakan Firebase Authentication untuk login
            val latch =
                java.util.concurrent.CountDownLatch(1) // Untuk menunggu operasi Firebase selesai

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        isSuccess = true
                    } else {
                        errorMessage = task.exception?.message
                    }
                    latch.countDown() // Kurangi latch agar tidak macet
                }

            latch.await() // Tunggu hingga Firebase Authentication selesai
            return isSuccess
        }

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            if (result) {
                Toast.makeText(this@MainActivity, "Login sukses!", Toast.LENGTH_SHORT).show()
                // Pindah ke HomepageActivity
                val intent = Intent(this@MainActivity, HomepageActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Login gagal! ${errorMessage ?: "Periksa email dan password."}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}




