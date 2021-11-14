package mx.edu.ittepic.u3_practica2_basededatosfirebasecloudfirestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.util.*

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val guardar = findViewById<ImageView>(R.id.btn_nueva_nueva)
        val regresar = findViewById<ImageView>(R.id.regresar)
        var nombreArchivoS = ""

        guardar.setOnClickListener {
            //ASIGNAR EL NOMBRE DE LA NOTA
            nombreArchivoS = findViewById<EditText>(R.id.nombreArchivo).text.toString()
            if(nombreArchivoS.equals("")){
                nombreArchivoS = UUID.randomUUID().toString()
            }
            val nota = Nota(this)
            nota.titulo = nombreArchivoS
            nota.contenido = contenido.text.toString()

            val resultado = nota.insertar()
            if(resultado){
                Toast.makeText(this, "SE CREÓ LA NOTA", Toast.LENGTH_LONG).show()
                nombreArchivo.setText("")
                contenido.setText("")

                finish()
            }else{
                Toast.makeText(this, "ERROR AL CREAR LA NOTA", Toast.LENGTH_LONG).show()
            }
        }

        regresar.setOnClickListener {
            finish()
        }
    }
}