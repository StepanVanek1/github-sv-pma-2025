package com.example.myapp013aeducationgame

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapp013aeducationgame.database.GameDatabase
import com.example.myapp013aeducationgame.database.GameDatabaseInstance
import com.example.myapp013aeducationgame.database.user.User
import com.example.myapp013aeducationgame.database.user.UserDao
import com.example.myapp013aeducationgame.databinding.ActivityMainBinding
import com.example.myapp013aeducationgame.databinding.UserScoreBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var db: GameDatabase
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = GameDatabaseInstance.getDatabase(this)
        userDao = db.userDao()

        loadUsers()

        binding.btnPlay.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            if (name.isNotEmpty()) {
                lifecycleScope.launch {
                    val user = User(name = name, points = 0)
                    val userId = userDao.insert(user)
                    startGame(user.copy(id = userId))
                }
            } else {
                Toast.makeText(this, "Zadejte jméno", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadUsers() {
        lifecycleScope.launch {
            val users = userDao.getAllUsers()
            val container = binding.llUserContainer
            container.removeAllViews()
            users.forEach { user ->
                val rowBinding = UserScoreBinding.inflate(layoutInflater, container, false)
                rowBinding.twName.text = user?.name
                rowBinding.twScore.text = user?.points.toString()
                container.addView(rowBinding.root)
            }
        }
    }

    private fun startGame(user: User) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("userId", user.id)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        binding.etName.setText("")
        loadUsers()
    }
}
