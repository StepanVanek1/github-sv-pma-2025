package com.example.quizgame.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.quizgame.database.AppDatabaseInstance
import com.example.quizgame.database.question.Question
import com.example.quizgame.databinding.ActivityCreateQuestionBinding
import kotlinx.coroutines.launch

class CreateQuestionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val quizId = intent.getLongExtra("QUIZ_ID", -1L)
        val questionId = intent.getLongExtra("QUESTION_ID", -1L)

        if (questionId != -1L) {
            lifecycleScope.launch {
                val db = AppDatabaseInstance.getDatabase(this@CreateQuestionActivity)
                val question = db.questionDao().getQuestionById(questionId)
                question.let {
                    binding.etQuestion.setText(it.question)
                    binding.etCorrectAnswer.setText(it.correctAnswer)
                    if (it.multipleChoice) {
                        binding.rdBtnChoiceQuestion.isChecked = true
                    } else {
                        binding.rdBtnTextInputQuestion.isChecked = true
                    }
                    binding.twCreateQuestion.text = "Úprava otázky"
                    binding.btnCreateQuestion.text = "Aktualizovat otázku"
                }
            }
        }

        binding.btnCreateQuestion.setOnClickListener {
            if (binding.etQuestion.text.toString().isBlank() ||
                binding.etCorrectAnswer.text.toString().isBlank() ||
                binding.rdGrpCreateQuestion.checkedRadioButtonId == -1
            ) {

                Toast.makeText(
                    this@CreateQuestionActivity,
                    "Vyplňte všechna pole a vyberte typ otázky",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val db = AppDatabaseInstance.getDatabase(this@CreateQuestionActivity)

                if (questionId == -1L) {
                    db.questionDao().insertQuestion(
                        Question(
                            question = binding.etQuestion.text.toString(),
                            correctAnswer = binding.etCorrectAnswer.text.toString(),
                            quizId = quizId,
                            multipleChoice = binding.rdBtnChoiceQuestion.isChecked
                        )
                    )
                } else {
                    db.questionDao().updateQuestion(
                        Question(
                            id = questionId,
                            question = binding.etQuestion.text.toString(),
                            correctAnswer = binding.etCorrectAnswer.text.toString(),
                            quizId = quizId,
                            multipleChoice = binding.rdBtnChoiceQuestion.isChecked
                        )
                    )
                }
                finish()
            }
        }
    }
}