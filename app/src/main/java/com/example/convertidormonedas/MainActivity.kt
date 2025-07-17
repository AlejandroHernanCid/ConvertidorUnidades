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
    private lateinit var tvListaHistorial: TextView
    private val historial = mutableListOf<String>()
    private val opciones = listOf("Kilómetros", "Millas", "Metros", "Pies", "Yardas", "Centímetros", "Pulgadas",
        "Kilogramos", "Libras", "Gramos", "Onzas",
        "Celsius", "Fahrenheit", "Kelvin")


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
        tvListaHistorial = findViewById<TextView>(R.id.tvListaHistorial)

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

            // ----- LONGITUD -----
            unidadEntrada == "Kilómetros" && unidadSalida == "Millas" -> valor * 0.621373
            unidadEntrada == "Kilómetros" && unidadSalida == "Metros" -> valor * 1000.0
            unidadEntrada == "Kilómetros" && unidadSalida == "Pies" -> valor * 3280.84
            unidadEntrada == "Kilómetros" && unidadSalida == "Yardas" -> valor * 1093.61
            unidadEntrada == "Kilómetros" && unidadSalida == "Centímetros" -> valor * 100000.0
            unidadEntrada == "Kilómetros" && unidadSalida == "Pulgadas" -> valor * 39370.08

            unidadEntrada == "Millas" && unidadSalida == "Kilómetros" -> valor * 1.60934
            unidadEntrada == "Millas" && unidadSalida == "Metros" -> valor * 1609.34
            unidadEntrada == "Millas" && unidadSalida == "Pies" -> valor * 5280.0
            unidadEntrada == "Millas" && unidadSalida == "Yardas" -> valor * 1760.0
            unidadEntrada == "Millas" && unidadSalida == "Centímetros" -> valor * 160934.0
            unidadEntrada == "Millas" && unidadSalida == "Pulgadas" -> valor * 63360.0

            unidadEntrada == "Metros" && unidadSalida == "Kilómetros" -> valor / 1000.0
            unidadEntrada == "Metros" && unidadSalida == "Millas" -> valor / 1609.34
            unidadEntrada == "Metros" && unidadSalida == "Pies" -> valor * 3.28084
            unidadEntrada == "Metros" && unidadSalida == "Yardas" -> valor * 1.09361
            unidadEntrada == "Metros" && unidadSalida == "Centímetros" -> valor * 100.0
            unidadEntrada == "Metros" && unidadSalida == "Pulgadas" -> valor * 39.3701

            unidadEntrada == "Pies" && unidadSalida == "Metros" -> valor * 0.3048
            unidadEntrada == "Pies" && unidadSalida == "Kilómetros" -> valor * 0.0003048
            unidadEntrada == "Pies" && unidadSalida == "Millas" -> valor / 5280.0
            unidadEntrada == "Pies" && unidadSalida == "Yardas" -> valor / 3.0
            unidadEntrada == "Pies" && unidadSalida == "Centímetros" -> valor * 30.48
            unidadEntrada == "Pies" && unidadSalida == "Pulgadas" -> valor * 12.0

            unidadEntrada == "Yardas" && unidadSalida == "Metros" -> valor * 0.9144
            unidadEntrada == "Centímetros" && unidadSalida == "Metros" -> valor / 100.0
            unidadEntrada == "Pulgadas" && unidadSalida == "Centímetros" -> valor * 2.54

            unidadEntrada == "Centímetros" && unidadSalida == "Pulgadas" -> valor / 2.54
            unidadEntrada == "Pulgadas" && unidadSalida == "Metros" -> valor * 0.0254

            // ----- PESO -----
            unidadEntrada == "Kilogramos" && unidadSalida == "Libras" -> valor * 2.20462
            unidadEntrada == "Kilogramos" && unidadSalida == "Gramos" -> valor * 1000.0
            unidadEntrada == "Kilogramos" && unidadSalida == "Onzas" -> valor * 35.274

            unidadEntrada == "Libras" && unidadSalida == "Kilogramos" -> valor * 0.453592
            unidadEntrada == "Libras" && unidadSalida == "Gramos" -> valor * 453.592
            unidadEntrada == "Libras" && unidadSalida == "Onzas" -> valor * 16.0

            unidadEntrada == "Gramos" && unidadSalida == "Kilogramos" -> valor / 1000.0
            unidadEntrada == "Gramos" && unidadSalida == "Libras" -> valor / 453.592
            unidadEntrada == "Gramos" && unidadSalida == "Onzas" -> valor * 0.035274

            unidadEntrada == "Onzas" && unidadSalida == "Gramos" -> valor / 0.035274
            unidadEntrada == "Onzas" && unidadSalida == "Kilogramos" -> valor / 35.274

            // ----- TEMPERATURA -----
            unidadEntrada == "Celsius" && unidadSalida == "Fahrenheit" -> valor * 9 / 5 + 32
            unidadEntrada == "Fahrenheit" && unidadSalida == "Celsius" -> (valor - 32) * 5 / 9
            unidadEntrada == "Celsius" && unidadSalida == "Kelvin" -> valor + 273.15
            unidadEntrada == "Kelvin" && unidadSalida == "Celsius" -> valor - 273.15
            unidadEntrada == "Fahrenheit" && unidadSalida == "Kelvin" -> (valor - 32) * 5 / 9 + 273.15
            unidadEntrada == "Kelvin" && unidadSalida == "Fahrenheit" -> (valor - 273.15) * 9 / 5 + 32

            else -> {
                Toast.makeText(this, "Conversión no soportada", Toast.LENGTH_SHORT).show()
                return
            }
        }
        tvResultado.text = "$valor $unidadEntrada -> ${"%.2f".format(resultado)} $unidadSalida"

        //Añadimos al historial el resultado
        historial.add(0, tvResultado.text.toString())
        //Actualizamos el TextView
        val historialTexto = historial.joinToString(separator = "\n")
        tvListaHistorial.text = historialTexto
        //Condicion para limitar historial a 10
        if (historial.size > 10) historial.removeAt(historial.lastIndex)

    }

}