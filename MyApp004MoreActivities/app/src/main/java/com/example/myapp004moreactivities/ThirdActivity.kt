package com.example.myapp004moreactivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp004moreactivities.databinding.ActivityThirdBinding

class ThirdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThirdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val surname = intent.getStringExtra("SURNAME")
        binding.twInfo.text = "Data z druhé aktivity. Příjmení: $surname"

        binding.btnClose.setOnClickListener {
            finish()
        }
    }
}