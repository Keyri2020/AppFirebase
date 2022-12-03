package com.example.appfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var edtEmail: EditText
    private lateinit var edtPass: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var btnLogin: Button

    private lateinit var llAutentificar: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtEmail = findViewById(R.id.et_Email)
        edtPass = findViewById(R.id.et_Pass)
        btnRegistrar = findViewById(R.id.btn_Registrar)
        btnLogin = findViewById(R.id.btn_Login)

        ejecutarAnalitica()
        setup()
    }
    fun ejecutarAnalitica(){
        val analisis:FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("mensaje","integracion en firebase completa")
        analisis.logEvent("InitScreen",bundle)
    }

    fun setup() {
        title = "Autentificacion"
        btnRegistrar.setOnClickListener {
            if (edtEmail.text.isNotEmpty() && edtPass.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    edtEmail.text.toString(), edtPass.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        mostrarPrincipal(it?.result?.user?.email?: "", TiposProveedor.BASICO)
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    } else {
                        mostrarAlerta()
                    }
                }
            }
        }
        btnLogin.setOnClickListener {
            if (edtEmail.text.isNotEmpty() && edtPass.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    edtEmail.text.toString(), edtPass.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        mostrarPrincipal(it?.result?.user?.email ?: "", TiposProveedor.BASICO)
                    } else {
                        mostrarAlerta()
                    }
                }
            }
        }
    }

    fun mostrarAlerta(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error de conexion")
        builder.setMessage("Se ha producido un error de autentificacion de usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog = builder.create()
        dialog.show()
    }

    fun mostrarPrincipal(email:String, proveedor:TiposProveedor){
        val ventada: Intent = Intent(this, PrincipalActivity::class.java).apply {
            putExtra("email",email)
            putExtra("proveedor",proveedor)
        }
        startActivity(ventada)
    }

    enum class TiposProveedor{
        BASICO
    }
}