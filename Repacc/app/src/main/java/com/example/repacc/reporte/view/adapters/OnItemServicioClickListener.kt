package com.example.repacc.reporte.view.adapters

import com.example.repacc.pojo.Tipo

interface OnItemServicioClickListener {
    fun onClickServicioListener(isChecked: Boolean, servicio: Tipo)
}