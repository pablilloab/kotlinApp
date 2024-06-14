package com.example.semana3

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CobroCuotas : AppCompatActivity() {

    private lateinit var dbHelper: sqlHelper
    private lateinit var cuotasImpagas: List<Map<String, String>>
    private lateinit var recyclerViewCuotas: RecyclerView
    private lateinit var checkBoxDescuento: CheckBox
    private lateinit var textViewDescuento: TextView
    private lateinit var cuotasAdapter: CuotasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cobro_cuotas)

        dbHelper = sqlHelper(this)

        val idSocio = intent.getIntExtra("idSocio", 0)
        cuotasImpagas = dbHelper.obtenerCuotasImpagas(idSocio)

        recyclerViewCuotas = findViewById(R.id.recyclerViewCuotas)
        checkBoxDescuento = findViewById(R.id.checkBoxDescuento)
        textViewDescuento = findViewById(R.id.textViewDescuento)
        val buttonPagarCuotas = findViewById<Button>(R.id.buttonPagarCuotas)

        cuotasAdapter = CuotasAdapter(cuotasImpagas)
        recyclerViewCuotas.layoutManager = LinearLayoutManager(this)
        recyclerViewCuotas.adapter = cuotasAdapter

        checkBoxDescuento.setOnCheckedChangeListener { _, _ ->
            calcularDescuento()
        }

        buttonPagarCuotas.setOnClickListener {
            pagarCuotasSeleccionadas()
        }
    }

    fun calcularDescuento() {
        val cuotasSeleccionadas = cuotasAdapter.getSelectedCuotas()
        var totalDescuento = 0.0f

        for (cuota in cuotasSeleccionadas) {
            val monto = cuota["monto"]!!.toFloat()
            if (checkBoxDescuento.isChecked) {
                totalDescuento += monto * 0.05f
            }
        }

        textViewDescuento.text = "Descuento aplicado: ${String.format("%.2f", totalDescuento)}"
    }

    private fun pagarCuotasSeleccionadas() {
        val cuotasSeleccionadas = cuotasAdapter.getSelectedCuotas()
        val cuotasAPagar = mutableListOf<Pair<Int, String>>()
        var totalMonto = 0.0f

        for (cuota in cuotasSeleccionadas) {
            val idCuota = cuota["idCuota"]!!.toInt()
            val fechaPago = cuota["fechaPago"]!!
            cuotasAPagar.add(idCuota to fechaPago)
            totalMonto += cuota["monto"]!!.toFloat()
        }

        if (checkBoxDescuento.isChecked) {
            totalMonto *= 0.95f
        }

        val exito = dbHelper.pagarCuotas(cuotasAPagar)
        if (exito) {
            Toast.makeText(this, "Cuotas pagadas con éxito. Total: ${String.format("%.2f", totalMonto)}", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al pagar cuotas", Toast.LENGTH_SHORT).show()
        }
    }

}

class CuotasAdapter(private val data: List<Map<String, String>>) : RecyclerView.Adapter<CuotasAdapter.ViewHolder>() {
    private val selectedItems = BooleanArray(data.size)

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkBoxCuota)
        val textDescripcion: TextView = view.findViewById(R.id.textDescripcion)
        val textMonto: TextView = view.findViewById(R.id.textMonto)
        val textFecha: TextView = view.findViewById(R.id.textFecha)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context).inflate(R.layout.cuota_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cuota = data[position]

        holder.textDescripcion.text = cuota["descripcion"]
        holder.textMonto.text = cuota["monto"]
        holder.textFecha.text = cuota["fechaPago"]

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = selectedItems[position]
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            selectedItems[position] = isChecked
            (holder.itemView.context as CobroCuotas).calcularDescuento()
        }
    }

    override fun getItemCount(): Int = data.size

    fun getSelectedCuotas(): List<Map<String, String>> {
        val selectedCuotas = mutableListOf<Map<String, String>>()
        for (i in selectedItems.indices) {
            if (selectedItems[i]) {
                selectedCuotas.add(data[i])
            }
        }
        return selectedCuotas
    }
}


