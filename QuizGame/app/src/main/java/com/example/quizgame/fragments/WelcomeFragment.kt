package com.example.quizgame.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.quizgame.R
import com.example.quizgame.activities.QuizOverviewActivity
import com.example.quizgame.database.AppDatabaseInstance
import com.example.quizgame.database.quiz.Quiz
import com.example.quizgame.databinding.FragmentWelcomeBinding
import com.example.quizgame.utils.UserManager
import kotlinx.coroutines.launch

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {
    private lateinit var binding: FragmentWelcomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWelcomeBinding.bind(view)

        binding.btnCreateQuiz.setOnClickListener {
            lifecycleScope.launch {
                val db = AppDatabaseInstance.getDatabase(requireContext())
                val quizId = db.quizDao().insertQuiz(
                    Quiz(
                        name = "Nový kvíz",
                        creatorId = UserManager.getUserId(requireContext()),
                        createdAt = System.currentTimeMillis()
                    )
                )

                val intent = Intent(requireContext(), QuizOverviewActivity::class.java)
                intent.putExtra("QUIZ_ID", quizId)
                startActivity(intent)
            }
        }
    }
}