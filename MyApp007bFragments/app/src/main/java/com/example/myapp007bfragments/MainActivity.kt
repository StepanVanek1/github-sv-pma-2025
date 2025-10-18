package com.example.myapp007bfragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapp007bfragments.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOk.setOnClickListener {
            replaceFragment(FragmentOk())
        }

        binding.btnNeutral.setOnClickListener {
            replaceFragment(FragmentNeutral())
        }

        binding.btnAngry.setOnClickListener {
            replaceFragment(FragmentAngry())
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentMng = supportFragmentManager
        val fragmentTransaction = fragmentMng.beginTransaction()

        fragmentTransaction.replace(R.id.fragContainer, fragment)
        fragmentTransaction.commit()
    }
}
