package com.example.nutrimamadaily

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class PageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page)

        // Timer untuk mengalihkan ke NavigationActivity setelah 3 detik
        Handler(Looper.getMainLooper()).postDelayed({
            // Intent menuju ke NavigationActivity (sebelumnya MainActivity)
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
            // Tutup aktivitas ini agar tidak kembali
            finish()
        }, 3000) // Delay selama 3 detik
    }
}
