package com.example.quizgame.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizgame.database.game.Game
import com.example.quizgame.databinding.PastGameComponentBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PastGameAdapter(
    private val games: List<Game>,
    private val userNameResolver: ((Long?) -> String?)? = null,
    private val quizNameResolver: ((Long) -> String)? = null
) : RecyclerView.Adapter<PastGameAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: PastGameComponentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PastGameComponentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = games[position]

        val quizTitle = quizNameResolver?.invoke(game.quizId) ?: "Neznámý kvíz"
        holder.binding.twGameName.text = quizTitle

        val playerName = userNameResolver?.invoke(game.playerId) ?: "Neznámý hráč"
        holder.binding.twPlayerName.text = "$playerName (Skóre: ${game.score})"

        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        holder.binding.twTimeOfGame.text = sdf.format(game.createdAt)
    }

    override fun getItemCount() = games.size
}
