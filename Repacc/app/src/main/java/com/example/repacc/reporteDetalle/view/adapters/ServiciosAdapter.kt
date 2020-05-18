package com.example.repacc.reporteDetalle.view.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.repacc.R
import com.example.repacc.pojo.Servicio
import kotlinx.android.synthetic.main.item_serviciosrepdet.view.*

class ServiciosAdapter(
    val serviciosSolicitados: ArrayList<Servicio>,
    val context: Context
): RecyclerView.Adapter<ServiciosAdapter.ServiciosViewHolder>() {

    class ServiciosViewHolder(servicioItemView: View): RecyclerView.ViewHolder(servicioItemView) {
        fun bind(servicio: Servicio, context: Context) = with(itemView){
            itmSerRep_servicio.setText(servicio.tipo.nombre)
            itmSerRep_estado.setText(servicio.estado.nombre)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiciosViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_serviciosrepdet,parent,false)
        return ServiciosViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ServiciosViewHolder, position: Int) {
        return holder.bind(serviciosSolicitados[position], context)
    }

    override fun getItemCount(): Int {
        return serviciosSolicitados.size
    }

}