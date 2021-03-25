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
        // 设置 bar 高度
        scrollBarView.setBarHeight(200f)
        // 设置 bar 颜色
        scrollBarView.setBarColor(R.color.design_default_color_primary)

    }
}