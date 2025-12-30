package com.example.myapp013aeducationgame

import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.myapp013aeducationgame.database.GameDatabase
import com.example.myapp013aeducationgame.database.user.UserDao
import com.example.myapp013aeducationgame.database.user.Word
import com.example.myapp013aeducationgame.database.word.WordDao
import com.example.myapp013aeducationgame.databinding.ActivityGameBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var db: GameDatabase
    private lateinit var wordDao: WordDao
    private lateinit var userDao: UserDao

    private var userId: Long = 0L
    private lateinit var currentWord: Word
    private var choices: List<String> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            GameDatabase::class.java,
            "game_database"
        ).build()

        wordDao = db.wordDao()
        userDao = db.userDao()
        userId = intent.getLongExtra("userId", 0L)

        runBlocking {
            val wordsInDB = wordDao.getAllWords()
            if (wordsInDB.isEmpty()) {
                wordDao.insertInitialWords()
            }
        }

        loadNextQuestion()

        binding.btnSubmit.setOnClickListener {
            val selectedId = binding.rdBtnGrp.checkedRadioButtonId
            if (selectedId != -1) {
                val selectedBtn = findViewById<RadioButton>(selectedId)
                checkAnswer(selectedBtn.text.toString())
            } else {
                Toast.makeText(this, "Vyberte možnost", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadNextQuestion() {
        lifecycleScope.launch {
            val word = wordDao.getRandomWord()
            if (word == null) {
                Toast.makeText(this@GameActivity, "Tabulka slov je prázdná!", Toast.LENGTH_SHORT)
                    .show()
                finish()
                return@launch
            }
            currentWord = word

            val wrongWords = wordDao.getRandomWordsExcluding(currentWord.id)
            choices =
                listOf(currentWord.foreign, wrongWords[0].foreign, wrongWords[1].foreign).shuffled()

            binding.twWord.text = currentWord.czech
            binding.rdBtn1.text = choices[0]
            binding.rdBtn2.text = choices[1]
            binding.rdBtn3.text = choices[2]
            binding.rdBtnGrp.clearCheck()
            binding.twResult.text = ""
        }
    }

    private fun checkAnswer(selected: String) {
        val correct = selected == currentWord.foreign
        val points = 0
        lifecycleScope.launch {
            val user = userDao.getUserById(userId) // User?
            if (user != null) {
                val points = if (correct) user.points + 1 else user.points
                userDao.update(user.copy(points = points))
            }
        }

        binding.twResult.text =
            if (correct) "Správně!" else "Špatně! Správná odpověď: ${currentWord.foreign}, Počet bodů: $points"

        if (correct) {
            binding.rdBtnGrp.postDelayed({ loadNextQuestion() }, 1000)
        } else {
            finish()
        }
    }
}
