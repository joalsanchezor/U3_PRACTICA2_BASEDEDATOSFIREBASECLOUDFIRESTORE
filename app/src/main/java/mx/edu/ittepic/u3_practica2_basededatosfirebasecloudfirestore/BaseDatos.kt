package mx.edu.ittepic.u3_practica2_basededatosfirebasecloudfirestore

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(p: SQLiteDatabase) {

        p.execSQL("CREATE TABLE NOTA(ID INTEGER PRIMARY KEY AUTOINCREMENT, TITULO VARCHAR(40),CONTENIDO VARCHAR(200), HORA VARCHAR(50), FECHA VARCHAR(50))")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

}