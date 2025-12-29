package com.example.myapp004moreactivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp004moreactivities.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nickname = intent.getStringExtra("NICK_NAME")
        binding.twInfo.text = "Data z první aktivity. Přezdívka: $nickname"

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnThirdAct.setOnClickListener {
            val surname = binding.etSurname.text.toString()
            val intent = Intent(this, ThirdActivity::class.java)
            intent.putExtra("SURNAME", surname)
            startActivity(intent)
        }
    }
}