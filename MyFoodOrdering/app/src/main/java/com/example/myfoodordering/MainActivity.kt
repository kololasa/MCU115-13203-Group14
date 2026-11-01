package com.example.myfoodordering

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var layoutFinalResult: View
    private lateinit var tvFinalResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        layoutFinalResult = findViewById(R.id.layoutFinalResult)
        tvFinalResult = findViewById(R.id.tvFinalResult)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewPager.adapter = OrderPagerAdapter(this)

        val tabTitles = listOf("Main", "Side", "Drink", "Confirm")
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        findViewById<View>(R.id.btnRestart).setOnClickListener {
            // Restart the activity to clear all selections and viewmodels easily
            val intent = intent
            finish()
            startActivity(intent)
        }
    }

    fun showFinalResult(order: Order) {
        findViewById<View>(R.id.appBarLayout).visibility = View.GONE
        viewPager.visibility = View.GONE
        layoutFinalResult.visibility = View.VISIBLE
        
        val summary = "Main: ${order.mainMeal}\n\n" +
                      "Sides: ${order.sideDishes.joinToString(", ")}\n\n" +
                      "Drink: ${order.drink}"
        tvFinalResult.text = summary
    }
}