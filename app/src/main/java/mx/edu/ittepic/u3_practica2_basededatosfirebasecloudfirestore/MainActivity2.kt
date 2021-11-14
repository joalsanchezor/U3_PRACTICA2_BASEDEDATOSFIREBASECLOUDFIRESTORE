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

        val guardar = findViewById<ImageView>(R.id.btn_nueva_nueva)
        var nombreArchivo = ""

        guardar.setOnClickListener {
            //ASIGNAR EL NOMBRE DE LA NOTA
                nombreArchivo = findViewById<EditText>(R.id.nombreArchivo).text.toString()
                if(nombreArchivo.equals("")){
                    nombreArchivo = UUID.randomUUID().toString()
                }
        }
    }
}