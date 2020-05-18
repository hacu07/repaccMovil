package com.example.repacc.reporte.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.repacc.R
import com.example.repacc.pojo.Tipo
import kotlinx.android.synthetic.main.item_servicios.view.*

class ServiciosAdapter(
    val listaServicios: ArrayList<Tipo>,
    val context: Context,
    val listener: OnItemServicioClickListener
) : RecyclerView.Adapter<ServiciosAdapter.ServiciosViewHolder>() {


    class ServiciosViewHolder(ServicioItemView: View):RecyclerView.ViewHolder(ServicioItemView) {
        fun bind(servicio: Tipo, context: Context, listener: OnItemServicioClickListener) = with(itemView) {
            itemSer_check.setText(servicio.nombre)

            itemSer_check.setOnClickListener {
                listener.onClickServicioListener(itemSer_check.isChecked,servicio)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiciosViewHolder {
        val inflater =  LayoutInflater.from(parent.context).inflate(R.layout.item_servicios,parent,false)
        return ServiciosViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ServiciosViewHolder, position: Int) {
        return holder.bind(listaServicios[position],context,listener)
    }

    override fun getItemCount(): Int {
        return listaServicios.size
    }
}