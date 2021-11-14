package mx.edu.ittepic.u3_practica2_basededatosfirebasecloudfirestore

import android.content.ContentValues
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Nota(p: Context) {
    var titulo = ""
    var contenido = ""
    var hora = obtenerHoraActual("US/Mountain")
    var fecha = obtenerFechaActual("US/Mountain")
    var pnt = p

    fun insertar() : Boolean{
        var tablaNota = BaseDatos(pnt, "notasPractica",null, 1).writableDatabase

        val dato = ContentValues()
        dato.put("titulo",titulo)
        dato.put("contenido",contenido)
        dato.put("hora",hora)
        dato.put("fecha",fecha)

        val resultado = tablaNota.insert("NOTA",null,dato)
        tablaNota.close()
        if(resultado == -1L){
            return false
        }
        return true
    }

    fun consulta() : ArrayList<String>{
        var tablaNota = BaseDatos(pnt, "notasPractica",null, 1).readableDatabase
        val resultadoConsulta = ArrayList<String>()

        val cursor = tablaNota.query("NOTA", arrayOf("*"), null,null,null,null,null)
        if(cursor.moveToFirst()){
            var dato = ""
            do{
                dato = cursor.getString(1)+"\n"+cursor.getString(2)+"\n"+cursor.getString(3)+"\n"+cursor.getString(4)
                resultadoConsulta.add(dato)
            }while(cursor.moveToNext())
        }else{
            resultadoConsulta.add("NO HAY NADA QUE MOSTRAR")
        }
        tablaNota.close()
        return resultadoConsulta
    }

    fun obtenerIDs() : ArrayList<Int>{
        var tablaNota = BaseDatos(pnt, "notasPractica",null, 1).readableDatabase
        val resultadoConsulta = ArrayList<Int>()
        val cursor = tablaNota.query("NOTA", arrayOf("*"), null,null,null,null,null)
        if(cursor.moveToFirst()){
            do{
                resultadoConsulta.add(cursor.getInt(0))
            }while(cursor.moveToNext())
        }
        tablaNota.close()
        return resultadoConsulta
    }

    fun eliminar(idEliminar:Int) : Boolean{
        var tablaNota = BaseDatos(pnt, "notasPractica",null, 1).writableDatabase
        val resultado = tablaNota.delete("NOTA","ID=?", arrayOf(idEliminar.toString()))
        if(resultado==0)return false
        return true
    }

    fun consulta(idABuscar:String) : Nota {
        var tablaNota = BaseDatos(pnt, "notasPractica",null, 1).readableDatabase
        val cursor = tablaNota.query("NOTA", arrayOf("*"), "ID=?", arrayOf(idABuscar), null, null, null)

        val artista = Nota(MainActivity())
        if(cursor.moveToFirst()){
            artista.titulo = cursor.getString(1)
            artista.contenido = cursor.getString(2)
            artista.hora = cursor.getString(3)
            artista.fecha = cursor.getString(4)
        }
        return artista
    }

    fun actualizar(idActualizar : String) : Boolean{
        var tablaNota = BaseDatos(pnt, "notasPractica",null, 1).writableDatabase
        val dato = ContentValues()

        dato.put("titulo",titulo)
        dato.put("contenido",contenido)
        dato.put("hora",hora)
        dato.put("fecha",fecha)

        val resultado = tablaNota.update("NOTA", dato, "ID=?", arrayOf(idActualizar))
        if(resultado==0) return false
        return true
    }

    fun obtenerFechaActual(zonaHoraria : String) : String{
        var formato = "yyyy-MM-dd"
        return obtenerFechaConFormato(formato, zonaHoraria)
    }

    fun obtenerHoraActual(zonaHoraria : String) : String{
        var formato = "HH:mm:ss"
        return obtenerFechaConFormato(formato, zonaHoraria)
    }

    fun obtenerFechaConFormato(formato: String, zonaHoraria: String): String {
        val calendar = Calendar.getInstance()
        val date = calendar.time
        val sdf: SimpleDateFormat
        sdf = SimpleDateFormat(formato)
        sdf.timeZone = TimeZone.getTimeZone(zonaHoraria)
        return sdf.format(date)
    }
}
