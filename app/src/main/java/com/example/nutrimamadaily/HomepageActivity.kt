package com.example.nutrimamadaily

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class HomepageActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var progressTextView: TextView
    private lateinit var btnResetProgress: Button
    private lateinit var db: FirebaseFirestore

    private lateinit var sharedPreferences: SharedPreferences

    private val TOTAL_STAGES = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        db = FirebaseFirestore.getInstance()

        progressBar = findViewById(R.id.progress_bar)
        progressTextView = findViewById(R.id.progress_text)
        btnResetProgress = findViewById(R.id.btn_reset_progress)

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        setupProgressBar()
        setupResetButton()

        val carouselImages = listOf(
            R.drawable.ibu_susu,
            R.drawable.ananda,
            R.drawable.maxresdefault
        )
        val viewPager: ViewPager2 = findViewById(R.id.image_carousel)
        val indicators: LinearLayout = findViewById(R.id.indicator_views)

        viewPager.adapter = ImageCarouselAdapter(carouselImages)
        setupIndicators(indicators, carouselImages.size)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateIndicators(indicators, position)
            }
        })

        setupButtons()

        // Menangani intent untuk memperbarui progres
        val updateProgress = intent.getBooleanExtra("update_progress", false)
        if (updateProgress) {
            val currentProgress = sharedPreferences.getInt("healthy_progress", 0)
            val newProgress = currentProgress + 100 // Menambah 1%
            saveProgress(newProgress)
            updateProgress()
        }
    }

    private fun setupProgressBar() {
        progressBar.max = 100
        val savedProgress = sharedPreferences.getInt("healthy_progress", 0)
        updateProgress()
    }

    private fun setupResetButton() {
        btnResetProgress.setOnClickListener {
            resetProgress()
        }
    }

    private fun resetProgress() {
        val editor = sharedPreferences.edit()
        editor.putInt("healthy_progress", 0)
        editor.apply()

        updateProgress()

        Toast.makeText(this, "Progress telah direset ke 0%", Toast.LENGTH_SHORT).show()
    }

    private fun saveProgress(progress: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("healthy_progress", progress)
        editor.apply()
    }

    private fun updateProgress() {
        countHealthyItems()
    }

    private fun countHealthyItems() {
        db.collection("riwayat")
            .whereEqualTo("category", "Healthy") // Filter by category
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                val count = querySnapshot.size() // Number of documents that match
                Log.d("Firestore", "Count of Healthy items: $count")

                // Update progress bar and text view with the count
                val progressPercentage = count
                progressBar.progress = progressPercentage
                progressTextView.text = "$progressPercentage%"
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching data", exception)
            }
    }


    private fun setupButtons() {
        val btnFye: Button = findViewById(R.id.btn_fye)
        val btnTips: Button = findViewById(R.id.btn_tips)
        val btnRiwayat: Button = findViewById(R.id.btn_riwayat)
        val diaryImage: ImageView = findViewById(R.id.utama)

        btnFye.setOnClickListener {
            navigateToActivity(FyeActivity::class.java)
        }
        btnTips.setOnClickListener {
            navigateToActivity(TipsActivity::class.java)
        }
        btnRiwayat.setOnClickListener {
            navigateToActivity(RiwayatActivity::class.java)
        }
        diaryImage.setOnClickListener {
            navigateToActivity(UtamaActivity::class.java)
        }
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        startActivity(Intent(this, activityClass))
    }

    private fun setupIndicators(indicators: LinearLayout, count: Int) {
        indicators.removeAllViews()
        for (i in 0 until count) {
            val indicator = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(16, 16).apply { marginEnd = 8 }
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
