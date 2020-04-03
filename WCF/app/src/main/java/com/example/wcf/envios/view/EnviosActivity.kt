package com.example.wcf.envios.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.wcf.R
import com.example.wcf.Util.Util
import com.example.wcf.envios.EnvioPresenter
import com.example.wcf.envios.EnvioPresenterClass
import com.example.wcf.envios.events.EnvioEvent
import com.example.wcf.pojo.Envios
import kotlinx.android.synthetic.main.activity_envios.*

class EnviosActivity : AppCompatActivity(), EnvioView {

    lateinit var mPresenter: EnvioPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_envios)

        inicializar()
        mPresenter = EnvioPresenterClass(this)
        mPresenter.onCreate()
    }

    override fun onDestroy() {
        mPresenter.onDestroy()
        super.onDestroy()
    }

    private fun inicializar() {
        habilitarFormulario(false)
        tilCedulaEmisorEnvio.isEnabled = false
        tilNombreEmisorEnvio.isEnabled = false
        tilCedulaDestinoEnvio.isEnabled = false
        tilNombreDestinoEnvio.isEnabled = false
        tilCiudadOrigenEnvio.isEnabled = false
        tilCiudadDestinoEnvio.isEnabled = false
        tilPesoEnvio.isEnabled = false
        tilSeguroEnvio.isEnabled = false
        tilValorEnvio.isEnabled = false
    }

    fun buscarEnvio(view: View){
        if (!etNumeroGuia.text.isNullOrEmpty()){
            mPresenter.consultarEnvio(
                this,
                etNumeroGuia.text.toString().toInt()
                )
        }else{
            mostrarMsj("Ingrese numero de guia")
        }
    }

    fun cambiarEstadoEnvio(view: View){
        var cod_estado = 1

        if (rdPendiente.isChecked)
            cod_estado = 1

        if (rdRecolectado.isChecked)
            cod_estado = 2

        if (rdEnviado.isChecked)
            cod_estado = 3

        if (rdEntregado.isChecked)
            cod_estado = 4

        mPresenter.cambiarEstado(this, etNumeroGuia.text.toString().toInt(), cod_estado)
    }

    override fun habilitarElementos(habilita: Boolean) {
        tilNumeroGuia.isEnabled = habilita
        btnBuscarEnvio.isEnabled = habilita

        rdGroup.isEnabled = habilita
        fabGuardarEnvio.isEnabled = habilita
    }

    override fun mostrarProgreso(habilita: Boolean) {
        progresoEnvio.visibility = if (habilita) View.VISIBLE else View.GONE
    }

    override fun habilitarFormulario(habilita: Boolean) {
        tilNumeroGuia.isEnabled = !habilita
        btnBuscarEnvio.isEnabled = !habilita

        rdGroup.isEnabled = habilita
        rdPendiente.isEnabled = habilita
        rdRecolectado.isEnabled = habilita
        rdEnviado.isEnabled = habilita
        rdEntregado.isEnabled = habilita
        fabGuardarEnvio.isEnabled = habilita
    }

    override fun mostrarMsj(msj: String) {
        Util.mostrarMsj(this,msj)
    }

    override fun limpiar() {
        habilitarFormulario(false)
        etNumeroGuia.setText("")
        etCedulaEmisorEnvio.setText("")
        etNombreEmisorEnvio.setText("")
        etCedulaDestinatarioEnvio.setText("")
        etNombreEmisorEnvio.setText("")
        etCiudadOrigenEnvio.setText("")
        etCiudadDestinoEnvio.setText("")
        etPesoEnvio.setText("")
        etSeguroEnvio.setText("")
        etValorEnvio.setText("")
    }

    override fun cargarDatosEnvio(envio: Envios?) {
        etCedulaEmisorEnvio.setText(envio?.cedula_emi?.cedula.toString())
        etNombreEmisorEnvio.setText(envio?.cedula_emi?.nombres + " " + envio?.cedula_emi?.apellidos)
        etCedulaDestinatarioEnvio.setText(envio?.cedula_des?.cedula)
        etNombreEmisorEnvio.setText(envio?.cedula_des?.nombres + " " + envio?.cedula_des?.apellidos)
        etCiudadOrigenEnvio.setText(envio?.ciud_orig?.nombre)
        etCiudadDestinoEnvio.setText(envio?.ciud_dest?.nombre)
        etPesoEnvio.setText(envio?.peso.toString())
        etSeguroEnvio.setText(envio?.valor_aseg.toString())
        etValorEnvio.setText(envio?.valor_envi.toString())
    }
}
