package com.example.repacc.contacto.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.repacc.R
import com.example.repacc.pojo.Contacto
import com.example.repacc.pojo.Solicitud
import com.example.repacc.pojo.Usuario
import kotlinx.android.synthetic.main.item_solicitud.view.*

class SolicitudAdapter(
        val solicitudes: ArrayList<Solicitud>?,
        val context: Context,
        val listener: OnItemSolicitudListener)
    : RecyclerView.Adapter<SolicitudAdapter.SolicitudViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolicitudViewHolder {
        val inflater =  LayoutInflater.from(parent.context).inflate(R.layout.item_solicitud,parent,false)
        return SolicitudViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: SolicitudViewHolder, position: Int){
        if (solicitudes != null) {
            return holder.bind(solicitudes.get(position), context, listener)
        }
    }

    override fun getItemCount(): Int {
        var size = 0

        if (solicitudes != null)
            size = solicitudes.size

        return size
    }

    /*
    *   Elimina la solicitud de la lista
    * */
    fun eliminarSolicitud(contacto: Contacto?) {
        val solicitud = solicitudes?.find {
                solicitud ->  solicitud.usuario._id.equals(contacto?.contacto?._id)
        }

        if (solicitud != null){
            val index = solicitudes?.indexOf(solicitud)
            index?.let {
                solicitudes?.removeAt(it)
                notifyItemRemoved(it)
            }
        }

    }


    class SolicitudViewHolder(solicitudItemView: View): RecyclerView.ViewHolder(solicitudItemView){

        fun bind(solicitud: Solicitud, context: Context, listener: OnItemSolicitudListener) = with(itemView){
            itmSol_usuario.text = solicitud.usuario.usuario
            // carga imagen de pefil
            if(solicitud.usuario.foto != null){
                Glide.with(context)
                    .load(solicitud.usuario.foto)
                    .error(R.drawable.user)
                    .centerCrop()
                    .into(itmSol_foto)
            }


            itmSol_btnConfirmar.setOnClickListener {
                listener.listenerEstadoSolicitud(solicitud,true)
            }
            itmSol_btnRechazar.setOnClickListener {
                listener.listenerEstadoSolicitud(solicitud,false)
            }
        }
    }
}

