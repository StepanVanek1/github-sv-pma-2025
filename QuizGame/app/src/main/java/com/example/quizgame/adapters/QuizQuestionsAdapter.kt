package com.example.quizgame.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizgame.database.question.Question
import com.example.quizgame.databinding.QuestionComponentBinding

class QuizQuestionsAdapter(
    private val questions: List<Question>,
    private val onDelete: ((Question) -> Unit)? = null,
    private val onEdit: ((Question) -> Unit)? = null
) : RecyclerView.Adapter<QuizQuestionsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: QuestionComponentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = QuestionComponentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = questions[position]
        holder.binding.twQuestion.text = question.question
        holder.binding.twAnswer.text = "Odpověď: ${question.correctAnswer}"

        holder.binding.btnDelete.setOnClickListener { onDelete?.invoke(question) }
        holder.binding.btnEdit.setOnClickListener { onEdit?.invoke(question) }
    }

    override fun getItemCount() = questions.size
}