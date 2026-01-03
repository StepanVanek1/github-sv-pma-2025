package com.example.quizgame.fragments

import Quiz
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.quizgame.R
import com.example.quizgame.activities.QuizOverviewActivity
import com.example.quizgame.database.AppDatabaseInstance
import com.example.quizgame.databinding.FragmentWelcomeBinding
import com.example.quizgame.managers.UserManager
import kotlinx.coroutines.launch

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentWelcomeBinding.bind(view)

        binding.btnCreateQuiz.setOnClickListener {
            lifecycleScope.launch {
                val db = AppDatabaseInstance.getDatabase(requireContext())
                val newQuiz =
                    Quiz(
                        name = "Nový kvíz",
                        creatorId = UserManager.getUserId(requireContext()),
                        createdAt = System.currentTimeMillis()
                    )
                val quizId = db.quizDao().insertQuiz(newQuiz)

                val intent = Intent(requireContext(), QuizOverviewActivity::class.java)
                intent.putExtra("QUIZ_ID", quizId)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}