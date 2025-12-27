package com.example.myapp011

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp011.databinding.ActivityDetailBinding
import androidx.core.net.toUri

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val surname = intent.getStringExtra("surname")
        val age = intent.getStringExtra("age")
        val gender = intent.getStringExtra("gender")
        val imageUriString = intent.getStringExtra("imageUri")

        binding.tvDetailInfo.text = """
            Jméno: $name
            Příjmení: $surname
            Pohlaví: $gender
            Věk: $age
        """.trimIndent()

        imageUriString?.let {
            binding.ivDetailImage.setImageURI(it.toUri())
        }

        binding.btnReturn.setOnClickListener {
            finish()
        }
    }
}
