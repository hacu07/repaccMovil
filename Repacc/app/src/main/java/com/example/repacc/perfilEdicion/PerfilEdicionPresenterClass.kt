package com.example.repacc.perfilEdicion

import android.content.Context
import android.net.Uri
import com.example.repacc.R
import com.example.repacc.perfilEdicion.event.DepartamentoEvent
import com.example.repacc.perfilEdicion.event.EdicionPerfilEvent
import com.example.repacc.perfilEdicion.event.MunicipioEvent
import com.example.repacc.perfilEdicion.event.PaisEvent
import com.example.repacc.perfilEdicion.model.PerfilEdicionModel
import com.example.repacc.perfilEdicion.model.PerfilEdicionModelClass
import com.example.repacc.perfilEdicion.view.PerfilEdicionActivity
import com.example.repacc.perfilEdicion.view.PerfilEdicionView
import com.example.repacc.pojo.Departamento
import com.example.repacc.pojo.Municipio
import com.example.repacc.pojo.Pais
import com.example.repacc.pojo.Usuario
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class PerfilEdicionPresenterClass : PerfilEdicionPresenter {

    private var mView:PerfilEdicionView? = null
    private var mModel:PerfilEdicionModel? = null
    private var usuarioModif:Usuario? = Constantes.config?.usuario

    private var paises:ArrayList<Pais>? = null
    private var departamentos:ArrayList<Departamento>? = null
    private var municipios:ArrayList<Municipio>? = null

    private val listaRh = listOf("Seleccione","A-","A+","O-","O+","B-","B+","AB-","AB+")

    // Si selecciono la primera vez el departamento del usuario
    var siCargoMunNotifUser = false

    constructor(perfilView: PerfilEdicionActivity){
        this.mView = perfilView
        this.mModel = PerfilEdicionModelClass()
    }

    override fun cargarDatos() {
        if(mView != null){
            Constantes.config?.usuario?.let {
                mView?.mostrarProgreso(true)
                mView?.habilitarCampos(false)
                mView?.configSpiRH(listaRh)
                mView?.cargarDatos(it)
                mView?.cargarTipoSangre(listaRh.indexOf(it.tipoSangre))
            }
        }
    }

    override fun asignarDatosUsuarioModif(nombre: String, correo: String, celular: String) {
        usuarioModif.apply {
            this?.nombre = nombre
            this?.correo = correo
            this?.celular = celular
        }
    }

    override fun editarPerfil(context: Context, mPhotoSelectedUri: Uri?) {
        if (mView != null){
            if (usuarioModif?.tipoSangre != null)
                usuarioModif?.let {
                    mView?.mostrarProgreso(true)
                    mView?.habilitarCampos(false)
                    mModel?.editarPerfil(context, it, mPhotoSelectedUri)
                }
            else
                mView?.mostrarMsj(context.getString(R.string.no_rh))
        }
    }

    override fun asignarRH(position: Int) {
        if (position != 0)
            usuarioModif?.tipoSangre = listaRh[position]
    }

    override fun asignarMunicipio(position: Int) {
        usuarioModif?.munNotif = municipios?.get(position)
    }

    override fun onCreate() {
        if(mView != null){
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        mView = null
    }

    override fun obtenerPaises(context: Context) {
        mView.let {
            it?.mostrarProgreso(true)
            it?.habilitarCampos(false)
            mModel?.obtenerPaises(context)
        }
    }

    private fun obtenerIdPais(posicion: Int): String? {
        var idPais: String? = null
        idPais = paises?.get(posicion)?._id
        return idPais
    }

    private fun obtenerIdDpto(posicion: Int): String? {
        var idDpto: String? = null
        idDpto = departamentos?.get(posicion)?._id
        return idDpto
    }

    override fun obtenerMunicipios(context: Context, posicion: Int) {
        mView.let {
            var idDpto: String? = obtenerIdDpto(posicion)

            if (idDpto != null){
                mView?.habilitarCampos(false)
                mView?.mostrarProgreso(true)
                mModel?.obtenerCiudades(context, idDpto)
            }
        }
    }

    override fun obtenerDepartamentos(context: Context, posicion: Int) {
        mView.let {
            var idPais = obtenerIdPais(posicion);

            if (idPais != null){
                mView?.habilitarCampos(false)
                mView?.mostrarProgreso(true)
                mModel?.obtenerDepartamentos(context, idPais)
            }
        }
    }



    private fun obtenerNombresPaises(lista: List<Pais>): List<String> {
        val nombrePais= mutableListOf<String>()
        var posPaisNotif = -1
        var contador = 0
        lista.forEach {
            nombrePais.add(it.nombre)

            if (Constantes.config?.usuario?.munNotif?.departamento?.pais?.nombre != null && !siCargoMunNotifUser){
                if(it.nombre == (Constantes.config?.usuario?.munNotif?.departamento?.pais?.nombre))
                    posPaisNotif = contador
            }

            contador++
        }

        // Deja de primero el pais del usuario
        if (posPaisNotif != -1){
            nombrePais.remove(lista[posPaisNotif].nombre)
            nombrePais.add(0,lista[posPaisNotif].nombre)
            val paisSave = paises!![posPaisNotif]
            paises?.removeAt(posPaisNotif)
            paises?.add(0, paisSave)
        }

        return nombrePais
    }

    private fun obtenerNombresDptos(lista: List<Departamento>): List<String> {
        val nombreDpto= mutableListOf<String>()
        var posDptoNotif = -1
        var contador = 0
        lista.forEach {
            nombreDpto.add(it.nombre)
            if (Constantes.config?.usuario?.munNotif?.departamento?.nombre != null && !siCargoMunNotifUser){
                if(it.nombre == (Constantes.config?.usuario?.munNotif?.departamento?.nombre))
                    posDptoNotif = contador
            }
            contador++
        }

        // Deja de primero el dpto del usuario
        if (posDptoNotif != -1){
            nombreDpto.remove(lista[posDptoNotif].nombre)
            nombreDpto.add(0,lista[posDptoNotif].nombre)
            val dptoSave = departamentos!![posDptoNotif]
            departamentos?.removeAt(posDptoNotif)
            departamentos?.add(0, dptoSave)
        }

        return nombreDpto
    }

    private fun obtenerNombresMuni(lista: List<Municipio>): List<String> {
        val nombreMuncp= mutableListOf<String>()
        var posCityNotif = -1
        var contador = 0

        lista.forEach {
            nombreMuncp.add(it.nombre)
            if (Constantes.config?.usuario?.munNotif?.nombre != null && !siCargoMunNotifUser){
                if(it.nombre == (Constantes.config?.usuario?.munNotif?.nombre))
                    posCityNotif = contador
            }
            contador++
        }

        // Deja de primero el dpto del usuario
        if (posCityNotif != -1){
            nombreMuncp.remove(lista[posCityNotif].nombre)
            nombreMuncp.add(0,lista[posCityNotif].nombre)
            val citySave = municipios!![posCityNotif]
            municipios?.removeAt(posCityNotif)
            municipios?.add(0, citySave)
        }

        return nombreMuncp
    }

    @Subscribe
    public fun onEventPaisListener(paisEvent: PaisEvent){
        if (mView != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarCampos(true)

            when(paisEvent.typeEvent){
                Util.SUCCESS -> {
                    paises = paisEvent.content
                    paises?.let { mView?.cargarPaises(obtenerNombresPaises(it)) }
                }

                Util.ERROR_DATA,Util.ERROR_RESPONSE,Util.ERROR_CONEXION->
                        paisEvent.msj?.let { msj -> mView?.mostrarMsj(msj) }

            }
        }
    }

    @Subscribe
    public fun onEventDptoListener(dptoEvent: DepartamentoEvent){
        if(mView != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarCampos(true)

            when(dptoEvent.typeEvent){
                Util.SUCCESS -> {
                    departamentos = dptoEvent.content
                    departamentos?.let {
                        mView?.cargarDepartamentos(obtenerNombresDptos(it))
                    }
                }
                Util.ERROR_DATA,Util.ERROR_RESPONSE,Util.ERROR_CONEXION->
                    dptoEvent.msj?.let { msj -> mView?.mostrarMsj(msj) }

            }
        }
    }

    @Subscribe
    public fun onEventMunicipioListener(municipioEvent: MunicipioEvent){
        if(mView != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarCampos(true)

            when(municipioEvent.typeEvent){
                Util.SUCCESS -> {
                    municipios = municipioEvent.content
                    municipios?.let { mView?.cargarMunicipios(obtenerNombresMuni(it)) }
                }
                Util.ERROR_DATA,Util.ERROR_RESPONSE,Util.ERROR_CONEXION->
                    municipioEvent.msj?.let { msj -> mView?.mostrarMsj(msj) }
            }
        }
    }

    @Subscribe
    public fun onEventEditarPerfil(event: EdicionPerfilEvent){
        if (mView != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarCampos(true)

            when(event.typeEvent){
                Util.SUCCESS->{
                    Constantes.config?.usuario = event.content
                    mView?.getContext()?.let {
                        Util.guardarConfig(it)
                    }
                    event.msj?.let {
                        mView?.mostrarMsj(it)
                    }
                    event.content!!.foto.let {
                        mView?.cargarDatos(event.content!!)
                    }
                }
                Util.ERROR_DATA,Util.ERROR_RESPONSE,Util.ERROR_CONEXION->
                    event.msj?.let { mView?.mostrarMsj(it) }
            }
        }
    }

    /****************************************************
     * Indica si ya se cargó por primera vez el municipio del usuario
     * en los spinners de la UI
     * HAROLDC 01/08/2020
     */
    override fun siCargoMunNotifUsuario(siCargo: Boolean) {
        this.siCargoMunNotifUser = siCargo
    }
}