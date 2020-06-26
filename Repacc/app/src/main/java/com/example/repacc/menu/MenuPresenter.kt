package com.example.repacc.menu

import android.content.Context

interface MenuPresenter {
    fun onCreate()
    fun onDestroy()

    fun cambiarEstado(context: Context, disponible: Boolean)
}