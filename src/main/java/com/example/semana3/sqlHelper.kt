package com.example.semana3

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

class sqlHelper(context : Context) : SQLiteOpenHelper(context, "club_deportivo.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {

        //TABLA PARA GUARDAR USUARIOS DEL SISTEMA
        val creacionTabla = "create table usuarios" + "(id integer primary key autoincrement, " +
                "nombre varchar(30), clave varchar(30)); "

        //TABLA PARA GUARDAR LOS SOCIOS DEL CLUB
        val creacionTablSocios = "create table socios (" +
            "idsocio integer primary key autoincrement, " +
                    "nombre varchar(30), " +
                    "apellido varchar(30), " +
                    "dni varchar(20), " +
                    "telefono varchar(15), " +
                    "email varchar(50), " +
                    "carnet varchar(30));";

        db!!.execSQL(creacionTabla)
        db!!.execSQL(creacionTablSocios)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    //insert de datos de ususarios
    fun insertarDato (nombre: String, clave: String){
        val datos = contentValuesOf()

        datos.put("nombre", nombre)
        datos.put("clave", clave)

        val db = this.writableDatabase

        db.insert("usuarios", null, datos)
        db.close()
    }

    //insert de datos de socios
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

}