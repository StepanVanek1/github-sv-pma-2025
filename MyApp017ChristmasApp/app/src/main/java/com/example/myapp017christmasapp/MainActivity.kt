package com.example.myapp017christmasapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapp017christmasapp.data.UserPreferencesRepository
import com.example.myapp017christmasapp.databinding.ActivityMainBinding
import com.example.myapp017christmasapp.databinding.FragmentHomeBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var bindingHome: FragmentHomeBinding
    private lateinit var repo: UserPreferencesRepository
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingHome = FragmentHomeBinding.inflate(layoutInflater)
        setContentView(bindingHome.root)
        repo = UserPreferencesRepository(this)
        setContentView(R.layout.activity_main)

        val navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.navController

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)

        setupActionBarWithNavController(navController)

        findViewById<BottomNavigationView>(R.id.bottomNav)
            .setupWithNavController(navController)

        findViewById<NavigationView>(R.id.drawerNav)
            .setupWithNavController(navController)
        lifecycleScope.launch {
            repo.darkModeFlow.collectLatest { value ->
                bindingHome.switchDarkMode.isChecked = value
                bindingHome.textPreview.setTextColor(
                    if (value) android.graphics.Color.WHITE else android.graphics.Color.BLACK
                )
                bindingHome.root.setBackgroundColor(
                    if (value) android.graphics.Color.DKGRAY else android.graphics.Color.WHITE
                )
            }
        }

        // 2) Username
        lifecycleScope.launch {
            repo.usernameFlow.collectLatest { value ->
                bindingHome.editUsername.setText(value)
                bindingHome.textPreview.text = value.ifEmpty { "Ukázkový text" }
            }
        }

        // 3) Font size
        lifecycleScope.launch {
            repo.fontSizeFlow.collectLatest { value ->
                bindingHome.seekFontSize.progress = value
                bindingHome.textFontSizeValue.text = "Velikost: $value"
                bindingHome.textPreview.textSize = value.toFloat()
            }
        }


        // ---------------------------------------
        // REAKCE NA UI A ULOŽENÍ DO DATASTORE
        // ---------------------------------------

        // 1) Přepnutí dark mode
        bindingHome.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                repo.setDarkMode(isChecked)
            }
        }

        // 2) Uložení username po kliknutí na tlačítko
        bindingHome.buttonSaveUsername.setOnClickListener {
            val name = bindingHome.editUsername.text.toString()
            lifecycleScope.launch {
                repo.setUsername(name)
            }
        }

        // 3) Změna velikosti fontu
        bindingHome.seekFontSize.setOnSeekBarChangeListener(
            object : android.widget.SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: android.widget.SeekBar?,
                    value: Int,
                    fromUser: Boolean
                ) {
                    bindingHome.textFontSizeValue.text = "Velikost: $value"
                }

                override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {
                    seekBar?.let {
                        lifecycleScope.launch {
                            repo.setFontSize(it.progress)
                        }
                    }
                }
            }
        )
    }
}
