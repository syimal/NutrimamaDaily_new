package com.example.nutrimamadaily

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

class NavigationActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var skipButton: Button
    private lateinit var backButton: Button
    private lateinit var nextButton: Button
    private lateinit var dotIndicator: LinearLayout
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private var currentPage = 0 // Menyimpan posisi slide saat ini

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        // Menghubungkan view dengan layout
        viewPager = findViewById(R.id.slideViewPager)
        skipButton = findViewById(R.id.skipButton)
        backButton = findViewById(R.id.backButton)
        nextButton = findViewById(R.id.nextButton)
        dotIndicator = findViewById(R.id.dotIndicator)

        // Set up ViewPager adapter
        viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        skipButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Tombol Back untuk kembali ke slide sebelumnya
        backButton.setOnClickListener {
            if (currentPage > 0) {
                currentPage--
                viewPager.setCurrentItem(currentPage, true)
            }
        }

        // Tombol Next untuk melanjutkan ke slide berikutnya
        nextButton.setOnClickListener {
            if (currentPage < viewPagerAdapter.count - 1) {
                currentPage++
                viewPager.setCurrentItem(currentPage, true)
            } else {
                // Finish jika di halaman terakhir
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        // Memperbarui status tombol saat ViewPager dipindahkan
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Tidak digunakan dalam kasus ini
            }

            override fun onPageSelected(position: Int) {
                currentPage = position

                // Update tombol
                updateButtons()
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Tidak digunakan dalam kasus ini
            }
        })

        // Inisialisasi tombol berdasarkan posisi awal
        updateButtons()
    }

    // Update tombol back, next, dan finish
    private fun updateButtons() {
        if (currentPage == 0) {
            backButton.visibility = View.INVISIBLE
            nextButton.text = "Next"
        } else if (currentPage == viewPagerAdapter.count - 1) {
            backButton.visibility = View.VISIBLE
            nextButton.text = "Finish"
        } else {
            backButton.visibility = View.VISIBLE
            nextButton.text = "Next"
        }
    }
}
