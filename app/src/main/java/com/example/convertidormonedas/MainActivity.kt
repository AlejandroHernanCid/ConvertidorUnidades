package com.example.convertidormonedas

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class MainActivity : AppCompatActivity() {
    private lateinit var inputValor: EditText
    private lateinit var spinnerTo: Spinner
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTipo: Spinner
    private lateinit var btnConvertir: Button
    private lateinit var tvResultado: TextView
    private lateinit var tvListaHistorial: TextView
    private lateinit var btnBorrarHistorial: Button
    private val historial = mutableListOf<String>()
    private val unidadesPorTipo = mapOf(
        "Longitud" to listOf("Kilómetros", "Millas", "Metros", "Pies", "Yardas", "Centímetros", "Pulgadas"),
        "Peso" to listOf("Kilogramos", "Libras", "Gramos", "Onzas"),
        "Temperatura" to listOf("Celsius", "Fahrenheit", "Kelvin")
    )



    @SuppressLint("ServiceCast")
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
        btnBorrarHistorial = findViewById<Button>(R.id.btnBorrarHistorial)
        spinnerTipo = findViewById<Spinner>(R.id.spinnerTipo)

        //adapter para tipos
        val tipos = unidadesPorTipo.keys.toList()
        val adapterTipo = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos)
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipo.adapter = adapterTipo

        //Al actualizar tipos, los dos spinners se actualizan con las siguientes unidades

        spinnerTipo.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val tipoSeleccionado = tipos[position]
                val unidades = unidadesPorTipo[tipoSeleccionado] ?: emptyList()

                val adapterUnidades = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, unidades)
                adapterUnidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerFrom.adapter = adapterUnidades
                spinnerTo.adapter = adapterUnidades
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        //Boton copiar

        val btnCopiar = findViewById<ImageButton>(R.id.btnCopiar)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        btnCopiar.setOnClickListener {
            val texto = tvResultado.text.toString()
            if (texto.isNotEmpty()) {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("resultado", texto)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, "Resultado copiado", Toast.LENGTH_SHORT).show()
            }
        }



        //Boton de convertir con la funcion de convertir

        btnConvertir.setOnClickListener {
            convertir()
        }

        btnBorrarHistorial.setOnClickListener {
            historial.clear()
            tvListaHistorial.text = ""
        }

        initAds()


    }
    private fun initAds(){
        MobileAds.initialize(this){}

        val adView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)


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