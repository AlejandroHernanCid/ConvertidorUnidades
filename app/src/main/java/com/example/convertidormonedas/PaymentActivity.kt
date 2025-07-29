package com.example.convertidormonedas

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class PaymentActivity : AppCompatActivity() {

    private lateinit var paymentSheet: PaymentSheet
    private lateinit var paymentIntentClientSecret: String
    private val TAG = "PaymentActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // Inicializa Stripe con tu clave pública
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51RpmWyIUDwXOcYFBQBTpKMmvL1tlufaso0Uj2iVm1Mx0u2imoqIb8yTijsT1ZFKbITJIrkYqzy7OBO304mb6yyy500quXxVMHY"
        )

        paymentSheet = PaymentSheet.Builder(::onPaymentSheetResult).build(this)

        findViewById<MaterialButton>(R.id.btnPagar).setOnClickListener {
            fetchPaymentIntent()
        }
    }

    private fun fetchPaymentIntent() {
        // Usa 10.0.2.2 si pruebas en emulador; tu IP en LAN para dispositivo real
        val url = "http://192.168.1.245:4242/create-payment-intent"
        val json = """
            {
                "items": [{"amount": 100}]
            }
        """.trimIndent()

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val body = json.toRequestBody(mediaType)
        val request = Request.Builder().url(url).post(body).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Error al llamar al servidor", e)
                runOnUiThread {
                    Toast.makeText(
                        this@PaymentActivity,
                        "Error de red: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string().orEmpty()
                Log.d(TAG, "Código: ${response.code}, body: $responseBody")

                try {
                    val jsonObject = JSONObject(responseBody)
                    if (response.isSuccessful) {
                        paymentIntentClientSecret = jsonObject.getString("clientSecret")
                        runOnUiThread { presentPaymentSheet() }
                    } else {
                        val errorMsg = jsonObject.optString("error", "Error desconocido")
                        runOnUiThread {
                            Toast.makeText(this@PaymentActivity, errorMsg, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parseando respuesta", e)
                    runOnUiThread {
                        Toast.makeText(
                            this@PaymentActivity,
                            "Respuesta inválida del servidor",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })
    }

    private fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration("Tu negocio")
        )
    }

    private fun onPaymentSheetResult(paymentResult: PaymentSheetResult) {
        when (paymentResult) {
            is PaymentSheetResult.Completed -> {
                Toast.makeText(this, "Pago completado! Ahora tienes plan Premium ✨", Toast.LENGTH_LONG).show()
                becamePremium()
            }
            is PaymentSheetResult.Canceled -> {
                //Toast.makeText(this, "Pago cancelado", Toast.LENGTH_LONG).show()
            }
            is PaymentSheetResult.Failed -> {
                /*Toast.makeText(
                    this,
                    "Error en pago: ${paymentResult.error.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()*/
            }
        }
    }

    private fun becamePremium(){
       var sharedPreferences: SharedPreferences
       sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        editor.apply {
            putBoolean("PREMIUM", true)
        }.apply()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
