package com.example.repacc.reporteDetalle.view.fragments.callback

import com.example.repacc.pojo.Entidad

interface InfoAtendidoCallback {
    fun atendidoCallback(unidadMedica: Entidad?, descripTraslado: String?)
}