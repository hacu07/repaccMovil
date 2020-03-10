package com.example.repacc.perfilEdicion.view

import com.example.repacc.pojo.Departamento
import com.example.repacc.pojo.Municipio
import com.example.repacc.pojo.Pais

interface PerfilEdicionView {
    fun habilitarCampos(habilitar: Boolean)
    fun mostrarProgreso(mostrar: Boolean)
    fun mostrarMsj(msj:String)

    fun cargarPaises(paises: List<String>)
    fun cargarDepartamentos(departamentos: List<String>)
    fun cargarMunicipios(municipios:List<String>)
}