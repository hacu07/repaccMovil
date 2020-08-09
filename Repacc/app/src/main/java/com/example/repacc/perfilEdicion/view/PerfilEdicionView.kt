package com.example.repacc.perfilEdicion.view

import android.content.Context
import com.example.repacc.pojo.Departamento
import com.example.repacc.pojo.Municipio
import com.example.repacc.pojo.Pais
import com.example.repacc.pojo.Usuario

interface PerfilEdicionView {
    fun habilitarCampos(habilitar: Boolean)
    fun mostrarProgreso(mostrar: Boolean)
    fun mostrarMsj(msj:String)
    fun cargarDatos(usuario: Usuario)
    fun cargarTipoSangre(position: Int)
    fun configSpiRH(listaRH: List<String>)
    fun getContext(): Context

    fun cargarPaises(paises: List<String>)
    fun cargarDepartamentos(departamentos: List<String>)
    fun cargarMunicipios(municipios:List<String>)
    //fun seleccionarDepartamento(index: Int)
}