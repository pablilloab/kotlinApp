package com.example.semana3

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AsignarActividad : AppCompatActivity() {

    private lateinit var spinnerActividades: Spinner
    private lateinit var editTextDuracion: EditText
    private lateinit var editTextMonto: EditText
    private lateinit var spinnerEsDiaria: Spinner
    private lateinit var buttonAsignar: Button
    private lateinit var sqlHelper: sqlHelper
    private var idSocio: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asignar_actividad)

        sqlHelper = sqlHelper(this)
        idSocio = intent.getIntExtra("idSocio", 1)

        spinnerActividades = findViewById(R.id.spinnerActividades)
        editTextDuracion = findViewById(R.id.editTextDuracion)
        editTextMonto = findViewById(R.id.editTextMonto)
        spinnerEsDiaria = findViewById(R.id.spinnerEsDiaria)
        buttonAsignar = findViewById(R.id.buttonAsignar)

        cargarActividades()
        configurarSpinnerEsDiaria()

        buttonAsignar.setOnClickListener {
            asignarActividad()
        }
    }

    private fun cargarActividades() {
        val actividades = sqlHelper.listarActividades()
        val nombresActividades = actividades.map { it["descripcion"] }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresActividades)
        spinnerActividades.adapter = adapter
    }

    private fun configurarSpinnerEsDiaria() {
        val opciones = arrayOf("Sí", "No")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        spinnerEsDiaria.adapter = adapter
    }

    private fun asignarActividad() {
        val actividadSeleccionada = spinnerActividades.selectedItem.toString()
        val actividad = sqlHelper.obtenerActividadPorDescripcion(actividadSeleccionada)
        val duracion = editTextDuracion.text.toString().toInt()
        val monto = editTextMonto.text.toString().toFloat()
        val esDiaria = spinnerEsDiaria.selectedItem.toString() == "Sí"

        if (actividad != null) {
            val exito = sqlHelper.asignarActividadASocio(idSocio, actividad["idActividad"]!!.toInt(), duracion, monto, esDiaria)
            if (exito) {
                Toast.makeText(this, "Actividad asignada correctamente", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Error: el socio ya está inscrito en esta actividad", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Error al asignar actividad", Toast.LENGTH_LONG).show()
        }
    }
}

