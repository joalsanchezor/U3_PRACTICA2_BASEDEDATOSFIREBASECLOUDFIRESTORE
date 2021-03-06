package mx.edu.ittepic.u3_practica2_basededatosfirebasecloudfirestore

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var idNotas = ArrayList<Int>()
    val baseRemota = FirebaseFirestore.getInstance()
    var notas = ArrayList<Nota>()
    var notasID = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mostrarNotas()

        val nueva_nota = findViewById<ImageView>(R.id.new_nota)
        nueva_nota.setOnClickListener {
            nuevaNota()
        }

        val sincronizar = findViewById<ImageView>(R.id.sincronizar)
        sincronizar.setOnClickListener {
            alerta("SINCRONIZANDO... POR FAVOR ESPERE.")
            sincronizarDatos()
        }

        val buscar = findViewById<ImageView>(R.id.btnBuscar)
        buscar.setOnClickListener {
            buscarNota()
        }
    }

    private fun buscarNota() {
        val buscar = EditText(this)

        buscar.inputType = InputType.TYPE_CLASS_TEXT
        AlertDialog.Builder(this)
            .setTitle("BÚSQUEDA DE NOTA")
            .setMessage("Escriba el título de la Nota:")
            .setView(buscar)
            .setPositiveButton("BUSCAR") { d, i ->
                val resultado = Nota(this).buscarNota(buscar.text.toString())
                if (resultado!= -44){
                    actualizar(resultado)
                }else{
                    alerta("NO SE ENCONTRÓ LA NOTA")
                }
            }
            .setNegativeButton("CANCELAR") { d, i ->
                d.cancel()
            }
            .show()
    }

    private fun sincronizarDatos() {
        notas.clear()
        var consulta = BaseDatos(this, "notasPractica",null, 1).readableDatabase
        var cursor = consulta.query("NOTA", arrayOf("*"), null,null,null,null,null)
        val nota = Nota(MainActivity())
        var notas: MutableMap<String, String> = HashMap()

        baseRemota.collection("notas").addSnapshotListener { querySnapshot, error ->
            if(error !=null)
            {
                mensaje("ERROR DE CONEXIÓN")
                return@addSnapshotListener
            }
            for(nota in querySnapshot!!){
                notasID.add(nota.id)
            }
        }

        //SE INSERTARÁ EL DATO CUANDO ÉSTE NO EXISTA PREVIAMENTE EN LA COLECCIÓN
        if(cursor.moveToFirst()){

            do{
                if(notasID.contains(cursor.getString(0))){
                    baseRemota.collection("notas")
                        .document(cursor.getString(0))
                        .update(
                            "TITULO", cursor.getString(1),
                           "CONTENIDO", cursor.getString(2),
                            "HORA", cursor.getString(3),
                            "FECHA", cursor.getString(4)
                        )
                        .addOnFailureListener {
                            mensaje("ERROR: ${it.message}")
                        }
                }else{
                    notas.put("ID", cursor.getString(0))
                    notas.put("TITULO", cursor.getString(1))
                    notas.put("CONTENIDO", cursor.getString(2))
                    notas.put("HORA", cursor.getString(3))
                    notas.put("FECHA", cursor.getString(4))

                    baseRemota.collection("notas").document("${cursor.getString(0)}")
                        .set(notas)
                        .addOnSuccessListener {
                            alerta("SE SINCRONIZÓ CORRECTAMENTE")
                        }
                        .addOnFailureListener {
                            mensaje("ERROR: ${it.message}")
                        }
                }
            }while(cursor.moveToNext())
        }
    }
        fun alerta(s: String) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show()
    }

    private fun mensaje(s: String){
        AlertDialog.Builder(this).setTitle("ATENCIÓN")
            .setMessage(s)
            .setPositiveButton("OK"){d, i->}
            .show()
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

    fun activarEvento(listaCapturados: ListView) {
        listaCapturados.setOnItemClickListener { adapterView, view, indiceSeleccionado, l ->

            val idSeleccionado = idNotas[indiceSeleccionado]
            android.app.AlertDialog.Builder(this)
                .setTitle("ATENCIÓN")
                .setMessage("¿QUÉ ACCIÓN DESEA HACER?")
                .setPositiveButton("ABRIR"){d, i-> actualizar(idSeleccionado)}
                .setNegativeButton("ELIMINAR"){d,i-> eliminar(idSeleccionado)}
                .setNeutralButton("CANCELAR"){d,i->
                    d.cancel()
                }
                .show()
        }
    }

    fun nuevaNota(){
        var ventanaNotaNueva = Intent(this,MainActivity2::class.java)
        startActivity(ventanaNotaNueva)

        android.app.AlertDialog.Builder(this).setMessage("¿DESEAS ACTUALIZAR LA LISTA?")
            .setPositiveButton("SI"){d,i-> mostrarNotas()}
            .setNegativeButton("NO"){d,i-> d.cancel()}
            .show()
    }

    fun actualizar(idSeleccionado: Int) {
        val intento = Intent(this, MainActivity3::class.java)
        intento.putExtra("idActualizar",idSeleccionado.toString())
        startActivity(intento)

        android.app.AlertDialog.Builder(this).setMessage("¿DESEAS ACTUALIZAR LA LISTA?")
            .setPositiveButton("SI"){d,i-> mostrarNotas()}
            .setNegativeButton("NO"){d,i-> d.cancel()}
            .show()
    }

    fun eliminar(idSeleccionado: Int) {
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