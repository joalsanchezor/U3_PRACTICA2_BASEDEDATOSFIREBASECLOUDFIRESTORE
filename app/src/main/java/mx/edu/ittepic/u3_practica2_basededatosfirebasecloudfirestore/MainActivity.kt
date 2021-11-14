package mx.edu.ittepic.u3_practica2_basededatosfirebasecloudfirestore

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {
    var idNotas = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mostrarNotas()

        val abrir = findViewById<ImageView>(R.id.btnBuscar)

        val nueva_nota = findViewById<ImageView>(R.id.new_nota)
        nueva_nota.setOnClickListener {
            nuevaNota()
        }

    }

    public fun mostrarNotas(){
        val resultado = Nota(this).consulta()

        listaNotas.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resultado)
        idNotas.clear()
        idNotas = Nota(this).obtenerIDs()

        if(!idNotas.isEmpty()) {
            activarEvento(listaNotas)
        }
    }

    private fun activarEvento(listaCapturados: ListView) {
        listaCapturados.setOnItemClickListener { adapterView, view, indiceSeleccionado, l ->

            val idSeleccionado = idNotas[indiceSeleccionado]
            android.app.AlertDialog.Builder(this)
                .setTitle("ATENCIÓN")
                .setMessage("¿QUÉ ACCIÓN DESEA HACER?")
                .setPositiveButton("EDITAR"){d, i-> actualizar(idSeleccionado)}
                .setNegativeButton("ELIMINAR"){d,i-> eliminar(idSeleccionado)}
                .setNeutralButton("CANCELAR"){d,i->
                    d.cancel()
                }
                .show()
        }
    }

    private fun nuevaNota(){
        var ventanaNotaNueva = Intent(this,MainActivity2::class.java)
        startActivity(ventanaNotaNueva)

        android.app.AlertDialog.Builder(this).setMessage("¿DESEAS ACTUALIZAR LA LISTA?")
            .setPositiveButton("SI"){d,i-> mostrarNotas()}
            .setNegativeButton("NO"){d,i-> d.cancel()}
            .show()
    }

    private fun actualizar(idSeleccionado: Int) {
        val intento = Intent(this, MainActivity3::class.java)
        intento.putExtra("idActualizar",idSeleccionado.toString())
        startActivity(intento)

        android.app.AlertDialog.Builder(this).setMessage("¿DESEAS ACTUALIZAR LA LISTA?")
            .setPositiveButton("SI"){d,i-> mostrarNotas()}
            .setNegativeButton("NO"){d,i-> d.cancel()}
            .show()
    }

    private fun eliminar(idSeleccionado: Int) {
        android.app.AlertDialog.Builder(this)
            .setTitle("AVISO IMPORTANTE")
            .setMessage("¿SEGURO QUE DESEAS ELIMINAR LA NOTA: ID ${idSeleccionado}?")
            .setPositiveButton("SÍ"){d,i->
                val resultado = Nota(this).eliminar(idSeleccionado)
                if(resultado){
                    Toast.makeText(this,"SE ELIMINÓ LA NOTA", Toast.LENGTH_LONG).show()
                    mostrarNotas()
                }else {
                    Toast.makeText(this,"ERROR AL ELIMINAR LA NOTA", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("NO"){d,i->
                d.cancel()
            }
            .show()
    }

}