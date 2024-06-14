package com.example.semana3

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.content.contentValuesOf
import java.util.Calendar

class sqlHelper(context: Context) : SQLiteOpenHelper(context, "club_deportivo.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val creacionTablaUsuarios = "create table usuarios (id integer primary key autoincrement, nombre varchar(30), clave varchar(30));"
        val creacionTablaSocios = "create table socios (idsocio integer primary key autoincrement, nombre varchar(30), apellido varchar(30), dni varchar(20), telefono varchar(15), email varchar(50), carnet varchar(30));"
        val creacionTablaActividad = "create table actividad (idActividad integer primary key autoincrement, descripcion varchar(100), horario varchar(50), costo_mensual REAL, costo_diario REAL);"
        val creacionTablaInscripcion = "create table inscripcion (idInscripcion integer primary key autoincrement, idSocio integer, idActividad integer, idCarnet integer, idCuota integer, foreign key (idSocio) references socios(idsocio), foreign key (idActividad) references actividad(idActividad), foreign key (idCuota) references cuota(idCuota));"
        val creacionTablaCuota = "create table cuota (idCuota integer, fechaPago date, monto float, medioPago varchar(30), pagoRealizado boolean, primary key (idCuota, fechaPago));"
        val creacionTablaCarnet = "create table carnet (idCarnet integer primary key autoincrement, numero integer);"

        db!!.execSQL(creacionTablaUsuarios)
        db.execSQL(creacionTablaSocios)
        db.execSQL(creacionTablaActividad)
        db.execSQL(creacionTablaInscripcion)
        db.execSQL(creacionTablaCuota)
        db.execSQL(creacionTablaCarnet)

        insertarDatosIniciales(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS socios")
        db.execSQL("DROP TABLE IF EXISTS actividad")
        db.execSQL("DROP TABLE IF EXISTS inscripcion")
        db.execSQL("DROP TABLE IF EXISTS cuota")
        db.execSQL("DROP TABLE IF EXISTS carnet")
        onCreate(db)
    }

    //Esta funcion inserta los datos necesarios para que la app funcione
    private fun insertarDatosIniciales(db: SQLiteDatabase) {
        val datosUsuario = contentValuesOf("nombre" to "pedro", "clave" to "1234")
        db.insert("usuarios", null, datosUsuario)

        val datosActividad = contentValuesOf(
            "descripcion" to "Natación",
            "horario" to "Lunes a Viernes 8:00 - 10:00",
            "costo_mensual" to 500.0f,
            "costo_diario" to 50.0f
        )
        db.insert("actividad", null, datosActividad)
    }

    //check de datos de login
    fun esUsuarioValido(nombre: String, clave: String): Boolean {
        val baseDatos = this.readableDatabase
        val consulta = "SELECT id FROM usuarios WHERE nombre = ? AND clave = ?"
        val cursor = baseDatos.rawQuery(consulta, arrayOf(nombre, clave))

        val usuarioExiste = cursor.count > 0

        cursor.close()
        baseDatos.close()

        return usuarioExiste
    }


    //inserta un nuevo socio a la DB.
    fun insertarSocio(nombre: String, apellido: String, dni: String, telefono: String, email: String, carnet: String) {
        val datos = contentValuesOf(
            "nombre" to nombre,
            "apellido" to apellido,
            "dni" to dni,
            "telefono" to telefono,
            "email" to email,
            "carnet" to carnet
        )
        val db = this.writableDatabase
        db.insert("socios", null, datos)
        db.close()
    }

    // funcion que recupera los socios que existen en el club
    fun listarSocios(): List<Map<String, String>> {
        val listaSocios = mutableListOf<Map<String, String>>()
        val baseDatos = this.readableDatabase
        val cursor = baseDatos.query("socios", null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            val socio = mapOf(
                "idsocio" to cursor.getInt(cursor.getColumnIndexOrThrow("idsocio")).toString(),
                "nombre" to cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                "apellido" to cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                "dni" to cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                "telefono" to cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                "email" to cursor.getString(cursor.getColumnIndexOrThrow("email")),
                "carnet" to cursor.getString(cursor.getColumnIndexOrThrow("carnet"))
            )
            listaSocios.add(socio)
        }
        cursor.close()
        baseDatos.close()
        return listaSocios
    }


    fun listarActividades(): List<Map<String, String>> {
        val actividadesList = mutableListOf<Map<String, String>>()
        val db = this.readableDatabase
        val cursor: Cursor = db.query("actividad", null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val actividad = mapOf(
                    "idActividad" to cursor.getInt(cursor.getColumnIndexOrThrow("idActividad")).toString(),
                    "descripcion" to cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                    "horario" to cursor.getString(cursor.getColumnIndexOrThrow("horario")),
                    "costo_mensual" to cursor.getFloat(cursor.getColumnIndexOrThrow("costo_mensual")).toString(),
                    "costo_diario" to cursor.getFloat(cursor.getColumnIndexOrThrow("costo_diario")).toString()
                )
                actividadesList.add(actividad)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return actividadesList
    }

    fun obtenerActividadPorDescripcion(descripcion: String): Map<String, String>? {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            "actividad",
            null,
            "descripcion = ?",
            arrayOf(descripcion),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            val actividad = mapOf(
                "idActividad" to cursor.getInt(cursor.getColumnIndexOrThrow("idActividad")).toString(),
                "descripcion" to cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                "horario" to cursor.getString(cursor.getColumnIndexOrThrow("horario")),
                "costo_mensual" to cursor.getFloat(cursor.getColumnIndexOrThrow("costo_mensual")).toString(),
                "costo_diario" to cursor.getFloat(cursor.getColumnIndexOrThrow("costo_diario")).toString()
            )
            cursor.close()
            db.close()
            actividad
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    fun asignarActividadASocio(idSocio: Int, idActividad: Int, duracion: Int, monto: Float, esDiaria: Boolean): Boolean {
        val db = this.writableDatabase

        // Check if the socio is already enrolled in the activity
        val cursor: Cursor = db.query(
            "inscripcion",
            arrayOf("idInscripcion"),
            "idSocio = ? AND idActividad = ?",
            arrayOf(idSocio.toString(), idActividad.toString()),
            null, null, null
        )

        val yaInscrito = cursor.count > 0
        cursor.close()

        if (yaInscrito) {
            db.close()
            return false // Already enrolled
        }

        val carnet = obtenerOInsertarCarnet()

        if (esDiaria) {
            val cuotaId = obtenerNuevoIdCuota()
            insertarCuota(cuotaId, monto, 0)
            val datosInscripcion = contentValuesOf(
                "idSocio" to idSocio,
                "idActividad" to idActividad,
                "idCarnet" to carnet,
                "idCuota" to cuotaId
            )
            db.insert("inscripcion", null, datosInscripcion)
        } else {
            val cuotaId = obtenerNuevoIdCuota()
            for (mes in 0 until duracion) {
                insertarCuota(cuotaId, monto, mes + 1)
                val datosInscripcion = contentValuesOf(
                    "idSocio" to idSocio,
                    "idActividad" to idActividad,
                    "idCarnet" to carnet,
                    "idCuota" to cuotaId
                )
                db.insert("inscripcion", null, datosInscripcion)
            }
        }

        db.close()
        return true
    }

    private fun insertarCuota(idCuota: Int, monto: Float, mes: Int) {
        val db = this.writableDatabase
        val fechaPago = Calendar.getInstance().apply { add(Calendar.MONTH, mes) }.time
        val datosCuota = contentValuesOf(
            "idCuota" to idCuota,
            "fechaPago" to fechaPago.toString(),
            "monto" to monto,
            "medioPago" to "contado",
            "pagoRealizado" to 0
        )
        db.insert("cuota", null, datosCuota)
    }

    private fun obtenerNuevoIdCuota(): Int {
        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT MAX(idCuota) FROM cuota", null)
        val idCuota = if (cursor.moveToFirst() && cursor.getInt(0) != 0) {
            cursor.getInt(0) + 1
        } else {
            1
        }
        cursor.close()
        return idCuota
    }

    private fun obtenerOInsertarCarnet(): Int {
        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT MAX(numero) FROM carnet", null)

        val numeroCarnet = if (cursor.moveToFirst() && cursor.getInt(0) != 0) {
            cursor.getInt(0) + 1
        } else {
            1
        }
        cursor.close()

        val datosCarnet = contentValuesOf("numero" to numeroCarnet)
        db.insert("carnet", null, datosCarnet)

        val carnetCursor: Cursor = db.rawQuery("SELECT MAX(idCarnet) FROM carnet", null)
        carnetCursor.moveToFirst()
        val idCarnet = carnetCursor.getInt(0)
        carnetCursor.close()

        return idCarnet
    }

    fun obtenerCuotasImpagas(idSocio: Int): List<Map<String, String>> {
        val cuotasList = mutableListOf<Map<String, String>>()
        val db = this.readableDatabase
        val query = """
        SELECT DISTINCT cuota.idCuota, cuota.fechaPago, cuota.monto, actividad.descripcion
        FROM cuota
        INNER JOIN inscripcion ON cuota.idCuota = inscripcion.idCuota
        INNER JOIN actividad ON inscripcion.idActividad = actividad.idActividad
        WHERE inscripcion.idSocio = ? AND cuota.pagoRealizado = 0
    """
        val cursor: Cursor = db.rawQuery(query, arrayOf(idSocio.toString()))

        if (cursor.moveToFirst()) {
            do {
                val cuota = mapOf(
                    "idCuota" to cursor.getInt(cursor.getColumnIndexOrThrow("idCuota")).toString(),
                    "fechaPago" to cursor.getString(cursor.getColumnIndexOrThrow("fechaPago")),
                    "monto" to cursor.getFloat(cursor.getColumnIndexOrThrow("monto")).toString(),
                    "descripcion" to cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
                )
                cuotasList.add(cuota)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return cuotasList
    }

    fun pagarCuotas(cuotas: List<Pair<Int, String>>): Boolean {
        val db = this.writableDatabase
        var exito = true

        db.beginTransaction()
        try {
            for (cuota in cuotas) {
                val (idCuota, fechaPago) = cuota
                val values = contentValuesOf("pagoRealizado" to 1)
                val affectedRows = db.update(
                    "cuota", values,
                    "idCuota = ? AND fechaPago = ?",
                    arrayOf(idCuota.toString(), fechaPago)
                )
                if (affectedRows <= 0) {
                    exito = false
                    break
                }
            }
            if (exito) {
                db.setTransactionSuccessful()
            }
        } catch (e: Exception) {
            exito = false
        } finally {
            db.endTransaction()
        }
        db.close()
        return exito
    }

}



