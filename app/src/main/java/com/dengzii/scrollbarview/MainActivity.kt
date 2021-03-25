package com.dengzii.scrollbarview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ScrollView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val scrollBarView = findViewById<ScrollBarView>(R.id.scrollbar)
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        scrollBarView.setWithScrollView(scrollView)
    }
}