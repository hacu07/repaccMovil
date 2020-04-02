package com.example.wcf.registro.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.wcf.R
import com.example.wcf.registro.RegistroPresenter

class RegistroActivity : AppCompatActivity(), RegistroView {

    lateinit var mPresenter: RegistroPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
    }

    fun guardarEnvio(view: View){

    }

    fun buscarEmisor(view: View){

    }

    fun buscarDestinatario(view: View){

    }

    /*
    * INTERFACE VIEW
    * */

    override fun salir() {
        finish()
    }

    override fun limpiar() {

    }

    override fun habilitarElementos(habilita: Boolean) {

    }

    override fun mostrarProgreso(muestra: Boolean) {

    }

    override fun mostrarMsj(msj: String) {

    }

    override fun mostrarErrorPeso(msj: String) {

    }

    override fun mostrarErrorValorEnvio(msj: String) {

    }

    override fun mostrarErrorSeguro(msj: String) {

    }

    override fun mostarErrorClienteEmisor(msj: String) {

    }

    override fun mostarErrorClienteDestino(msj: String) {

    }

    override fun mostarErrorCiudadOrigen(msj: String) {

    }

    override fun mostarErrorCiudadDestino(msj: String) {

    }

    override fun cargarCiudadesOrigen(ciudadesOrigen: List<String>) {

    }
}
