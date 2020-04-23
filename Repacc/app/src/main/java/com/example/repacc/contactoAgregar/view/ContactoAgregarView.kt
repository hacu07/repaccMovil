package com.example.repacc.contactoAgregar.view

import com.example.repacc.pojo.Usuario
import java.util.ArrayList

interface ContactoAgregarView {
    fun habilitarElementos(habilita: Boolean)
    fun mostrarProgreso(mostrar: Boolean)

    fun mostrarMsj(msj: String)
    fun cargarUsuarios(usuarios: ArrayList<Usuario>)
    fun limpiar()
}