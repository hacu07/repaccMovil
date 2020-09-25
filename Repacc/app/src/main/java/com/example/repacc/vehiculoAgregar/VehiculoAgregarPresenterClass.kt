package com.example.repacc.vehiculoAgregar

import android.content.Context
import android.net.Uri
import com.example.repacc.R
import com.example.repacc.pojo.*
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import com.example.repacc.vehiculoAgregar.events.MarcaEvent
import com.example.repacc.vehiculoAgregar.events.ModeloEvent
import com.example.repacc.vehiculoAgregar.events.TipoEvent
import com.example.repacc.vehiculoAgregar.events.VehiculoEvent
import com.example.repacc.vehiculoAgregar.model.VehiculoAgregarModel
import com.example.repacc.vehiculoAgregar.model.VehiculosAgregarModelClass
import com.example.repacc.vehiculoAgregar.view.VehiculoAgregarActivity
import com.example.repacc.vehiculoAgregar.view.VehiculoAgregarView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class VehiculoAgregarPresenterClass: VehiculoAgregarPresenter {

    private var mView: VehiculoAgregarView?
    private lateinit var mModel: VehiculoAgregarModel

    private lateinit var listaTipos: ArrayList<Tipo>
    private lateinit var listaMarcas: ArrayList<Marca>
    private lateinit var listaModelos: ArrayList<Modelo>

    private var tipoVeh: Tipo? = null
    private var marcaVeh: Marca? = null
    private var modelVeh: Modelo? = null
    private var vehiculo: Vehiculo? = null

    constructor(mView: VehiculoAgregarActivity){
        this.mView = mView
        mModel = VehiculosAgregarModelClass()
    }

    override fun onCreate() {
        if (mView != null){
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        mView = null
        EventBus.getDefault().unregister(this)
    }

    override fun obtenerTipos(context: Context) {
        if (mView != null){
            mView?.mostrarProgreso(true)
            mView?.habilitarElementos(false)
            mModel.obtenerTipos(context)
        }
    }

    override fun obtenerMarcas(context: Context, position: Int) {
        if (mView != null){
            tipoVeh = listaTipos.get(position)
            mView?.habilitarElementos(false)
            mView?.mostrarProgreso(true)
            mModel.obtenerMarcas(context,tipoVeh!!._id)
        }
    }

    override fun obtenerModelos(context: Context, position: Int) {
        if (mView != null){
            marcaVeh = listaMarcas.get(position)
            mView?.habilitarElementos(false)
            mView?.mostrarProgreso(true)
            mModel.obtenerModelos(context,marcaVeh!!._id)
        }
    }

    override fun asignarModelo(position: Int) {
        modelVeh = listaModelos.get(position)
    }

    override fun registrarVehiculo(
        context: Context,
        placa: String,
        esParticular: Boolean,
        colores: Array<String>,
        mPhotoSelectedUri: Uri?
    ) {
        if (mView != null){

            if (tipoVeh != null && marcaVeh != null && modelVeh != null){
                mView?.mostrarProgreso(true)
                mView?.habilitarElementos(false)
                val vehiculo = Vehiculo(
                    usuario = Constantes.config!!.usuario!!,
                    tipo = tipoVeh!!,
                    esParticular = esParticular,
                    modelo = modelVeh!!,
                    colores = colores,
                    placa = placa
                )
                this.vehiculo = vehiculo
                mModel.registroVehiculo(context,vehiculo, mPhotoSelectedUri)
            }else{
                mView?.mostrarMsj(context.getString(R.string.falta_datos))
            }
        }
    }

    @Subscribe
    fun onTiposListener(tipoEvent: TipoEvent){
        if (mView != null){
            mView?.mostrarProgreso(false)

            when(tipoEvent.typeEvent){
                Util.SUCCESS -> cargarTipos(tipoEvent.content)
                else -> mView?.mostrarMsj(tipoEvent.msj!!)
            }
        }
    }

    private fun cargarTipos(listaTipos: ArrayList<Tipo>?) {
        mView?.habilitarElementos(true)
        this.listaTipos = listaTipos!!
        mView?.cargarSpiTipos(obtenerNombresTipos(this.listaTipos))
    }

    private fun obtenerNombresTipos(listaTipos: ArrayList<Tipo>): List<String> {
        val nombresTipos = mutableListOf<String>()
        listaTipos.forEach {nombresTipos.add(it.nombre)}
        return nombresTipos
    }

    @Subscribe
    fun onMarcasListener(marcaEvent: MarcaEvent){
        if (mView != null){
            mView?.mostrarProgreso(false)

            when(marcaEvent.typeEvent){
                Util.SUCCESS -> cargarMarcas(marcaEvent.content)
                else -> mView?.mostrarMsj(marcaEvent.msj!!)
            }
        }
    }

    private fun cargarMarcas(listaMarcas: ArrayList<Marca>?) {
        mView?.habilitarElementos(true)
        this.listaMarcas = listaMarcas!!
        mView?.cargarSpiMarcas(obtenerNombresMarcas(this.listaMarcas))
    }

    private fun obtenerNombresMarcas(listaMarcas: ArrayList<Marca>): List<String> {
        val nombresMarcas = mutableListOf<String>()
        listaMarcas.forEach {nombresMarcas.add(it.nombre)}
        return nombresMarcas
    }

    @Subscribe
    fun onModelosListener(modeloEvent: ModeloEvent){
        if (mView != null){
            mView?.mostrarProgreso(false)

            when(modeloEvent.typeEvent){
                Util.SUCCESS -> cargarModelos(modeloEvent.content)
                else -> mView?.mostrarMsj(modeloEvent.msj!!)
            }
        }
    }

    private fun cargarModelos(listaModelos: ArrayList<Modelo>?) {
        mView?.habilitarElementos(true)
        this.listaModelos = listaModelos!!
        mView?.cargarSpiModelos(obtenerNombresModelos(this.listaModelos))
    }

    private fun obtenerNombresModelos(listaModelos: ArrayList<Modelo>): List<String> {
        val nombresModelos = mutableListOf<String>()
        listaModelos.forEach {nombresModelos.add(it.nombre)}
        return nombresModelos
    }

    @Subscribe
    fun onRegistroListener(event: VehiculoEvent){
        if (mView != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarElementos(true)
            mView?.mostrarMsj(event.msj!!)

            when(event.typeEvent){
                Util.SUCCESS -> {
                    mView?.agregarVehiculo(event.content!!)
                    mView?.finalizar()
                }
                else ->{
                    this.vehiculo = null
                }
            }
        }
    }
}