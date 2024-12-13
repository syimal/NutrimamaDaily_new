package com.example.nutrimamadaily

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter

class ViewPagerAdapter(var context: Context) : PagerAdapter() {

    // Gambar yang digunakan untuk setiap slide
    var sliderAllImages: IntArray = intArrayOf(
        R.drawable.ibu_ibu,   // Gambar untuk slide pertama
        R.drawable.ibu_ibu,  // Gambar untuk slide kedua
        R.drawable.ibu_ibu     // Gambar untuk slide ketiga
    )

    // Judul untuk setiap slide
    var sliderAllTitle: IntArray = intArrayOf(
        R.string.screen1,   // Judul untuk slide pertama
        R.string.screen2,   // Judul untuk slide kedua
        R.string.screen3    // Judul untuk slide ketiga
    )

    // Deskripsi untuk setiap slide
    var sliderAllDesc: IntArray = intArrayOf(
        R.string.screen1desc,   // Deskripsi untuk slide pertama
        R.string.screen2desc,   // Deskripsi untuk slide kedua
        R.string.screen3desc    // Deskripsi untuk slide ketiga
    )

    override fun getCount(): Int {
        return sliderAllTitle.size  // Mengembalikan jumlah slide
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout  // Memeriksa apakah view yang diberikan adalah objek yang sesuai
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // Mengambil LayoutInflater untuk menginflate layout slider_screen.xml
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.slider_screen, container, false)

        // Menemukan elemen-elemen di dalam layout yang telah diinflate
        val sliderImage = view.findViewById<ImageView>(R.id.sliderImage)
        val sliderTitle = view.findViewById<TextView>(R.id.sliderTitle)
        val sliderDesc = view.findViewById<TextView>(R.id.sliderDesc)

        // Mengatur gambar, judul, dan deskripsi untuk setiap slide
        sliderImage.setImageResource(sliderAllImages[position])
        sliderTitle.setText(sliderAllTitle[position])
        sliderDesc.setText(sliderAllDesc[position])

        // Menambahkan view ke dalam container
        container.addView(view)
        return view  // Mengembalikan view yang baru dibuat
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // Menghapus view dari container saat tidak dibutuhkan lagi
        container.removeView(`object` as View)
    }
}
