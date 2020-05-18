package com.example.repacc.reporte.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.repacc.R
import kotlinx.android.synthetic.main.item_placa.view.*

class PlacasAdapter(
    val listaPlacas: ArrayList<String>,
    val context: Context,
    val listener: OnPlacaClickListener
) : RecyclerView.Adapter<PlacasAdapter.PlacasViewHolder>() {

    class PlacasViewHolder(PlacaItemView: View):RecyclerView.ViewHolder(PlacaItemView) {
        fun bind(placa: String, context: Context, listener: OnPlacaClickListener) = with(itemView) {
            itmRep_placa.setText(placa)
            itmRep_btnEliminar.setOnClickListener {
                listener.onPlacaClickListener(placa)
            }
        }
    }

    fun agregarPlaca(placa: String){
        // Si no lo tiene lo agrega
        if (!listaPlacas.contains(placa)){
            listaPlacas.add(placa)
            notifyItemInserted(listaPlacas.size - 1)
        }else{
            actualizarPlaca(placa)
        }
    }

    fun actualizarPlaca(placa: String) {
        if (listaPlacas.contains(placa)){
            val index = listaPlacas.indexOf(placa)
            listaPlacas.set(index,placa)
            notifyItemChanged(index)
        }
    }

    fun eliminarPlaca(placa: String){
        if (listaPlacas.contains(placa)){
            val index = listaPlacas.indexOf(placa)
            listaPlacas.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun obtenerPlacas(): ArrayList<String>{
        return listaPlacas
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacasViewHolder {
        val inflater =  LayoutInflater.from(parent.context).inflate(R.layout.item_placa,parent,false)
        return PlacasViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return listaPlacas.size
    }

    override fun onBindViewHolder(holder: PlacasViewHolder, position: Int) {
        return holder.bind(listaPlacas[position], context, listener)
    }
}