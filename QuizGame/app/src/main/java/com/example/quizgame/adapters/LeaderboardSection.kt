package com.example.quizgame.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizgame.database.game.Game
import com.example.quizgame.databinding.QuizResultsComponentBinding

data class LeaderboardSection(
    val quizTitle: String,
    val games: List<Game>
)

class LeaderboardSectionAdapter(
    private val sections: List<LeaderboardSection>,
    private val userNameResolver: (Long) -> String
) : RecyclerView.Adapter<LeaderboardSectionAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: QuizResultsComponentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = QuizResultsComponentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val section = sections[position]

        holder.binding.twQuizName.text = section.quizTitle

        holder.binding.rvPastGames.layoutManager = LinearLayoutManager(holder.itemView.context)

        holder.binding.rvPastGames.adapter = PastGameLeaderboardAdapter(
            games = section.games,
            userNameResolver = userNameResolver
        )
    }

    override fun getItemCount() = sections.size
}