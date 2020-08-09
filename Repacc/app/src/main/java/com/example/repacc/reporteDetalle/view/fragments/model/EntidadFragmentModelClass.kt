package com.example.repacc.reporteDetalle.view.fragments.model

import android.content.Context
import com.example.repacc.reporteDetalle.view.fragments.events.EntidadesEvent
import com.example.repacc.reporteDetalle.view.fragments.model.DAO.DAO
import com.example.repacc.util.BasicCallback
import org.greenrobot.eventbus.EventBus

class EntidadFragmentModelClass : EntidadFragmentModel {

    val mDAO = DAO()

    override fun buscarClinicasMunicipio(context: Context, idMunicipio: String) {
        mDAO.buscarClinicasMunicipio(context, idMunicipio, object: BasicCallback{
            override fun response(event: Any) {
                postEntidades(event as EntidadesEvent)
            }
        })
    }

    fun postEntidades(entidadesEvent: EntidadesEvent) {
        EventBus.getDefault().post(entidadesEvent)
    }
}