package com.example.myapp002myownconstraintlayout

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        data class Person(
            val name: String,
            val email: String,
            val telephone: String,
            val desc: String
        )

        val etNameValue = findViewById<EditText>(R.id.etInputName)
        val etSurnameValue = findViewById<EditText>(R.id.etInputSurname)
        val etEmailValue = findViewById<EditText>(R.id.etInputEmail)
        val etPhoneValue = findViewById<EditText>(R.id.etInputPhone)
        val etDescValue = findViewById<EditText>(R.id.etInputDesc)
        val personSummary = findViewById<TextView>(R.id.TWPersonSummary)
        val btnSend = findViewById<Button>(R.id.btnSend)
        val btnDelete = findViewById<Button>(R.id.btnClear)
        val btnClearPeople = findViewById<Button>(R.id.btnClearPeople)

        btnClearPeople.visibility = View.GONE

        fun wipeForm() {
            etNameValue.text = null
            etSurnameValue.text = null
            etEmailValue.text = null
            etPhoneValue.text = null
            etDescValue.text = null
        }

        btnSend.setOnClickListener {
            val person = Person(
                etNameValue.text.toString() + " " + etSurnameValue.text.toString(),
                etEmailValue.text.toString(),
                etPhoneValue.text.toString(),
                etDescValue.text.toString()
            )

            personSummary.append(
                "Jméno: ${person.name}\n" +
                        "E-mail: ${person.email}\n" +
                        "Telefon: ${person.telephone}\n" +
                        "Popis: ${person.desc}\n\n"
            )

            if (personSummary.text.isNotBlank()) {
                btnClearPeople.visibility = View.VISIBLE
            }

            wipeForm()
        }

        btnDelete.setOnClickListener {
            wipeForm()
        }

        btnClearPeople.setOnClickListener {
            personSummary.text = ""
            btnClearPeople.visibility = View.GONE
        }
    }
}