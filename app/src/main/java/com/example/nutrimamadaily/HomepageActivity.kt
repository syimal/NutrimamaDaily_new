package com.example.nutrimamadaily

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class HomepageActivity : AppCompatActivity() {

    // Deklarasi variabel tambahan
    private lateinit var progressBar: ProgressBar
    private lateinit var progressTextView: TextView

    // Total tahapan atau misi yang akan dilacak
    private val TOTAL_STAGES = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Inisialisasi Progress Bar
        progressBar = findViewById(R.id.progress_bar)
        progressTextView = findViewById(R.id.progress_text)

        // Setup Progress Bar
        setupProgressBar()

        val carouselImages = listOf(
            R.drawable.ibu_susu,
            R.drawable.ananda,
            R.drawable.maxresdefault
        )

        val viewPager: ViewPager2 = findViewById(R.id.image_carousel)
        val indicators: LinearLayout = findViewById(R.id.indicator_views)

        // Set Adapter
        viewPager.adapter = ImageCarouselAdapter(carouselImages)

        // Setup indicators
        setupIndicators(indicators, carouselImages.size)

        // Listen to page changes
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateIndicators(indicators, position)
            }
        })

        // Set the OnClickListener for the "For Your Education" button
        val btnFye: Button = findViewById(R.id.btn_fye)
        btnFye.setOnClickListener {
            // Start FyeActivity when the button is clicked
            val intent = Intent(this, FyeActivity::class.java)
            startActivity(intent)

            // Update progress untuk milestone "Edukasi"
            updateProgressForMilestone("Edukasi")
        }

        // Set the OnClickListener for the "Tips" button
        val btnTips: Button = findViewById(R.id.btn_tips)
        btnTips.setOnClickListener {
            // Start TipsActivity when the button is clicked
            val intent = Intent(this, TipsActivity::class.java)
            startActivity(intent)

            // Update progress untuk milestone "Tips"
            updateProgressForMilestone("Tips")
        }

        // Set the OnClickListener for the "Riwayat" button
        val btnRiwayat: Button = findViewById(R.id.btn_riwayat)
        btnRiwayat.setOnClickListener {
            // Start RiwayatActivity when the button is clicked
            val intent = Intent(this, RiwayatActivity::class.java)
            startActivity(intent)

            // Update progress untuk milestone "Riwayat"
            updateProgressForMilestone("Riwayat")
        }

        // Set the OnClickListener for the Diary image
        val diaryImage: ImageView = findViewById(R.id.utama)
        diaryImage.setOnClickListener {
            // When clicked, open UtamaActivity
            val intent = Intent(this, UtamaActivity::class.java)
            startActivity(intent)

            // Update progress untuk milestone "Diary"
            updateProgressForMilestone("Diary")
        }
    }

    // Metode untuk setup progress bar
    private fun setupProgressBar() {
        // Atur maksimum progress bar
        progressBar.max = 100

        // Inisialisasi progress awal (bisa diambil dari penyimpanan atau default 0)
        updateProgress(getInitialProgress())
    }

    // Metode untuk mendapatkan progress awal
    private fun getInitialProgress(): Int {
        // Implementasi untuk mendapatkan progress dari penyimpanan
        // Misalnya dari SharedPreferences atau Database
        // Sementara ini dikembalikan 0
        return 0
    }

    // Metode untuk memperbarui progress
    private fun updateProgress(completedStages: Int) {
        // Hitung persentase progress
        val progressPercentage = kotlin.math.min(
            (completedStages.toFloat() / TOTAL_STAGES * 100).toInt(),
            100
        )

        // Update progress bar
        progressBar.progress = progressPercentage

        // Update teks progress
        progressTextView.text = "$progressPercentage%"

        // Simpan progress (opsional)
        saveProgress(completedStages)
    }

    // Variabel untuk melacak milestone yang sudah diselesaikan
    private val completedMilestones = mutableSetOf<String>()

    // Metode untuk update progress berdasarkan milestone
    private fun updateProgressForMilestone(milestone: String) {
        // Tambahkan milestone ke set yang sudah diselesaikan
        completedMilestones.add(milestone)

        // Update progress berdasarkan jumlah milestone yang diselesaikan
        updateProgress(completedMilestones.size)
    }

    // Metode untuk menyimpan progress
    private fun saveProgress(progress: Int) {
        // Implementasi untuk menyimpan progress
        // Misalnya menggunakan SharedPreferences
        val sharedPref = getPreferences(MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("APP_PROGRESS", progress)
            apply()
        }
    }

    // Metode dari implementasi sebelumnya
    private fun setupIndicators(indicators: LinearLayout, count: Int) {
        indicators.removeAllViews()
        for (i in 0 until count) {
            val indicator = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(16, 16).apply {
                    marginEnd = 8
                }
                setBackgroundResource(if (i == 0) R.drawable.square_red else R.drawable.square_grey)
            }
            indicators.addView(indicator)
        }
    }

    private fun updateIndicators(indicators: LinearLayout, position: Int) {
        for (i in 0 until indicators.childCount) {
            indicators.getChildAt(i).setBackgroundResource(
                if (i == position) R.drawable.square_red else R.drawable.square_grey
            )
        }
    }

    // Inner class Adapter untuk Carousel (tetap sama seperti sebelumnya)
    inner class ImageCarouselAdapter(private val images: List<Int>) :
        RecyclerView.Adapter<ImageCarouselAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val imageView: ImageView = view.findViewById(R.id.carousel_image)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_carousel_image, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.imageView.setImageResource(images[position])
        }

        override fun getItemCount(): Int = images.size
    }
}