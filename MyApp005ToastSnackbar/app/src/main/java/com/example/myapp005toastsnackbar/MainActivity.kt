package com.example.myapp005toastsnackbar

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp005toastsnackbar.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import androidx.core.graphics.toColorInt
import com.example.myapp005toastsnackbar.databinding.CustomToastBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnShowToast.setOnClickListener {
            val toastBinding = CustomToastBinding.inflate(layoutInflater)

            toastBinding.toastText.text = "toast s michalem"
            toastBinding.toastIcon.setImageResource(R.drawable.michal)

            val toast = Toast(this)
            toast.duration = Toast.LENGTH_LONG
            toast.view = toastBinding.root
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            toast.show()
        }

        binding.btnShowSnackbar.setOnClickListener {
            Snackbar.make(binding.root, "snackbar test", Snackbar.LENGTH_LONG)
                .setBackgroundTint("#FFCC50".toColorInt())
                .setTextColor(Color.CYAN)
                .setDuration(7000)
                .setActionTextColor(Color.YELLOW)
                .setAction("Zavřít") {
                    Toast.makeText(this, "snackbar -", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
    }
}