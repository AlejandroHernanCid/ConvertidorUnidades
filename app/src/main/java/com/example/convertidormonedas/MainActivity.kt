package com.example.convertidormonedas

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var inputValor: EditText
    private lateinit var spinnerTo: Spinner
    private lateinit var spinnerFrom: Spinner
    private lateinit var btnConvertir: Button
    private lateinit var tvResultado: TextView

    private val opciones = listOf("Km", "Millas")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //Inicializamos las variable asignando los ids del xml
        inputValor = findViewById<EditText>(R.id.inputValor)
        spinnerTo = findViewById<Spinner>(R.id.spinnerTo)
        spinnerFrom = findViewById<Spinner>(R.id.spinnerFrom)
        btnConvertir = findViewById<Button>(R.id.btnConvertir)
        tvResultado = findViewById<TextView>(R.id.tvResultado)

        //Creamos un adaptador para asignar el contenido del array de opciones a los spinners
        val adapterTo = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTo.adapter = adapterTo

        val adapterFrom = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFrom.adapter = adapterFrom

        //Boton de convertir con la funcion de convertir

        btnConvertir.setOnClickListener {
            convertir()
        }

    }
    private fun convertir(){
        val inputValor = inputValor.text.toString()
        if (inputValor.isEmpty()){
            Toast.makeText(this, "Introduce un número", Toast.LENGTH_SHORT).show()
            return
        }

        val valor = inputValor.toDouble()

        val unidadEntrada = spinnerTo.selectedItem.toString()
        val unidadSalida = spinnerFrom.selectedItem.toString()

        val resultado = when{

            unidadEntrada == unidadSalida -> valor

            unidadEntrada == "Km" && unidadSalida == "Millas" -> valor * 0.621371

            unidadEntrada == "Millas" && unidadSalida == "Km" -> valor / 0.621371


            else -> {
                Toast.makeText(this, "Conversión no soportada", Toast.LENGTH_SHORT).show()
                return
            }
        }
            tvResultado.text = "Resultado: %.2f".format(resultado)
    }
}