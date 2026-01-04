package com.example.quizgame.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizgame.database.quiz.Quiz
import com.example.quizgame.databinding.QuizComponentBinding

class QuizAdapter(
    private val quizzes: List<Quiz>,
    private val currentUserId: Long,
    private val userNameResolver: (Long) -> String,
    private val onPlay: ((Quiz) -> Unit)? = null,
    private val onEdit: ((Quiz) -> Unit)? = null,
    private val onDelete: ((Quiz) -> Unit)? = null
) : RecyclerView.Adapter<QuizAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: QuizComponentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = QuizComponentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quiz = quizzes[position]

        holder.binding.twQuizName.text = quiz.name

        val authorName = userNameResolver(quiz.creatorId)
        holder.binding.twCreator.text = "Autor: $authorName"

        if (quiz.creatorId == currentUserId) {
            holder.binding.btnEdit.visibility = View.VISIBLE
            holder.binding.btnDelete.visibility = View.VISIBLE
        } else {
            holder.binding.btnEdit.visibility = View.GONE
            holder.binding.btnDelete.visibility = View.GONE
        }

        holder.binding.btnPlay.setOnClickListener { onPlay?.invoke(quiz) }
        holder.binding.btnEdit.setOnClickListener { onEdit?.invoke(quiz) }
        holder.binding.btnDelete.setOnClickListener { onDelete?.invoke(quiz) }
    }

    override fun getItemCount() = quizzes.size
}
