package com.example.wcf.registro

import android.content.Context
import com.example.wcf.Util.Util
import com.example.wcf.pojo.Ciudad
import com.example.wcf.pojo.Cliente
import com.example.wcf.pojo.Envios
import com.example.wcf.pojo.Pesos
import com.example.wcf.registro.event.ClienEvent
import com.example.wcf.registro.event.PesosEvent
import com.example.wcf.registro.event.RegistroEvent
import com.example.wcf.registro.model.RegistroModel
import com.example.wcf.registro.model.RegistroModelClass
import com.example.wcf.registro.view.RegistroActivity
import com.example.wcf.registro.view.RegistroView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class RegistroPresenterClass: RegistroPresenter {

    var mModel: RegistroModel
    var mView: RegistroView? = null

    var tipoCliente = 0 // 1 = cliete_emi - 2 = cliente_des
    var cliente_emi: Cliente? = null
    var cliente_des: Cliente? = null

    var pesos: ArrayList<Pesos> =  ArrayList()
    var ciud_orig: Ciudad? = null
    var ciud_dest: Ciudad? = null
    var pesosKl = 0

    var ciudadesDestino : ArrayList<Ciudad>? = null

    constructor(mView: RegistroActivity){
        this.mView = mView
        mModel = RegistroModelClass()
    }

    override fun onCreate() {
        if (mView != null){
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        mView = null
    }

    override fun obtenerPesos(context: Context) {
        if (mView != null){
            mView?.habilitarElementos(false)
            mView?.mostrarProgreso(true)
            mModel.obtenerPesos(context)
        }
    }

    override fun buscarCliente(context: Context, cliente: Cliente, tipoCliente: Int) {
        if (mView != null){
            this.tipoCliente = tipoCliente
            mView?.mostrarProgreso(true)
            mView?.habilitarElementos(false)
            mModel.buscarCliente(context,cliente)
        }
    }

    override fun registrarEnvio(
        context: Context,
        pesoKilos: Int,
        valorSeguro: Int,
        valorEnvio: Int
    ) {
        if (mView != null){
            if (validarDatosRegistro(pesoKilos,valorSeguro,valorEnvio)){
                mView?.habilitarElementos(false)
                mView?.mostrarProgreso(true)

                var envios = Envios(
                    cedula_emi = cliente_emi!!,
                    cedula_des = cliente_des!!,
                    ciud_orig = ciud_orig!!,
                    ciud_dest = ciud_dest!!,
                    peso = pesoKilos,
                    valor_aseg = valorSeguro,
                    preciokl = obtenerPrecioKl(),
                    valor_envi = valorEnvio
                )

                mModel.registrarEnvio(context, envios)
            }
        }
    }

    private fun obtenerPrecioKl(): Int {
        var precioKl = 0
        val pesoActual = pesos.find {
                pesos ->
            pesos.ciud_orig.equals(ciud_orig) && pesos.ciud_dest.equals(ciud_dest)
        }

        if (pesoActual != null){
            precioKl = pesoActual?.preciokl
        }

        return precioKl
    }

    private fun validarDatosRegistro(pesoKilos: Int, valorSeguro: Int, valorEnvio: Int): Boolean {
        var esValido = true

        if (pesoKilos <= 0){
            esValido = false
            mView?.mostrarErrorPeso("Ingrese el peso del paquete.")
        }

        if(valorSeguro < 0){
            esValido = false
            mView?.mostrarErrorSeguro("Valor de seguro no permitido.")
        }

        if (valorEnvio <= 0){
            esValido = false
            mView?.mostrarErrorValorEnvio("Valor de envio no permitido.")
        }

        if (cliente_emi == null){
            esValido = false
            mView?.mostarErrorClienteEmisor("Cliente emisor no asignado.")
        }

        if (cliente_des == null){
            esValido = false
            mView?.mostarErrorClienteDestino("Cliente destino no asignado.")
        }

        if (ciud_orig == null){
            esValido = false
            mView?.mostarErrorCiudadOrigen("Ciudad ORIGEN no asignada.")
        }

        if (ciud_dest == null){
            esValido = false
            mView?.mostarErrorCiudadDestino("Ciudad DESTINO no asignada.")
        }

        return esValido
    }

    @Subscribe
    fun onEventListenerPesos(event: PesosEvent){
        if (mView != null){
            mView?.mostrarProgreso(false)

            if (event != null){
                when(event.typeEvent){
                    Util.SUCCESS -> {
                        asignarValoresSpinner(event.content)
                        mView?.habilitarElementos(true)
                    }
                    else -> {
                        mView?.habilitarElementos(false)
                        event.msj?.let { mView?.mostrarMsj(it) }
                    }
                }
            }else{
                mView?.mostrarMsj("No se logró obtener las ciudades")
            }
        }
    }

    private fun asignarValoresSpinner(pesos: ArrayList<Pesos>?) {
        this.pesos = pesos!!
        val ciudadesOrigen = mutableListOf<String>()
        pesos?.forEach {
            ciudadesOrigen.add(it.ciud_orig.nombre)
        }
        mView?.cargarCiudadesOrigen(ciudadesOrigen)
    }

    override fun cargarCiudadesDestino(posicion: Int) {
        ciud_orig = pesos[posicion].ciud_orig
        val l_ciudadesDestino = mutableListOf<String>()
        this.ciudadesDestino = ArrayList()

        pesos.forEach {
            // Obtiene las ciudades de destino que se relaciones con la de origen seleccionada
            if (it.ciud_orig.equals(ciud_orig)){
                this.ciudadesDestino?.add(it.ciud_dest)
                l_ciudadesDestino.add(it.ciud_dest.nombre)
            }
        }

        mView?.cargarCiudadesDestino(l_ciudadesDestino)
    }

    override fun asignarCiudadDestino(posicion: Int) {
        ciud_dest = this.ciudadesDestino?.get(posicion)
        //Obtiene el pesoKl segun las ciudades seleccionadas
        val peso = pesos.find{
            it.ciud_orig.equals(ciud_orig) && it.ciud_dest.equals(ciud_dest)
        }

        if (peso != null)
            pesosKl = peso.preciokl
    }

    override fun pesosKl(): Int {
        return pesosKl
    }

    @Subscribe
    fun onEventClienEvent(clienEvent: ClienEvent){
        if(mView != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarElementos(true)
            clienEvent.msj?.let { mView?.mostrarMsj(it) }

            when(clienEvent.typeEvent){
                Util.SUCCESS -> {
                    if (tipoCliente == Util.CONSULTA_CLIENTE_EMISOR) //cliente_emi
                        cliente_emi = clienEvent.content
                    else
                        cliente_des = clienEvent.content


                    mView?.asignarNombreCliente(clienEvent.content, tipoCliente)
                }
                else -> {
                    if (tipoCliente == Util.CONSULTA_CLIENTE_EMISOR) //cliente_emi
                        cliente_emi = null
                    else
                        cliente_des = null

                    mView?.limpiarNombreCliente(tipoCliente)
                }
            }
        }
    }

    @Subscribe
    fun onEventListenerRegistro(registroEvent: RegistroEvent){
        if(mView != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarElementos(true)
            registroEvent.msj?.let { mView?.mostrarMsj(it) }

            when(registroEvent.typeEvent){
                Util.SUCCESS -> {
                    mView?.mostrarMsj("GUIA NUMERO: ${registroEvent.content?.cod_guia}")
                    cliente_emi = null
                    cliente_des = null
                    ciud_orig = null
                    ciud_dest = null
                    mView?.limpiar()
                }
            }
        }
    }
}