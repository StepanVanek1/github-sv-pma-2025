package com.example.quizgame.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizgame.database.question.Question
import com.example.quizgame.databinding.QuizComponentBinding

class QuizQuestionsAdapter(
    private val questions: List<Question>,
    private val onDelete: ((Question) -> Unit)? = null,
    private val onEdit: ((Question) -> Unit)? = null
) : RecyclerView.Adapter<QuizQuestionsAdapter.ViewHolder>() {

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
        val question = questions[position]
        holder.binding.twQuizName.text = question.question
        holder.binding.twCreator.text = "Odpověď: ${question.correctAnswer}"

        holder.binding.btnPlay.visibility = View.GONE
        holder.binding.btnEdit.visibility = View.VISIBLE
        holder.binding.btnDelete.visibility = View.VISIBLE

        holder.binding.btnDelete.setOnClickListener { onDelete?.invoke(question) }
        holder.binding.btnEdit.setOnClickListener { onEdit?.invoke(question) }
    }

    override fun getItemCount() = questions.size
}