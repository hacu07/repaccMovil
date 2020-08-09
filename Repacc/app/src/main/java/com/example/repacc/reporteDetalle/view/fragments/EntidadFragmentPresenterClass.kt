package com.example.repacc.reporteDetalle.view.fragments

import android.content.Context
import com.example.repacc.pojo.Entidad
import com.example.repacc.reporteDetalle.view.fragments.events.EntidadesEvent
import com.example.repacc.reporteDetalle.view.fragments.model.EntidadFragmentModel
import com.example.repacc.reporteDetalle.view.fragments.model.EntidadFragmentModelClass
import com.example.repacc.reporteDetalle.view.fragments.view.ClinicaFragment
import com.example.repacc.reporteDetalle.view.fragments.view.ClinicaFragmentView
import com.example.repacc.util.Util
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class EntidadFragmentPresenterClass: EntidadFragmentPresenter {

    private var mView: ClinicaFragmentView? = null
    private lateinit var mModel: EntidadFragmentModel

    constructor(mView: ClinicaFragment){
        this.mView = mView
        mModel = EntidadFragmentModelClass()
    }

    override fun onShow() {
        if (mView != null)
            EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        mView = null
    }

    override fun buscarClinicasMunicipio(context: Context, idMunicipio: String) {
        if (mView != null){
            mView?.mostrarProgreso(true)
            mView?.habilitarElementos(false)
            mModel.buscarClinicasMunicipio(context,idMunicipio)
        }
    }

    @Subscribe
    override fun onEventListener(event: EntidadesEvent){
        if(mView != null && event != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarElementos(true)
            when(event.typeEvent){
                Util.SUCCESS -> cargarEntidades(event.content)
                else -> mView?.ocultarSpinnerEntidades()
            }
        }
    }

    private fun cargarEntidades(entidades: ArrayList<Entidad>?) {
        val nombresEntidades = mutableListOf<String>()

        entidades?.forEach { entidad ->
            nombresEntidades.add(entidad.nombre)
        }

        mView?.cargarEntidades(entidades, nombresEntidades)
    }
}