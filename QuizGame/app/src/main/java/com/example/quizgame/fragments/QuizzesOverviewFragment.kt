package com.example.quizgame.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizgame.adapters.QuizAdapter
import com.example.quizgame.activities.QuestionActivity
import com.example.quizgame.R
import com.example.quizgame.activities.QuizOverviewActivity
import com.example.quizgame.database.AppDatabaseInstance
import com.example.quizgame.databinding.FragmentQuizesOverviewBinding
import com.example.quizgame.managers.UserManager
import kotlinx.coroutines.launch

class QuizzesOverviewFragment : Fragment(R.layout.fragment_quizes_overview) {

    private lateinit var binding: FragmentQuizesOverviewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuizesOverviewBinding.bind(view)

        binding.rvQuizes.layoutManager = LinearLayoutManager(requireContext())

        initQuizzes()
    }

    private fun initQuizzes() {
        lifecycleScope.launch {
            val db = AppDatabaseInstance.getDatabase(requireContext())
            val quizzes = db.quizDao().getAllQuizes() ?: emptyList()

            if (quizzes.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
                binding.rvQuizes.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.GONE
                binding.rvQuizes.visibility = View.VISIBLE

                val users = db.userDao().getAllUsers() ?: emptyList()
                val currentUserId = UserManager.getUserId(requireContext())

                val usersMap = users.associateBy { it.id }

                binding.rvQuizes.adapter = QuizAdapter(
                    quizzes = quizzes,
                    currentUserId = currentUserId,
                    onPlay = { quiz ->
                        val intent = Intent(requireContext(), QuestionActivity::class.java)
                        intent.putExtra("QUIZ_ID", quiz.id)
                        startActivity(intent)
                    },
                    onEdit = { quiz ->
                        val intent = Intent(requireContext(), QuizOverviewActivity::class.java)
                        intent.putExtra("QUIZ_ID", quiz.id)
                        startActivity(intent)
                    },
                    onDelete = { quiz ->
                        lifecycleScope.launch {
                            db.quizDao().deleteQuiz(quiz)
                            initQuizzes()
                        }
                    },
                    userNameResolver = { creatorId ->
                        usersMap[creatorId]?.name ?: "Neznámý autor"
                    },
                )
            }
        }
    }
}