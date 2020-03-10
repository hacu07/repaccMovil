package com.example.repacc.perfil

import com.example.repacc.perfil.view.PerfilActivity
import com.example.repacc.perfil.view.PerfilView
import com.example.repacc.util.Constantes

class PerfilPresenterClass : PerfilPresenter {

    private var mView:PerfilView? = null;

    constructor(mView: PerfilActivity){
        this.mView = mView
    }

    override fun cargarDatos() {
        if (mView != null){
            Constantes.config?.usuario?.let {
                mView?.cargarDatos(it)
            }
        }
    }

    override fun onCreate() {

    }

    override fun onDestroy() {

    }


}