package com.example.wcf.registro.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.wcf.R
import com.example.wcf.Util.Util
import com.example.wcf.pojo.Cliente
import com.example.wcf.registro.RegistroPresenter
import com.example.wcf.registro.RegistroPresenterClass
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegistroActivity : AppCompatActivity(), RegistroView {

    lateinit var mPresenter: RegistroPresenter

    lateinit var tilCedulaEmisor: TextInputLayout
    lateinit var tilNombreEmisor: TextInputLayout
    lateinit var tilCedulaDestinatario: TextInputLayout
    lateinit var tilNombreDestinatario: TextInputLayout
    lateinit var tilPeso: TextInputLayout
    lateinit var tilSeguro: TextInputLayout
    lateinit var tilValor: TextInputLayout

    lateinit var etCedulaEmisor: TextInputEditText
    lateinit var etNombreEmisor: TextInputEditText
    lateinit var etCedulaDestinatario: TextInputEditText
    lateinit var etNombreDestinatario: TextInputEditText
    lateinit var etPeso: TextInputEditText
    lateinit var etSeguro: TextInputEditText
    lateinit var etValor: TextInputEditText

    lateinit var btnBuscarEmisor: ImageButton
    lateinit var btnBuscarDestinatario: ImageButton

    lateinit var spiCiudadOrigen: Spinner
    lateinit var spiCiudadDestino: Spinner

    lateinit var fabGuardarEnvio: FloatingActionButton
    lateinit var progresoRegistro: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        inicializar()

        mPresenter = RegistroPresenterClass(this)
        mPresenter.onCreate()
        mPresenter.obtenerPesos(this)
        eventosPrecio()
    }

    override fun onDestroy() {
        mPresenter.onDestroy()
        super.onDestroy()
    }

    private fun inicializar() {
        tilCedulaEmisor = findViewById(R.id.tilCedulaEmisor)
        tilNombreEmisor = findViewById(R.id.tilNombreEmisor)
        tilCedulaDestinatario = findViewById(R.id.tilCedulaDestinatario)
        tilNombreDestinatario = findViewById(R.id.tilNombreDestinatario)
        tilPeso = findViewById(R.id.tilPeso)
        tilSeguro = findViewById(R.id.tilSeguro)
        tilValor = findViewById(R.id.tilValor)
        etCedulaEmisor = findViewById(R.id.etCedulaEmisor)
        etNombreEmisor = findViewById(R.id.etNombreEmisor)
        etCedulaDestinatario = findViewById(R.id.etCedulaDestinatario)
        etNombreDestinatario = findViewById(R.id.etNombreDestinatario)
        etPeso = findViewById(R.id.etPeso)
        etSeguro = findViewById(R.id.etSeguro)
        etValor = findViewById(R.id.etValor)
        btnBuscarEmisor = findViewById(R.id.btnBuscarEmisor)
        btnBuscarDestinatario = findViewById(R.id.btnBuscarDestinatario)
        spiCiudadOrigen = findViewById(R.id.spiCiudadOrigen)
        spiCiudadDestino = findViewById(R.id.spiCiudadDestino)
        fabGuardarEnvio = findViewById(R.id.fabGuardarEnvio)
        progresoRegistro = findViewById(R.id.progresoRegistro)

        tilNombreEmisor.isEnabled = false
        tilNombreDestinatario.isEnabled = false
        tilValor.isEnabled = false
    }

    private fun eventosPrecio() {

        etPeso.setOnFocusChangeListener(object: View.OnFocusChangeListener{
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                //Perdio el foco
                if (!hasFocus){
                    calcularValor()
                }
            }
        })

        etSeguro.setOnFocusChangeListener(object: View.OnFocusChangeListener{
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                //Perdio el foco
                if (!hasFocus){
                    calcularValor()
                }
            }
        })

    }

    private fun calcularValor() {
        val precioki: Int = mPresenter.pesosKl()

        if (!etPeso.text.isNullOrEmpty()){
            val peso = if (!etPeso.text.isNullOrEmpty()) etPeso.text.toString().toInt() else 0
            val seguro = if (!etSeguro.text.isNullOrEmpty()) etSeguro.text.toString().toInt() else 0
            val total = ((precioki * peso) + (seguro * 0.2)).toInt()
            etValor.setText(total.toString())
        }else{
            etValor.setText("0")
        }
    }

    fun guardarEnvio(view: View){
        tilPeso.isErrorEnabled = false
        tilCedulaEmisor.isErrorEnabled = false
        tilCedulaDestinatario.isErrorEnabled = false
        tilSeguro.isErrorEnabled = false
        tilValor.isErrorEnabled = false

        calcularValor()
        mPresenter.registrarEnvio(
            context = this,
            pesoKilos = if (etPeso.text.isNullOrEmpty()) 0 else etPeso.text.toString().toInt(),
            valorSeguro = if (etSeguro.text.isNullOrEmpty()) 0 else etSeguro.text.toString().toInt(),
            valorEnvio =  if (etValor.text.isNullOrEmpty()) 0 else etValor.text.toString().toInt()
        )
    }

    fun buscarEmisor(view: View){
        if (!etCedulaEmisor.text.isNullOrEmpty()){
            mPresenter.buscarCliente(
                this,
                Cliente(cedula = etCedulaEmisor.text.toString().trim()),
                Util.CONSULTA_CLIENTE_EMISOR
            )
        }else{
            mostarErrorClienteEmisor("Ingrese cedula")
        }
    }

    fun buscarDestinatario(view: View){
        if (!etCedulaDestinatario.text.isNullOrEmpty()){
            mPresenter.buscarCliente(
                this,
                Cliente(cedula = etCedulaDestinatario.text.toString().trim()),
                Util.CONSULTA_CLIENTE_DESTINO
            )
        }else{
            mostarErrorClienteDestino("Ingrese cedula")
        }
    }

    /*
    * INTERFACE VIEW
    * */

    override fun salir() {

    }

    override fun limpiar() {
        etCedulaEmisor.setText("")
        etNombreEmisor.setText("")
        etCedulaDestinatario.setText("")
        etNombreDestinatario.setText("")
        etPeso.setText("")
        etSeguro.setText("")
        etValor.setText("")
    }

    override fun habilitarElementos(habilita: Boolean) {
        tilCedulaEmisor.isEnabled = habilita
        tilCedulaDestinatario.isEnabled = habilita
        tilPeso.isEnabled = habilita
        tilSeguro.isEnabled = habilita
        btnBuscarDestinatario.isEnabled = habilita
        btnBuscarEmisor.isEnabled = habilita
        fabGuardarEnvio.isEnabled = habilita
        spiCiudadDestino.isEnabled = habilita
        spiCiudadOrigen.isEnabled = habilita
    }

    override fun mostrarProgreso(muestra: Boolean) {
        progresoRegistro.visibility = if (muestra) View.VISIBLE else View.GONE
    }

    override fun mostrarMsj(msj: String) {
        Util.mostrarMsj(this, msj)
    }

    override fun mostrarErrorPeso(msj: String) {
        tilPeso.error = msj
    }

    override fun mostrarErrorValorEnvio(msj: String) {
        tilValor.error = msj
    }

    override fun mostrarErrorSeguro(msj: String) {
        tilSeguro.error = msj
    }

    override fun mostarErrorClienteEmisor(msj: String) {
        tilCedulaEmisor.error = msj
    }

    override fun mostarErrorClienteDestino(msj: String) {
        tilCedulaDestinatario.error = msj
    }

    override fun mostarErrorCiudadOrigen(msj: String) {
        Util.mostrarMsj(this,msj)
    }

    override fun mostarErrorCiudadDestino(msj: String) {
        mostrarMsj(msj)
    }

    override fun cargarCiudadesOrigen(ciudadesOrigen: List<String>) {
        spiCiudadOrigen.adapter = obtenerArrayAdapter(ciudadesOrigen)

        spiCiudadOrigen.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mPresenter.cargarCiudadesDestino(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    override fun cargarCiudadesDestino(lCiudadesdestino: List<String>) {
        spiCiudadDestino.adapter = obtenerArrayAdapter(lCiudadesdestino)

        spiCiudadDestino.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mPresenter.asignarCiudadDestino(position)
                calcularValor()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun obtenerArrayAdapter(lista: List<String>): SpinnerAdapter? {
        val dataAdapter:ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, lista)

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        return dataAdapter
    }

    override fun asignarNombreCliente(content: Cliente?, tipoCliente: Int) {
        when(tipoCliente){
            Util.CONSULTA_CLIENTE_EMISOR -> etCedulaEmisor.setText("${content?.nombres} ${content?.apellidos}")
            Util.CONSULTA_CLIENTE_DESTINO-> etCedulaDestinatario.setText("${content?.nombres} ${content?.apellidos}")
        }
    }

    override fun limpiarNombreCliente(tipoCliente: Int) {
        when(tipoCliente){
            Util.CONSULTA_CLIENTE_EMISOR -> etCedulaEmisor.setText("")
            Util.CONSULTA_CLIENTE_DESTINO-> etCedulaDestinatario.setText("")
        }
    }
}
