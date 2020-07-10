package com.example.repacc.notificaciones.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.repacc.R
import com.example.repacc.pojo.Notificacion
import com.example.repacc.util.Util

class NotificacionesActivity : AppCompatActivity() {

    private lateinit var notificaciones : ArrayList<Notificacion>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificaciones)

        notificaciones = intent.extras!!.getSerializable(NotificacionesActivity::class.java.name) as ArrayList<Notificacion>

        for (notif in notificaciones){
            Util.mostrarToast(this, notif.mensaje)
        }
    }
}
