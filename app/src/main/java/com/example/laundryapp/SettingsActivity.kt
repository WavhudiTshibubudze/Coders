package com.example.laundryapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var languageSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        languageSpinner = findViewById(R.id.languageSpinner)
        setupLanguageSpinner()
    }

    private fun setupLanguageSpinner() {
        val languages = arrayOf("English", "Afrikaans", "isiZulu", "isiXhosa", "Sesotho", "Setswana")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val languageCode = when (position) {
                    0 -> "en" // English
                    1 -> "af" // Afrikaans
                    2 -> "zu" // isiZulu
                    3 -> "xh" // isiXhosa
                    4 -> "st" // Sesotho
                    5 -> "tn" // Setswana
                    else -> "en"
                }
                setLocale(languageCode)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        recreate() // Recreate the activity to apply the new locale
    }
}
