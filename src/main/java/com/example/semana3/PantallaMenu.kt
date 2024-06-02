package com.example.semana3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PantallaMenu : AppCompatActivity() {
    private lateinit var boton_agregar_socio: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla_menu)

        boton_agregar_socio = findViewById(R.id.nuevo_socio)

        boton_agregar_socio.setOnClickListener{
            val agregarSocio = Intent(this, alta_socio::class.java)
            startActivity(agregarSocio)
        }
    }
}