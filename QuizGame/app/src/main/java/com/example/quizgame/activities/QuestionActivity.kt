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
import com.example.quizgame.managers.UserManager
import kotlinx.coroutines.launch

class QuestionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionBinding

    // Data
    private var questions: List<Question> = emptyList()
    private var allAnswers: List<String> = emptyList() // Pool všech odpovědí pro generování špatných možností

    // Stav hry
    private var currentIndex = 0
    private var score = 0
    private var quizId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. OPRAVA PÁDU: Inicializace ViewBindingu
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizId = intent.getLongExtra("QUIZ_ID", -1)

        lifecycleScope.launch {
            val db = AppDatabaseInstance.getDatabase(this@QuestionActivity)
            questions = db.questionDao().getAllQuizQuestions(quizId) ?: emptyList() //

            // Vytvoříme si seznam všech možných odpovědí v tomto kvízu, abychom měli z čeho brát "špatné" možnosti
            allAnswers = questions.map { it.correctAnswer }

            if (questions.isNotEmpty()) {
                showQuestion()
            } else {
                Toast.makeText(this@QuestionActivity, "Kvíz nemá žádné otázky!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.btnSubmit.setOnClickListener {
            // Nejdřív zkontroluj, jestli uživatel něco zadal/vybral
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

        // Nastavení textů
        binding.twQuestion.text = q.question

        // Resetování vstupů
        binding.etAnswer.text.clear()
        binding.rdBtnGrp.clearCheck()

        // ROZHODOVÁNÍ: Text vs. Výběr z možností
        if (q.multipleChoice) {
            setupMultipleChoiceUI(q)
        } else {
            setupTextInputUI()
        }
    }

    private fun setupTextInputUI() {
        binding.etAnswer.visibility = View.VISIBLE
        binding.rdBtnGrp.visibility = View.GONE
    }

    private fun setupMultipleChoiceUI(question: Question) {
        binding.etAnswer.visibility = View.GONE
        binding.rdBtnGrp.visibility = View.VISIBLE

        // 1. Vezmeme správnou odpověď
        val correctAnswer = question.correctAnswer

        // 2. Vezmeme ostatní odpovědi z kvízu (nesmí to být ta správná)
        val otherAnswers = allAnswers.filter { it != correctAnswer }

        // 3. Náhodně vybereme 2 špatné odpovědi (pokud jich je dost, jinak doplníme "N/A")
        val wrongAnswers = otherAnswers.shuffled().take(2)

        // 4. Spojíme správnou a špatné dohromady a zamícháme pořadí
        val options = (wrongAnswers + correctAnswer).shuffled()

        // 5. Nastavíme texty tlačítkům
        // Poznámka: Musíš zajistit, že máš v seznamu dost odpovědí, jinak se tlačítka skryjí nebo zopakují
        val radioButtons = listOf(binding.rdBtn1, binding.rdBtn2, binding.rdBtn3)

        radioButtons.forEachIndexed { index, radioButton ->
            if (index < options.size) {
                radioButton.text = options[index]
                radioButton.visibility = View.VISIBLE
            } else {
                radioButton.visibility = View.INVISIBLE // Pokud by bylo málo otázek v kvízu
            }
        }
    }

    private fun validateInput(): Boolean {
        val q = questions[currentIndex]
        return if (q.multipleChoice) {
            binding.rdBtnGrp.checkedRadioButtonId != -1
        } else {
            binding.etAnswer.text.isNotBlank()
        }
    }

    private fun checkAnswer() {
        val q = questions[currentIndex]
        val userAnswer: String

        if (q.multipleChoice) {
            // Získání textu z vybraného RadioButtonu
            val selectedId = binding.rdBtnGrp.checkedRadioButtonId
            val selectedRb = findViewById<RadioButton>(selectedId)
            userAnswer = selectedRb.text.toString()
        } else {
            userAnswer = binding.etAnswer.text.toString()
        }

        // Porovnání (ignorujeme velikost písmen a mezery na okrajích)
        if (userAnswer.trim().equals(q.correctAnswer.trim(), ignoreCase = true)) {
            score++
            Toast.makeText(this, "Správně!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Chyba! Správně bylo: ${q.correctAnswer}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveGameAndFinish() {
        lifecycleScope.launch {
            val db = AppDatabaseInstance.getDatabase(this@QuestionActivity)

            db.gameDao().insertGame(
                Game(
                    quizId = quizId,
                    playerId = UserManager.getUserId(this@QuestionActivity),
                    score = score,
                    createdAt = System.currentTimeMillis()
                )
            )

            Toast.makeText(this@QuestionActivity, "Finální skóre: $score", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}