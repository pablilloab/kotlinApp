package com.example.semana3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetalleSocio : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_socio)

        val idSocio = intent.getIntExtra("idsocio", 1)
        val nombre = intent.getStringExtra("nombre")
        val apellido = intent.getStringExtra("apellido")
        val dni = intent.getStringExtra("dni")
        val telefono = intent.getStringExtra("telefono")
        val email = intent.getStringExtra("email")
        val carnet = intent.getStringExtra("carnet")

        findViewById<TextView>(R.id.detalle_nombre).text = nombre
        findViewById<TextView>(R.id.detalle_apellido).text = apellido
        findViewById<TextView>(R.id.detalle_dni).text = dni
        findViewById<TextView>(R.id.detalle_telefono).text = telefono
        findViewById<TextView>(R.id.detalle_email).text = email
        findViewById<TextView>(R.id.detalle_carnet).text = carnet

        findViewById<Button>(R.id.buttonAsignarActividad).setOnClickListener {
            val intent = Intent(this, AsignarActividad::class.java).apply {
                putExtra("idSocio", idSocio)
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.buttonCobroCuotas).setOnClickListener {
            val intent = Intent(this, CobroCuotas::class.java).apply {
                putExtra("idSocio", idSocio)
            }
            startActivity(intent)
        }
    }
}

