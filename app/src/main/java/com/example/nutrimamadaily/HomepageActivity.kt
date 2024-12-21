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
import de.hdodenhof.circleimageview.CircleImageView

class HomepageActivity : AppCompatActivity() {

    // Deklarasi variabel tambahan
    private lateinit var progressBar: ProgressBar
    private lateinit var progressTextView: TextView
    private lateinit var headerCaptionTextView: TextView

    // Total tahapan atau misi yang akan dilacak
  // private val TOTAL_STAGES = 5

    private val PROGRESS_LIMIT = 27.0f // Batas total zat besi sebelum reset

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Inisialisasi Progress Bar
        progressBar = findViewById(R.id.progress_bar)
        progressTextView = findViewById(R.id.progress_text)
        headerCaptionTextView = findViewById(R.id.header_image_caption)

        updateProgressUI()

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
        }

        // Set the OnClickListener for the "Tips" button
        val btnTips: Button = findViewById(R.id.btn_tips)
        btnTips.setOnClickListener {
            // Start TipsActivity when the button is clicked
            val intent = Intent(this, TipsActivity::class.java)
            startActivity(intent)
        }

        // Set the OnClickListener for the "Riwayat" button
        val btnRiwayat: Button = findViewById(R.id.btn_riwayat)
        btnRiwayat.setOnClickListener {
            // Start RiwayatActivity when the button is clicked
            val intent = Intent(this, RiwayatActivity::class.java)
            startActivity(intent)
        }

        // Set the OnClickListener for the Diary image
        val diaryImage: ImageView = findViewById(R.id.utama)
        diaryImage.setOnClickListener {
            // When clicked, open UtamaActivity
            val intent = Intent(this, FoodActivity::class.java)
            startActivity(intent)
        }

        // Tambahan untuk CircleImageView
        val profileIcon: CircleImageView = findViewById(R.id.profile_icon)
        profileIcon.setOnClickListener {
            // Ketika di klik, buka halaman ProfileActivity
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateProgressUI() {
        val sharedPref = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val currentProgress = sharedPref.getFloat("TOTAL_ZAT_BESI", 0f).toDouble()
        val currentHeaderCaption = sharedPref.getInt("HEADER_CAPTION", 0)

        if (currentProgress >= PROGRESS_LIMIT) {
            val newHeaderCaption = currentHeaderCaption + 1
            sharedPref.edit().apply {
                putFloat("TOTAL_ZAT_BESI", 0f)
                putInt("HEADER_CAPTION", newHeaderCaption)
                apply()
            }
            progressBar.progress = 0
            progressTextView.text = "0.0 mg / 27 mg "
            headerCaptionTextView.text = "$newHeaderCaption"
        } else {
            // Perhitungan yang lebih eksplisit
            val progressFloat = currentProgress.toFloat()
            val limitFloat = PROGRESS_LIMIT.toFloat()
            val percentage = (progressFloat / limitFloat * 100).toInt()
            progressBar.progress = percentage
            progressTextView.text = "%.1f mg / 27 mg".format(currentProgress)
            headerCaptionTextView.text = "$currentHeaderCaption"
        }
    }

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

    // Inner class Adapter untuk Carousel
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
