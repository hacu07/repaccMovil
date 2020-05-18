package com.example.repacc.reportes.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.repacc.R
import com.example.repacc.pojo.Reporte
import kotlinx.android.synthetic.main.item_reportebasico.view.*

class ReportesAdapter(
    val listaReportes : ArrayList<Reporte>,
    val context: Context,
    val listener: OnReporteClickListener
) :RecyclerView.Adapter<ReportesAdapter.ReporteViewHolder>() {

    class ReporteViewHolder(reporteView: View) : RecyclerView.ViewHolder(reporteView) {
        fun bind(reporte: Reporte, context: Context, listener: OnReporteClickListener) = with(itemView){
            itmReps_info.setText("${reporte.direccion}")
            if(reporte.imagen != null){
                Glide.with(context)
                    .load(reporte.imagen)
                    .error(android.R.drawable.ic_menu_camera)
                    .centerCrop()
                    .into(itmReps_foto)
            }

            itmReps_foto.setOnClickListener {
                listener.onReporteClickListener(reporte)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReporteViewHolder {
        val inflater =  LayoutInflater.from(parent.context).inflate(
            R.layout.item_reportebasico,parent,false)
        return ReporteViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ReporteViewHolder, position: Int) {
        return holder.bind(listaReportes[position], context,listener)
    }

    override fun getItemCount(): Int {
        return listaReportes.size
    }

}