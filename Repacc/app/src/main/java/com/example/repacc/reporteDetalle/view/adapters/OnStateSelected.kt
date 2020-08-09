package com.example.repacc.reporteDetalle.view.adapters

import com.example.repacc.pojo.Estado
import com.example.repacc.pojo.Servicio

interface OnStateSelected {
    fun onClickStateServiceListener(servicio: Servicio, position: Int, estado: Estado?= null)
}