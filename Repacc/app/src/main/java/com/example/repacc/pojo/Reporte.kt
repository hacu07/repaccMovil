package com.example.repacc.pojo

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class Reporte(
    val _id: String? = null,
    val codigo: String? = null,
    val placas: ArrayList<String>? = null,
    val latlong: String,
    val latitud: Double? = null,
    val longitud: Double? = null,
    val direccion: String? = null,
    val descripcion: String? = null,
    val estado: Estado? = null,
    val imagen: String?= null,
    val imgValid: Boolean = false,
    val numHeridos: Int = 0,
    val usuarioReg: Usuario? = null,
    val municipioReg: Municipio? = null,
    // Usadas en el envio para detectar en que municipio fue
    val pais: String? = null,
    val departamento: String? = null,
    val municipio: String? = null,
    // Agente que reportó la falsa alarma
    var agenteFalAlarm: Agente? = null,
    var esFalAlarm: Boolean = false,
    var fechaFalAlar: String? = null,
    val servicios: ArrayList<Tipo>?= null, // Servicios al momento de guardar
    val date: Date? =null,
    val serviciosSolicitados: ArrayList<Servicio>? = null // servicios obtenidos de la BD
): Serializable