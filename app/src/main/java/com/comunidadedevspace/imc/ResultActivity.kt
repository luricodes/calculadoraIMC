package com.comunidadedevspace.imc

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

const val key_result_imc = "ResultActivity.key_imc"

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val result = intent.getFloatExtra(key_result_imc, 0f)
        val tvResult = findViewById<TextView>(R.id.tv_result)
        val tvClassificacao = findViewById<TextView>(R.id.tv_classificacao)

        tvResult.text = result.toString()

        val classificacao: String = if (result <= 18.5){
            "Magreza"
        } else if (result > 18.5 && result <= 24.9){
            "Normal"
        } else if (result > 25 && result <= 29.9) {
            "Sobrepeso"
        } else if (result > 30 && result <= 39.9) {
            "Obesidade"
        } else {
            "Obesidade Grave"
        }

        tvClassificacao.text = classificacao

        val colorResId = when(classificacao) {
            "Magreza", "Sobrepeso" -> R.color.orange  // Orange for both
            "Normal" -> R.color.green                  // Green
            "Obesidade", "Obesidade Grave" -> R.color.red // Red for both
            else -> R.color.black  // Fallback color if needed
        }

        val textColor = ContextCompat.getColor(this, colorResId)
        tvClassificacao.setTextColor(textColor)
        println(result)

    }
}