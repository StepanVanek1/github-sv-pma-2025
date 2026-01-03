package com.example.quizgame.activities

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.quizgame.R
import com.example.quizgame.database.AppDatabaseInstance
import com.example.quizgame.database.user.User
import com.example.quizgame.databinding.ActivityMainBinding
import com.example.quizgame.managers.UserManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkIfUserExists()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                    as NavHostFragment

        val navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)
    }

    private fun checkIfUserExists() {
        lifecycleScope.launch {
            val userId = UserManager.getUserId(this@MainActivity)
            val db = AppDatabaseInstance.getDatabase(this@MainActivity)

            val user = if (userId != -1L) db.userDao().getUserById(userId) else null

            if (user == null) {
                UserManager.saveUserId(this@MainActivity, -1L)
                showUserNameDialog()
            }
        }
    }

    private fun showUserNameDialog() {
        val editText = EditText(this)
        editText.hint = "Zadej své jméno"

        AlertDialog.Builder(this)
            .setTitle("Vítej v QuizGame!")
            .setMessage("Před hraním zadej své jméno:")
            .setView(editText)
            .setCancelable(false)
            .setPositiveButton("Uložit") { _, _ ->
                val name = editText.text.toString()
                if (name.isNotBlank()) {
                    saveUser(name)
                } else {
                    showUserNameDialog()
                }
            }
            .show()
    }

    private fun saveUser(name: String) {
        lifecycleScope.launch {
            val db = AppDatabaseInstance.getDatabase(this@MainActivity)
            val newUserId = db.userDao().insertUser(User(name = name))
            UserManager.saveUserId(this@MainActivity, newUserId)
        }
    }
}