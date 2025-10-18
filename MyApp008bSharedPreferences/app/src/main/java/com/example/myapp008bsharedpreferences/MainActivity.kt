package com.example.myapp008bsharedpreferences

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp008bsharedpreferences.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("pref", MODE_PRIVATE)
        val editor = sharedPref.edit()

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val ageText = binding.etAge.text.toString()
            val isAdult = binding.cbIsAdult.isChecked

            if (ageText.isBlank()) {
                Toast.makeText(this, "Vyplňte věk", Toast.LENGTH_LONG).show()
            } else {
                val age = ageText.toInt()
                val expectedIsAdult = age >= 18
                if (isAdult != expectedIsAdult) {
                    Toast.makeText(this, "Vyplňte správný věk", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Ukládám", Toast.LENGTH_LONG).show()
                    editor.apply {
                        putString("name", name)
                        putInt("age", age)
                        putBoolean("isAdult", isAdult)
                        apply()

                        binding.etName.setText("")
                        binding.etAge.setText("")
                        binding.cbIsAdult.isChecked = false
                    }
                }
            }
        }

        binding.btnLoad.setOnClickListener {
            val name = sharedPref.getString("name", "")
            val age = sharedPref.getInt("age", 0)
            val isAdult = sharedPref.getBoolean("isAdult", false)

            binding.etName.setText(name)
            binding.etAge.setText(age.toString())
            binding.cbIsAdult.isChecked = isAdult

            Toast.makeText(this, "Data načtena", Toast.LENGTH_LONG).show()
        }
    }
}