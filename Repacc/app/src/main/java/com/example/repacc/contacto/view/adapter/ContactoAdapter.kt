package com.example.repacc.contacto.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.repacc.R
import com.example.repacc.pojo.Contacto
import kotlinx.android.synthetic.main.item_contacto.view.*

class ContactoAdapter(val contactos: ArrayList<Contacto>?, val context: Context) : RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactoViewHolder {
        val inflater =  LayoutInflater.from(parent.context).inflate(R.layout.item_contacto,parent,false)
        return ContactoViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        var size = 0

        if (contactos != null)
            size = contactos.size

        return size
    }

    override fun onBindViewHolder(holder: ContactoViewHolder, position: Int) {
        if (contactos != null) {
            return holder.bind(contactos.get(position), context)
        }
    }

    fun agregarContacto(contacto: Contacto?) {
        if (contacto != null){
            if (contactos?.contains(contacto) == false){
                contactos.add(contacto)
                notifyItemInserted( contactos.size - 1 )
            }
        }
    }

    class ContactoViewHolder(contactoItem: View): RecyclerView.ViewHolder(contactoItem) {

        fun bind(itemContact: Contacto, context: Context) = with(itemView){
            itmCon_nombre.text = itemContact.contacto?.nombre
            itmCon_usuario.text = itemContact.contacto?.usuario

            // carga imagen de pefil
            if(itemContact.contacto?.foto != null){
                Glide.with(context)
                    .load(itemContact.contacto?.foto)
                    .error(R.drawable.user)
                    .centerCrop()
                    .into(itmCon_foto)
            }
        }

    }

}
