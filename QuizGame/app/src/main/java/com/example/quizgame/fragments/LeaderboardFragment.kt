package com.example.quizgame.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizgame.R
import com.example.quizgame.adapters.LeaderboardSection
import com.example.quizgame.adapters.LeaderboardSectionAdapter
import com.example.quizgame.database.AppDatabaseInstance
import com.example.quizgame.databinding.FragmentLeaderboardBinding
import kotlinx.coroutines.launch

class LeaderboardFragment : Fragment(R.layout.fragment_leaderboard) {

    private lateinit var binding: FragmentLeaderboardBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLeaderboardBinding.bind(view)

        binding.rvPastGames.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            val db = AppDatabaseInstance.getDatabase(requireContext())

            val allGames = db.gameDao().getGamesOrderedByScore() ?: emptyList()

            val quizzes = db.quizDao().getAllQuizzes() ?: emptyList()
            val quizzesMap = quizzes.associateBy { it.id }

            val users = db.userDao().getAllUsers() ?: emptyList()
            val usersMap = users.associateBy { it.id }

            if (allGames.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
                binding.rvPastGames.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.GONE
                binding.rvPastGames.visibility = View.VISIBLE

                val sections = allGames
                    .groupBy { it.quizId }
                    .map { (quizId, games) ->

                        val quizTitle = quizzesMap[quizId]?.name ?: "Smazaný kvíz (ID: $quizId)"
                        LeaderboardSection(
                            quizTitle = quizTitle,
                            games = games.sortedByDescending { it.score }
                        )
                    }
                    .sortedBy { it.quizTitle }

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