package mx.edu.ittepic.u3_practica2_basededatosfirebasecloudfirestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main2.contenido
import kotlinx.android.synthetic.main.activity_main2.nombreArchivo
import kotlinx.android.synthetic.main.activity_main3.*

class MainActivity3 : AppCompatActivity() {
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        var extra = intent.extras
        id = extra!!.getString("idActualizar")!!

        //RECUPERAR LA DATA
        val nota = Nota(this).consulta(id)
        nombreArchivo.setText(nota.titulo)
        contenido.setText(nota.contenido)

        btn_editar.setOnClickListener {
            val notaActualizar = Nota(this)
            notaActualizar.titulo = nombreArchivo.text.toString()
            notaActualizar.contenido = contenido.text.toString()

            val resultado = notaActualizar.actualizar(id)
            if(resultado){
                Toast.makeText(this,"SE ACTUALIZÓ LA NOTA", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"ERROR AL ACTUALIZAR", Toast.LENGTH_LONG).show()
            }
        }

        btn_regresar.setOnClickListener {
            finish()
        }
    }
}