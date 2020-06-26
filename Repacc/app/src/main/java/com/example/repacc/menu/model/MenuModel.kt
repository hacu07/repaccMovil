package com.example.repacc.menu.model

import android.content.Context

interface MenuModel {
    fun cambiarEstado(context: Context, disponible: Boolean)
}