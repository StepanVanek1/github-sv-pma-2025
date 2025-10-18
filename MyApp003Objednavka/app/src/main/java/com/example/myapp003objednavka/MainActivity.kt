package com.example.myapp003objednavka

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp003objednavka.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        title = "Kola"

        binding.rdGrp.setOnCheckedChangeListener { _, _ ->
            val selectedImage = when (binding.rdGrp.checkedRadioButtonId) {
                R.id.rdBtnCarOne -> R.drawable.rapid
                R.id.rdBtnCarTwo -> R.drawable.octavia
                R.id.rdBtnCarThree -> R.drawable.fabia
                else -> R.drawable.rapid
            }
            binding.imgView.setImageResource(selectedImage)
        }

        binding.btnOrder.setOnClickListener {
            val checkedId = binding.rdGrp.checkedRadioButtonId

            val car = findViewById<RadioButton>(checkedId)
            val seats = binding.cbSeats.isChecked
            val rugs = binding.cbRugs.isChecked
            val steeringWheel = binding.cbHeatedSteeringWheel.isChecked

            binding.txtVwOrder.text =
                "Souhrn objednávky:\n" +
                        "${car.text}\n" +
                        "• Lepší sedadla: ${if (seats) "✅" else "❌"}\n" +
                        "• Koberečky: ${if (rugs) "✅" else "❌"}\n" +
                        "• Vyhřívaný volant: ${if (steeringWheel) "✅" else "❌"}"
        }
    }
}