package com.example.wcf.cliente.vista

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Switch
import com.example.wcf.R
import com.example.wcf.Util.Util
import com.example.wcf.cliente.ClientePresenter
import com.example.wcf.cliente.ClientePresenterClass
import com.example.wcf.pojo.Cliente
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_cliente.*
import kotlinx.android.synthetic.main.activity_cliente.view.*

class ClienteActivity : AppCompatActivity(), ClienteView {

    private lateinit var mPresenter: ClientePresenter

    private lateinit var tilCedulaCliente: TextInputLayout
    private lateinit var tilNombresCliente: TextInputLayout
    private lateinit var tilApellidosCliente: TextInputLayout
    private lateinit var tilFechaNacCliente: TextInputLayout
    private lateinit var tilDireccionCliente: TextInputLayout
    private lateinit var tilTelefonoCliente: TextInputLayout
    private lateinit var tilEmailCliente: TextInputLayout

    private lateinit var etCedulaCliente: TextInputEditText
    private lateinit var etNombresCliente: TextInputEditText
    private lateinit var etApellidosCliente: TextInputEditText
    private lateinit var etFechaNacCliente: TextInputEditText
    private lateinit var etDireccionCliente: TextInputEditText
    private lateinit var etTelefonoCliente: TextInputEditText
    private lateinit var etEmailCliente: TextInputEditText

    private lateinit var swEstadoCliente: Switch
    private lateinit var fabGuardarCliente: FloatingActionButton
    private lateinit var btnBuscarCliente: ImageButton

    private lateinit var progresoCliente: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente)

        inicializar()
        limpiarCampos()

        mPresenter = ClientePresenterClass(this)
        mPresenter.onCreate()
        habilitarBusqueda(true)
        habilitaFormulario(false)
        mostrarProgreso(false)
        mostrarEstado(false)
    }

    fun buscarCliente(view: View){
        mPresenter.buscarCliente(
            this,
            Cliente(
                cedula = etCedulaCliente.text.toString().trim()
            )
        )
    }

    fun cambiarEstado(view: View){
        mPresenter.cambiarEstado(
            this,
            Cliente(
                cedula = etCedulaCliente.text.toString().trim(),
                estado = if(swEstadoCliente.isChecked) 1 else 0
            )
        )
    }

    fun guardarCliente(view: View){
        var retur = false

        if (etNombresCliente.text.isNullOrEmpty()){
            retur = true
            tilNombresCliente.error = "Debe ingresar los nombres"
        }

        if (etApellidosCliente.text.isNullOrEmpty()){
            retur = true
            tilNombresCliente.error = "Debe ingresar los apellidos"
        }

        if (retur){
            return
        }

        mPresenter.guardarCliente(
            this,
            Cliente(
                cedula = etCedulaCliente.text.toString().trim(),
                estado = if(swEstadoCliente.isChecked) 1 else 0,
                nombres = etNombresCliente.text.toString().trim(),
                apellidos = etApellidosCliente.text.toString().trim(),
                fecha_nac = etFechaNacCliente.text.toString().trim(),
                direccion = etDireccionCliente.text.toString().trim(),
                telefono = etTelefonoCliente.text.toString().trim(),
                email = etEmailCliente.text.toString().trim()
            )
        )
    }

    override fun onDestroy() {
        mPresenter.onDestroy()
        super.onDestroy()
    }

    private fun inicializar() {
        tilCedulaCliente = findViewById(R.id.tilCedulaCliente)
        tilNombresCliente = findViewById(R.id.tilNombresCliente)
        tilApellidosCliente = findViewById(R.id.tilApellidosCliente)
        tilFechaNacCliente = findViewById(R.id.tilFechaNacCliente)
        tilDireccionCliente = findViewById(R.id.tilDireccionCliente)
        tilTelefonoCliente = findViewById(R.id.tilTelefonoCliente)
        tilEmailCliente = findViewById(R.id.tilEmailCliente)

        etCedulaCliente = findViewById(R.id.etCedulaCliente)
        etNombresCliente = findViewById(R.id.etNombresCliente)
        etApellidosCliente = findViewById(R.id.etApellidosCliente)
        etFechaNacCliente = findViewById(R.id.etFechaNacCliente)
        etDireccionCliente = findViewById(R.id.etDireccionCliente)
        etTelefonoCliente = findViewById(R.id.etTelefonoCliente)
        etEmailCliente = findViewById(R.id.etEmailCliente)

        swEstadoCliente = findViewById(R.id.swEstadoCliente)
        fabGuardarCliente = findViewById(R.id.fabGuardarCliente)
        btnBuscarCliente = findViewById(R.id.btnBuscarCliente)

        progresoCliente = findViewById(R.id.progresoCliente)
    }


    override fun habilitarElementos(habilita: Boolean) {
        tilCedulaCliente.isEnabled = habilita
        tilNombresCliente.isEnabled = habilita
        tilApellidosCliente.isEnabled = habilita
        tilFechaNacCliente.isEnabled = habilita
        tilDireccionCliente.isEnabled = habilita
        tilTelefonoCliente.isEnabled = habilita
        tilEmailCliente.isEnabled = habilita

        etCedulaCliente.isEnabled = habilita
        etNombresCliente.isEnabled = habilita
        etApellidosCliente.isEnabled = habilita
        etFechaNacCliente.isEnabled = habilita
        etDireccionCliente.isEnabled = habilita
        etTelefonoCliente.isEnabled = habilita
        etEmailCliente.isEnabled = habilita

        swEstadoCliente.isEnabled = habilita
        fabGuardarCliente.isEnabled = habilita
        btnBuscarCliente.isEnabled = habilita
    }

    override fun mostrarProgreso(habilita: Boolean) {
        progresoCliente.visibility = if(habilita) View.VISIBLE else View.GONE
    }

    override fun mostrarEstado(muestra: Boolean) {
        swEstadoCliente.visibility = if(muestra) View.VISIBLE else View.GONE
    }

    override fun mostrarMsj(msj: String) {
        Util.mostrarMsj(this,msj)
    }

    override fun cargarCliente(cliente: Cliente) {
        habilitarBusqueda(false)
        habilitaFormulario(true)
        mostrarEstado(true)
        swEstadoCliente.isChecked = cliente.estado == 1

        etNombresCliente.setText(cliente.nombres)
        etApellidosCliente.setText(cliente.apellidos)
        etFechaNacCliente.setText(cliente.fecha_nac)
        etDireccionCliente.setText(cliente.direccion)
        etTelefonoCliente.setText(cliente.telefono)
        etEmailCliente.setText(cliente.email)
    }

    override fun limpiarCampos() {
        mostrarProgreso(false)
        mostrarEstado(false)

        habilitarBusqueda(true)
        habilitaFormulario(false)

        etCedulaCliente.setText("")
        etNombresCliente.setText("")
        etApellidosCliente.setText("")
        etFechaNacCliente.setText("")
        etDireccionCliente.setText("")
        etTelefonoCliente.setText("")
        etEmailCliente.setText("")
    }

    override fun habilitarBusqueda(habilita: Boolean){
        tilCedulaCliente.isEnabled = habilita
        etCedulaCliente.isEnabled = habilita
        btnBuscarCliente.isEnabled = habilita
    }

    override fun habilitaFormulario(habilita: Boolean){
        tilNombresCliente.isEnabled = habilita
        tilApellidosCliente.isEnabled = habilita
        tilFechaNacCliente.isEnabled = habilita
        tilDireccionCliente.isEnabled = habilita
        tilTelefonoCliente.isEnabled = habilita
        tilEmailCliente.isEnabled = habilita

        etNombresCliente.isEnabled = habilita
        etApellidosCliente.isEnabled = habilita
        etFechaNacCliente.isEnabled = habilita
        etDireccionCliente.isEnabled = habilita
        etTelefonoCliente.isEnabled = habilita
        etEmailCliente.isEnabled = habilita

        swEstadoCliente.isEnabled = habilita
        fabGuardarCliente.isEnabled = habilita
    }
}
