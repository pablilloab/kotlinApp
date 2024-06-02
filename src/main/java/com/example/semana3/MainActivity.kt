package com.example.semana3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    //private lateinit var clubDeportivo: sqlHelper
    //private lateinit var nombreEditText: EditText
    //private lateinit var claveEditText: EditText
    //private lateinit var agregarButton: Button
    private lateinit var botonIngresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //clubDeportivo = sqlHelper(this)
        //nombreEditText = findViewById(R.id.nombre)
        //claveEditText = findViewById(R.id.clave)
        //agregarButton = findViewById(R.id.boton)
        botonIngresar = findViewById(R.id.btn_ingresar)

        /*agregarButton.setOnClickListener {
            val nombre = nombreEditText.text.toString()
            val clave = claveEditText.text.toString()

            clubDeportivo.insertarDato(nombre, clave)
            Toast.makeText(this, "Datos agregados correctamente", Toast.LENGTH_SHORT).show()
        }*/

        botonIngresar.setOnClickListener {
            val menu = Intent(this, PantallaMenu::class.java)
            startActivity(menu)
        }
    }
}
