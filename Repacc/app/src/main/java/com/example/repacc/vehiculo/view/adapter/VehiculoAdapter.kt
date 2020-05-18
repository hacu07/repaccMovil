package com.example.repacc.vehiculo.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.repacc.R
import com.example.repacc.pojo.Vehiculo
import kotlinx.android.synthetic.main.item_vehiculo.view.*

class VehiculoAdapter(
    val vehiculos: ArrayList<Vehiculo>,
    val context: Context,
    val listener: OnItemVehiculoListener
) :  RecyclerView.Adapter<VehiculoAdapter.VehiculoViewHolder>()
{
    class VehiculoViewHolder(vehiculoItemView: View): RecyclerView.ViewHolder(vehiculoItemView) {
        fun onBind(vehiculo: Vehiculo, context: Context, listener: OnItemVehiculoListener) = with(itemView){
            itmVeh_placa.setText(vehiculo.placa)
            itmVeh_marca.setText(vehiculo.modelo.marca.nombre)
            itmVeh_modelo.setText(vehiculo.modelo.nombre)

            if (vehiculo.foto != null){
                Glide.with(context)
                    .load(vehiculo.foto)
                    .error(android.R.drawable.ic_menu_camera)
                    .centerCrop()
                    .into(itmVeh_foto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehiculoViewHolder {
        val inflater =  LayoutInflater.from(parent.context).inflate(R.layout.item_vehiculo,parent,false)
        return VehiculoViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return vehiculos.size
    }

    override fun onBindViewHolder(holder: VehiculoViewHolder, position: Int) {
        return holder.onBind(
            vehiculos.get(position),
            context,
            listener
        )
    }

    fun agregarVehiculo(vehiculo: Vehiculo) {
        if (!vehiculos.contains(vehiculo)){
            vehiculos.add(vehiculo)
            notifyItemInserted(vehiculos.size -1)
        }
    }
}