package com.example.quizgame.activities

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.quizgame.database.AppDatabaseInstance
import com.example.quizgame.database.game.Game
import com.example.quizgame.database.question.Question
import com.example.quizgame.databinding.ActivityQuestionBinding
import com.example.quizgame.utils.UserManager
import kotlinx.coroutines.launch

class QuestionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionBinding
    private var questions: List<Question> = emptyList()
    private var currentIndex = 0
    private var score = 0
    private var quizId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizId = intent.getLongExtra("QUIZ_ID", -1)

        lifecycleScope.launch {
            val db = AppDatabaseInstance.getDatabase(this@QuestionActivity)
            questions = db.questionDao().getAllQuizQuestions(quizId) ?: emptyList()

            if (questions.isNotEmpty()) {
                showQuestion()
            } else {
                Toast.makeText(this@QuestionActivity, "Kvíz nemá žádné otázky!", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }

        binding.btnSubmit.setOnClickListener {
            if (!validateInput()) {
                Toast.makeText(this, "Zadej nebo vyber odpověď", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            checkAnswer()

            if (currentIndex < questions.size - 1) {
                currentIndex++
                showQuestion()
            } else {
                saveGameAndFinish()
            }
        }
    }

    private fun showQuestion() {
        val q = questions[currentIndex]

        binding.twQuestion.text = q.question
        binding.etAnswer.text?.clear()
        binding.rdBtnGrp.clearCheck()

        if (q.multipleChoice) {
            prepareMultipleChoiceData(q)
        } else {
            setupTextInputUI()
        }
    }

    private fun prepareMultipleChoiceData(question: Question) {
        lifecycleScope.launch {
            val db = AppDatabaseInstance.getDatabase(this@QuestionActivity)
            val correctAnswer = question.correctAnswer
            val wrongAnswers = db.questionDao().getRandomWrongAnswersGlobal(
                excludeQuestionId = question.id
            )

            val allOptions = mutableListOf(correctAnswer)
            allOptions.addAll(wrongAnswers)

            while (allOptions.size < 3) {
                allOptions.add("Banan")
            }

            allOptions.shuffle()

            setupMultipleChoiceUI(allOptions)
        }
    }

    private fun setupMultipleChoiceUI(options: List<String>) {
        binding.etAnswer.visibility = View.GONE
        binding.rdBtnGrp.visibility = View.VISIBLE

        val radioButtons = listOf(binding.rdBtn1, binding.rdBtn2, binding.rdBtn3)

        radioButtons.forEachIndexed { index, radioButton ->
            if (index < options.size) {
                radioButton.text = options[index]
                radioButton.visibility = View.VISIBLE
            }
        }
    }

    private fun setupTextInputUI() {
        binding.etAnswer.visibility = View.VISIBLE
        binding.rdBtnGrp.visibility = View.GONE
    }

    private fun validateInput(): Boolean {
        val q = questions[currentIndex]
        return if (q.multipleChoice) {
            binding.rdBtnGrp.checkedRadioButtonId != -1
        } else {
            binding.etAnswer.text?.isNotBlank() == true
        }
    }

    private fun checkAnswer() {
        val q = questions[currentIndex]
        val userAnswer: String

        if (q.multipleChoice) {
            val selectedId = binding.rdBtnGrp.checkedRadioButtonId
            val selectedRb = findViewById<RadioButton>(selectedId)
            userAnswer = selectedRb.text.toString()
        } else {
            userAnswer = binding.etAnswer.text.toString()
        }

        if (userAnswer.trim().equals(q.correctAnswer.trim(), ignoreCase = true)) {
            score++
            Toast.makeText(this, "Správně!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Chyba! Správně bylo: ${q.correctAnswer}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun saveGameAndFinish() {
        lifecycleScope.launch {
            val db = AppDatabaseInstance.getDatabase(this@QuestionActivity)
            val userId = UserManager.getUserId(this@QuestionActivity)

            db.gameDao().insertGame(
                Game(
                    quizId = quizId,
                    playerId = userId,
                    score = score,
                    createdAt = System.currentTimeMillis()
                )
            )

            Toast.makeText(
                this@QuestionActivity,
                "Finální skóre: $score / ${questions.size}",
                Toast.LENGTH_LONG
            ).show()
            finish()
        }
    }
}