package mx.edu.ittepic.u3_practica2_basededatosfirebasecloudfirestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AlertDialog
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.util.*

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val radio_group = findViewById<RadioGroup>(R.id.radioGroup)
        val radio_group2 = findViewById<RadioGroup>(R.id.radioGroup2)
        val guardar = findViewById<ImageView>(R.id.btn_nueva_nueva)
        var nombreArchivo = ""

        guardar.setOnClickListener {
            //ASIGNAR EL NOMBRE DE LA NOTA
                //OBTENER EL ID DEL RADIO BUTTON SELECCIONADO
            var tipoSeleccionado = radio_group2.indexOfChild(findViewById(radio_group2.checkedRadioButtonId)).toInt()
            if(tipoSeleccionado==0){
                //GENERAR ID DINÁMICO Y UNICO
                nombreArchivo = UUID.randomUUID().toString()
            }else{
                nombreArchivo = findViewById<EditText>(R.id.nombreArchivo).text.toString()
                if(nombreArchivo.equals("")){
                    nombreArchivo = UUID.randomUUID().toString()
                }
            }

            //OBTENER EL ID DEL RADIO BUTTON SELECCIONADO
            var index = radio_group.indexOfChild(findViewById(radio_group.checkedRadioButtonId)).toInt()

            if(index==0) {
                if (guardarEnArchivoInterno(nombreArchivo.toString())) {
                    findViewById<EditText>(R.id.contenido).setText("")
                    findViewById<EditText>(R.id.nombreArchivo).setText("")
                    Toast.makeText(this, "SE GUARDÓ LA NOTA EN MEMORIA INTERNA: "+nombreArchivo, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "ERROR AL GUARDAR LA NOTA", Toast.LENGTH_SHORT).show()
                }
            }else{
                if (guardarEnArchivoExterno(nombreArchivo.toString())) {
                    findViewById<EditText>(R.id.contenido).setText("")
                    findViewById<EditText>(R.id.nombreArchivo).setText("")
                    Toast.makeText(this, "SE GUARDÓ LA NOTA EN MEMORIA EXTERNA: "+nombreArchivo, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "ERROR AL GUARDAR LA NOTA", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun guardarEnArchivoInterno(nombre:String) : Boolean{
        try {
            val archivo = OutputStreamWriter(openFileOutput(nombre+".txt", MODE_PRIVATE))
            var textoAGuardar = findViewById<EditText>(R.id.contenido).text.toString()

            archivo.write(textoAGuardar)
            archivo.flush()
            archivo.close()
            return true
        }catch (io: IOException){
            AlertDialog.Builder(this)
                .setTitle("¡ERROR!")
                .setMessage(io.message)
                .setPositiveButton("ACEPTAR") {dialog, exception->
                    dialog.dismiss()
                }
                .show()
            return false
        }
    }

    private fun guardarEnArchivoExterno(nombre:String) : Boolean{
        try {
            val raiz = getExternalFilesDir(null)
            val archivo = File(raiz?.absolutePath,nombre+".txt")
            var textoAGuardar = findViewById<EditText>(R.id.contenido).text.toString()

            val objescribir = OutputStreamWriter(FileOutputStream(archivo))

            objescribir.write(textoAGuardar)
            objescribir.flush()
            objescribir.close()
            return true
        }catch (io: IOException){
            AlertDialog.Builder(this)
                .setTitle("¡ERROR!")
                .setMessage(io.message)
                .setPositiveButton("ACEPTAR") {dialog, exception->
                    dialog.dismiss()
                }
                .show()
            return false
        }
    }



}