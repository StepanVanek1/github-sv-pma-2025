package com.example.myapp009bimagetoapp

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp009bimagetoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGray.visibility = android.view.View.GONE
        binding.btnSepia.visibility = android.view.View.GONE
        binding.btnRemoveImage.visibility = android.view.View.GONE

        val getContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                if (uri != null) {
                    binding.ivImage.setImageURI(uri)

                    binding.btnGray.visibility = android.view.View.VISIBLE
                    binding.btnSepia.visibility = android.view.View.VISIBLE
                    binding.btnRemoveImage.visibility = android.view.View.VISIBLE
                    binding.btnGetImage.visibility = android.view.View.GONE

                    binding.ivImage.clearColorFilter()
                }
            }

        binding.btnGetImage.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.btnRemoveImage.setOnClickListener {
            binding.ivImage.setImageURI(null)
            binding.ivImage.clearColorFilter()

            binding.btnGray.visibility = android.view.View.GONE
            binding.btnSepia.visibility = android.view.View.GONE
            binding.btnRemoveImage.visibility = android.view.View.GONE
            binding.btnGetImage.visibility = android.view.View.VISIBLE
        }

        binding.btnGray.setOnClickListener {
            val matrix = ColorMatrix()
            matrix.setSaturation(0f)
            binding.ivImage.colorFilter = ColorMatrixColorFilter(matrix)
        }

        binding.btnSepia.setOnClickListener {
            val matrix = ColorMatrix()
            matrix.setScale(1f, 0.9f, 0.6f, 1f)
            binding.ivImage.colorFilter = ColorMatrixColorFilter(matrix)
        }
    }
}