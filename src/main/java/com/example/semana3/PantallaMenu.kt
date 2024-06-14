package com.example.semana3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class PantallaMenu : AppCompatActivity() {
    private lateinit var boton_agregar_socio: Button
    private lateinit var buscar: EditText
    private lateinit var listadoSociosLayout: LinearLayout
    private lateinit var sqlHelper: sqlHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla_menu)

        boton_agregar_socio = findViewById(R.id.nuevo_socio)
        buscar = findViewById(R.id.buscar)
        listadoSociosLayout = findViewById(R.id.listado_socios_layout)
        sqlHelper = sqlHelper(this)

        boton_agregar_socio.setOnClickListener {
            val agregarSocio = Intent(this, alta_socio::class.java)
            startActivity(agregarSocio)
        }

        //listarSocios()
    }

    //cargo los socios llamando a la funcion al momento de
    //cargar la activity, para mantenerlos actualizados.
    override fun onResume() {
        super.onResume()
        listarSocios()
    }

    //lista socios en el ViewGroup
    private fun listarSocios() {
        listadoSociosLayout.removeAllViews()
        val socios = sqlHelper.listarSocios()
        for (socio in socios) {
            val socioView = TextView(this).apply {
                text = "${socio["nombre"]} ${socio["apellido"]} - ${socio["dni"]}"
                textSize = 18f
                setOnClickListener {
                    val intent = Intent(this@PantallaMenu, DetalleSocio::class.java).apply {
                        putExtra("idsocio", socio["idsocio"]!!.toInt())
                        putExtra("nombre", socio["nombre"])
                        putExtra("apellido", socio["apellido"])
                        putExtra("dni", socio["dni"])
                        putExtra("telefono", socio["telefono"])
                        putExtra("email", socio["email"])
                        //putExtra("carnet", socio["carnet"])
                    }
                    startActivity(intent)
                }
            }
            listadoSociosLayout.addView(socioView)
        }
    }
}
