package com.example.semana3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

//aca hace el alta

class alta_socio : AppCompatActivity() {
    private lateinit var clubDeportivo: sqlHelper
    private lateinit var nombre: EditText
    private lateinit var apellido: EditText
    private lateinit var dni: EditText
    private lateinit var telefono: EditText
    private lateinit var email: EditText
    private lateinit var botonGuardar: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alta_socio)

        clubDeportivo = sqlHelper(this)

        nombre = findViewById(R.id.nombre)
        apellido = findViewById(R.id.apellido)
        dni = findViewById(R.id.DNI)
        telefono = findViewById(R.id.Telefono)
        email = findViewById(R.id.Email)

        botonGuardar = findViewById(R.id.btn_alta)
        botonGuardar.setOnClickListener{
            clubDeportivo.insertarSocio(nombre.text.toString(), apellido.text.toString(), dni.text.toString(), telefono.text.toString(), email.text.toString(), "1")
            Toast.makeText(this, "Socio Cargado", Toast.LENGTH_SHORT).show()

        }

    }
}