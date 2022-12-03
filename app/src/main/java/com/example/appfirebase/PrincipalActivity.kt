package com.example.appfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PrincipalActivity : AppCompatActivity() {
    private lateinit var tvEmail: TextView
    private lateinit var tvProveedor: TextView
    private lateinit var btnCerrar: Button

    private lateinit var etNombre: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etMunicipio: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnConsultar: Button
    private lateinit var btnBorrar: Button
    val bd = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        tvEmail = findViewById(R.id.tv_Email)
        tvProveedor = findViewById(R.id.tv_Proveedor)
        btnCerrar = findViewById(R.id.btn_Cerrar)

        etNombre = findViewById(R.id.et_Nombre)
        etTelefono = findViewById(R.id.et_Telefono)
        etMunicipio = findViewById(R.id.et_Municipio)
        btnGuardar = findViewById(R.id.btn_Guardar)
        btnConsultar = findViewById(R.id.btn_Consultar)
        btnBorrar = findViewById(R.id.btn_Borrar)


        val bundle:Bundle? = intent.extras
        val email:String? = bundle?.getString("email")
        val proveedor:String? = bundle?.getString("proveedor")

        setup(email?:"",proveedor?:"")
    }
    fun setup(email:String, proveedor:String) {
        title = "Principal"
        tvEmail.text = email
        tvProveedor.text = proveedor

        btnCerrar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        btnGuardar.setOnClickListener {
            bd.collection("usuarios").document(email).set(
                hashMapOf("proveedor" to proveedor,
                    "nombre" to etNombre.text.toString(),
                    "telefono" to etTelefono.text.toString(),
                    "municipio" to etMunicipio.text.toString()
                )
            )
        }

        btnConsultar.setOnClickListener {
            bd.collection("usuarios").document(email).get().addOnSuccessListener {
                etNombre.setText(it.getString("nombre") as String?)
                etTelefono.setText(it.getString("telefono") as String?)
                etMunicipio.setText(it.getString("municipio") as String?)
            }


            btnBorrar.setOnClickListener {
                bd.collection("usuarios").document(email).delete()
            }
        }
    }
}