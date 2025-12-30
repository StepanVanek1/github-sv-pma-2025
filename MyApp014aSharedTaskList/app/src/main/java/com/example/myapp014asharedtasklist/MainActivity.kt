package com.example.myapp014asharedtasklist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp014asharedtasklist.Task
import com.example.myapp014asharedtasklist.TaskAdapter
import com.example.myapp014asharedtasklist.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore

    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.firestore

        // Nastavení adapteru
        adapter = TaskAdapter(
            tasks = emptyList(),
            onChecked = { task -> toggleCompleted(task) },
            onDelete = { task -> deleteTask(task) },
            onEdit = { task, newTitle -> editTask(task, newTitle) },
            onEditInit = { task -> editTaskInit(task) }
        )

        binding.recyclerViewTasks.adapter = adapter
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        // Přidání úkolu
        binding.buttonAdd.setOnClickListener {
            val title = binding.inputTask.text.toString()
            if (title.isNotEmpty()) {
                addTask(title)
                binding.inputTask.text.clear()
            }
        }

        // Realtime sledování Firestore
        listenForTasks()
    }

    private fun addTask(title: String) {
        println("DEBUG: addTask called with title = $title")
        val task = Task(title = title, completed = false)
        db.collection("tasks").add(task)
    }

    private fun toggleCompleted(task: Task) {

        // Vyhledá v databázi všechny dokumenty, které mají stejné title jako kliknutý úkol
        db.collection("tasks")
            .whereEqualTo("title", task.title)
            .get()
            .addOnSuccessListener { docs ->

                // Pro každý nalezený dokument změní hodnotu "completed" na opačnou
                for (doc in docs) {
                    db.collection("tasks")
                        .document(doc.id)
                        .update("completed", !task.completed)
                }
            }
    }

    private fun deleteTask(task: Task) {
        db.collection("tasks")
            .whereEqualTo("title", task.title)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    db.collection("tasks").document(doc.id).delete()
                }
            }
    }

    private fun editTaskInit(task: Task) {
        db.collection("tasks")
            .whereEqualTo("title", task.title)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    db.collection("tasks")
                        .document(doc.id)
                        .update("isEditing", true)
                }
            }
    }

    private fun editTask(task: Task, newTitle: String) {
        db.collection("tasks")
            .whereEqualTo("title", task.title)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    db.collection("tasks")
                        .document(doc.id)
                        .update(
                            mapOf(
                                "title" to newTitle,
                                "isEditing" to false
                            )
                        )
                }
            }
    }

    private fun listenForTasks() {
        db.collection("tasks")
            .addSnapshotListener { snapshots, _ ->

                val taskList = snapshots?.documents?.map { doc ->
                    Task(
                        title = doc.getString("title") ?: "",
                        completed = doc.getBoolean("completed") ?: false,
                        isEditing = doc.getBoolean("isEditing") ?: false
                    )
                } ?: emptyList()

                adapter.submitList(taskList)
            }
    }
}