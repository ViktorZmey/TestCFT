package com.example.testcft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testcft.fragment.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction().replace(R.id.placeHalder, MainFragment.newInstance())
            .commit()
    }
}

