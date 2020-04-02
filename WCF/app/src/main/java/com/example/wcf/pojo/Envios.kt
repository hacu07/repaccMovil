package com.example.wcf.pojo

data class Envios(
     val cod_guia: Int = 0,
     val cedula_emi: Cliente,
     val cedula_des: Cliente,
     val ciud_orig: Ciudad,
     val ciud_dest: Ciudad,
     val peso :Int,
     val valor_aseg: Int,
     val preciokl: Int,
     val valor_envi: Int,
     val cod_estado: Estados = Estados(cod_estado = 1, descripcion = "PENDIENTE")
)