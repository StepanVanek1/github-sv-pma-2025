package com.example.myapp014asharedtasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp014asharedtasklist.databinding.ItemTaskBinding

class TaskAdapter(
    private var tasks: List<Task>,
    private val onChecked: (Task) -> Unit,
    private val onDelete: (Task) -> Unit,
    private val onEdit: (Task, String) -> Unit,
    private val onEditInit: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.binding.textTitle.text = task.title
        holder.binding.textTitleEdit.setText(task.title)

        println(task.isEditing)

        if (task.isEditing) {
            holder.binding.textTitle.visibility = View.GONE
            holder.binding.textTitleEdit.visibility = View.VISIBLE
            holder.binding.editInit.visibility = View.GONE
            holder.binding.taskEdit.visibility = View.VISIBLE
        } else {
            holder.binding.textTitle.visibility = View.VISIBLE
            holder.binding.textTitleEdit.visibility = View.GONE
            holder.binding.editInit.visibility = View.VISIBLE
            holder.binding.taskEdit.visibility = View.GONE
        }

        // ENTER EDIT MODE
        holder.binding.taskEdit.setOnClickListener {
            onEditInit(task)
        }

        // Nastaví text úkolu
        holder.binding.textTitle.text = task.title

        // Nastaví zaškrtnutí checkboxu podle hodnoty v objektu
        holder.binding.checkCompleted.isChecked = task.completed

        // Listener pro změnu stavu checkboxu
        // Když uživatel změní stav, zavolá se funkce onChecked(task)
        holder.binding.checkCompleted.setOnCheckedChangeListener { _, _ ->
            onChecked(task)
        }

        // Listener pro smazání úkolu
        holder.binding.taskDelete.setOnClickListener {
            onDelete(task)
        }

        holder.binding.editInit.setOnClickListener {
            onEditInit(task)
        }

        // CONFIRM EDIT
        holder.binding.taskEdit.setOnClickListener {
            val newTitle = holder.binding.textTitleEdit.text.toString()
            if (newTitle.isNotBlank()) {
                onEdit(task, newTitle)
            }
        }

    }

    // Počet položek v seznamu
    override fun getItemCount() = tasks.size

    // Aktualizace seznamu – nahradí starý list novým a překreslí RecyclerView
    fun submitList(newList: List<Task>) {
        tasks = newList
        notifyDataSetChanged()
    }
}