package com.example.myapp10

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp10.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var selectedImageUri: Uri? = null
    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                selectedImageUri = result.data?.data
                binding.ivImage.setImageURI(selectedImageUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnUploadImg.setOnClickListener {
            openImagePicker()
        }

        binding.btnRndmAge.setOnClickListener {
            val randomAge = Random.nextInt(1, 100)
            binding.etAge.setText(randomAge.toString())
        }

        binding.btnSave.setOnClickListener {
            saveForm()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePicker.launch(intent)
    }

    private fun saveForm() {
        val name = binding.etName.text.toString().trim()
        val surname = binding.etSurname.text.toString().trim()
        val age = binding.etAge.text.toString().trim()
        val selectedGenderId = binding.radioGroupOptions.checkedRadioButtonId
        val gender = findViewById<RadioButton>(selectedGenderId)?.text ?: "Neznámé"

        if (name.isEmpty() || surname.isEmpty() || age.isEmpty()) {
            Toast.makeText(this, "Vyplňte všechna pole", Toast.LENGTH_SHORT).show()
        } else {
            val itemLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
            }

            val imageView = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(100, 100)
                setImageURI(selectedImageUri)
            }

            val textView = TextView(this).apply {
                text = "Jméno: $name\nPříjmení: $surname\nPohlaví: $gender\nVěk: $age"
                setPadding(0, 0, 0, 100)
            }

            val detailButton = Button(this).apply {
                text = "Detail"
                setOnClickListener {
                    val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                        putExtra("name", name)
                        putExtra("surname", surname)
                        putExtra("age", age)
                        putExtra("gender", gender)
                        putExtra("imageUri", selectedImageUri?.toString())
                    }
                    startActivity(intent)
                }
            }

            itemLayout.addView(imageView)
            itemLayout.addView(textView)
            itemLayout.addView(detailButton)

            binding.main.addView(itemLayout)

            Snackbar.make(binding.root, "Formulář byl úspěšně uložen", Snackbar.LENGTH_SHORT).show()
            clearForm()
        }
    }

    private fun clearForm() {
        binding.ivImage.setImageDrawable(null)
        selectedImageUri = null
        binding.etName.text.clear()
        binding.etSurname.text.clear()
        binding.etAge.text.clear()
        binding.radioGroupOptions.check(R.id.rdBtnMale)
    }
}
