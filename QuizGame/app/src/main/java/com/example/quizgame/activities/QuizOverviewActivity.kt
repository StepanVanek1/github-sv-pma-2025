package com.example.quizgame.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizgame.adapters.QuizQuestionsAdapter
import com.example.quizgame.database.AppDatabaseInstance
import com.example.quizgame.databinding.ActivityQuizOverviewBinding
import com.example.quizgame.utils.hideKeyboard
import kotlinx.coroutines.launch

class QuizOverviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizOverviewBinding
    private var quizId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizId = intent.getLongExtra("QUIZ_ID", -1)

        if (quizId == -1L) {
            Toast.makeText(this, "Chyba: Kvíz nenalezen", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupRecyclerView()

        binding.btnAddQuestion.setOnClickListener {
            val intent = Intent(this, CreateQuestionActivity::class.java)
            intent.putExtra("QUIZ_ID", quizId)
            startActivity(intent)
        }

        binding.btnSaveName.setOnClickListener {
            saveQuizTitle()
        }

        loadQuizDetails()
    }

    private fun setupRecyclerView() {
        binding.rvQuizQuestions.layoutManager = LinearLayoutManager(this)
    }

    private fun loadQuizDetails() {
        lifecycleScope.launch {
            val db = AppDatabaseInstance.getDatabase(this@QuizOverviewActivity)
            val quiz = db.quizDao().getQuizById(quizId)

            binding.etQuizName.setText(quiz.name)

            loadQuestions()
        }
    }

    private fun loadQuestions() {
        lifecycleScope.launch {
            val db = AppDatabaseInstance.getDatabase(this@QuizOverviewActivity)
            val questions = db.questionDao().getAllQuizQuestions(quizId) ?: emptyList()

            binding.rvQuizQuestions.adapter = QuizQuestionsAdapter(
                questions,
                onDelete = { questionToDelete ->
                    lifecycleScope.launch {
                        db.questionDao().deleteQuestion(questionToDelete)
                        loadQuestions()
                    }
                },
                onEdit = { questionToEdit ->
                    val intent =
                        Intent(this@QuizOverviewActivity, CreateQuestionActivity::class.java)
                    intent.putExtra("QUIZ_ID", quizId)
                    intent.putExtra("QUESTION_ID", questionToEdit.id)
                    startActivity(intent)
                }
            )
        }
    }

    private fun saveQuizTitle() {
        val newTitle = binding.etQuizName.text.toString()
        if (newTitle.isBlank()) {
            Toast.makeText(this, "Název kvízu nesmí být prázdný", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val db = AppDatabaseInstance.getDatabase(this@QuizOverviewActivity)
            db.quizDao().updateName(quizId, newTitle)
            Toast.makeText(this@QuizOverviewActivity, "Název kvízu byl upraven", Toast.LENGTH_SHORT).show()
        }

        binding.etQuizName.clearFocus()
        binding.etQuizName.hideKeyboard()
    }

    override fun onResume() {
        super.onResume()
        loadQuestions()
    }
}