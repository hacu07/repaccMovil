package com.example.repacc.notificaciones.view.adapters

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.repacc.R
import com.example.repacc.pojo.Notificacion
import kotlinx.android.synthetic.main.item_notificacion.view.*

class NotificationsAdapter(
    val context: Context,
    val notificaciones: ArrayList<Notificacion>,
    val listener: OnNotificationClickListener
) : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(
            notificacion: Notificacion,
            context: Context,
            listener: OnNotificationClickListener) = with(itemView){
            itmNot_titulo.setText(context.getString(R.string.titulo_notif_report,notificacion.reporte.codigo!!))
            //itmNot_resumen.setText(context.getString(R.string.resumen_notif_report,notificacion.reporte.direccion!!))
            itmNot_resumen.setText(notificacion.mensaje)
            if(notificacion.reporte.imagen != null){
                Glide.with(context)
                    .load(notificacion.reporte.imagen)
                    .error(android.R.drawable.ic_popup_reminder)
                    .centerCrop()
                    .into(itmNot_foto)
            }

            itmNot_cv.setOnClickListener {
                listener.onNotificationClickListener(notificacion)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_notificacion, parent, false)
        return NotificationViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        return holder.onBind(
            notificacion = notificaciones[position],
            context = context,
            listener = listener
        )
    }

    override fun getItemCount() = notificaciones.size


}