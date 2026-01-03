package com.example.quizgame.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizgame.R
import com.example.quizgame.adapter.LeaderboardSection
import com.example.quizgame.adapter.LeaderboardSectionAdapter
import com.example.quizgame.database.AppDatabaseInstance
import com.example.quizgame.databinding.FragmentLeaderboardBinding
import kotlinx.coroutines.launch

class LeaderboardFragment : Fragment(R.layout.fragment_leaderboard) {

    private lateinit var binding: FragmentLeaderboardBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLeaderboardBinding.bind(view)

        // Nastavení hlavního RecyclerView
        binding.rvPastGames.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            val db = AppDatabaseInstance.getDatabase(requireContext())

            val allGames = db.gameDao().getGamesOrderedByScore() ?: emptyList()
            val quizzes = db.quizDao().getAllQuizes() ?: emptyList()
            val users = db.userDao().getAllUsers() ?: emptyList()

            val usersMap = users.associateBy { it.id }

            if (allGames.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
                binding.rvPastGames.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.GONE
                binding.rvPastGames.visibility = View.VISIBLE

                val sections = quizzes.map { quiz ->
                    val gamesForThisQuiz = allGames
                        .filter { it.quizId == quiz.id } // Vybereme hry jen pro tento kvíz
                        .sortedByDescending { it.score } // Seřadíme je podle skóre

                    LeaderboardSection(
                        quizTitle = quiz.name,
                        games = gamesForThisQuiz
                    )
                }.filter {
                    it.games.isNotEmpty()
                }

                // 3. Nastavíme adaptér
                binding.rvPastGames.adapter = LeaderboardSectionAdapter(
                    sections = sections,
                    userNameResolver = { playerId ->
                        usersMap[playerId]?.name ?: "Neznámý hráč"
                    }
                )
            }
        }
    }
}