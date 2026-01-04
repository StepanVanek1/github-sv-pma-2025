package com.example.quizgame.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizgame.adapters.QuizAdapter
import com.example.quizgame.R
import com.example.quizgame.activities.QuestionActivity
import com.example.quizgame.activities.QuizOverviewActivity
import com.example.quizgame.adapters.PastGameAdapter
import com.example.quizgame.database.AppDatabaseInstance
import com.example.quizgame.databinding.FragmentProfileBinding
import com.example.quizgame.utils.UserManager
import com.example.quizgame.utils.hideKeyboard
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentProfileBinding.bind(view)

        binding.rvProfilePastGames.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProfileQuizes.layoutManager = LinearLayoutManager(requireContext())

        binding.btnSaveName.setOnClickListener {
            val newName = binding.etNameEdit.text.toString()

            if (newName.isBlank()) {
                Toast.makeText(requireContext(), "Jméno nesmí být prázdné", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val db = AppDatabaseInstance.getDatabase(requireContext())

                val userId = UserManager.getUserId(requireContext())
                val user = db.userDao().getUserById(userId)

                user?.let {
                    val updatedUser = it.copy(name = newName)
                    db.userDao().updateUser(updatedUser)

                    Toast.makeText(requireContext(), "Jméno bylo upraveno", Toast.LENGTH_SHORT)
                        .show()

                    binding.etNameEdit.clearFocus()
                    binding.etNameEdit.hideKeyboard()
                }
            }
        }

        loadQuizAndGamePart()
    }

    private fun loadQuizAndGamePart() {
        lifecycleScope.launch {
            val db = AppDatabaseInstance.getDatabase(requireContext())

            val userId = UserManager.getUserId(requireContext())
            val user = db.userDao().getUserById(userId)

            binding.etNameEdit.setText(user?.name)

            val pastGames = db.gameDao().getGamesByPlayerId(userId) ?: emptyList()
            val createdQuizzes = db.quizDao().getAllQuizzesByCreatorId(userId) ?: emptyList()
            val quizzes = db.quizDao().getAllQuizzes()
            val quizzesMap = quizzes?.associateBy { it.id }

            if (pastGames.isEmpty()) {
                binding.tvEmptyGames.visibility = View.VISIBLE
                binding.rvProfilePastGames.visibility = View.GONE
            } else {
                binding.rvProfilePastGames.adapter = PastGameAdapter(
                    games = pastGames,
                    userNameResolver = { user?.name },
                    quizNameResolver = { id ->
                        quizzesMap?.get(id)?.name ?: "Neznámý kvíz"
                    })
            }

            if (createdQuizzes.isEmpty()) {
                binding.tvEmptyQuiz.visibility = View.VISIBLE
                binding.rvProfileQuizes.visibility = View.GONE
            } else {
                binding.rvProfileQuizes.adapter = QuizAdapter(
                    quizzes = createdQuizzes,
                    currentUserId = UserManager.getUserId(requireContext()),
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
                            loadQuizAndGamePart()
                        }
                    },
                    userNameResolver = {
                        user?.name ?: "Neznámý autor"
                    },
                )
            }
        }
    }
}