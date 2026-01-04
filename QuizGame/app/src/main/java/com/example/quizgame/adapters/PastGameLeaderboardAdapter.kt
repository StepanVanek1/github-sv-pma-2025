package com.example.quizgame.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizgame.database.game.Game
import com.example.quizgame.databinding.PastGameComponentBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PastGameLeaderboardAdapter(
    private val games: List<Game>,
    private val userNameResolver: (Long) -> String
) : RecyclerView.Adapter<PastGameLeaderboardAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: PastGameComponentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            PastGameComponentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = games[position]

        val playerName = userNameResolver(game.playerId)

        holder.binding.twGameName.text = "Hráč: $playerName"
        holder.binding.twPlayerName.text = "Skóre: ${game.score}"

        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        holder.binding.twTimeOfGame.text = sdf.format(game.createdAt)
    }

    override fun getItemCount() = games.size
}