package com.example.repacc.util

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.repacc.pojo.Config
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Util {

    //Elementos estaticos
    companion object {

        lateinit var URL_API: String

        val SUCCESS = 0
        val ERROR_DATA = 100
        val ERROR_RESPONSE = 101
        val ERROR_CONEXION = 102

        fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun mostrarToast(context: Context, msj: String){
            Toast.makeText(context,msj,Toast.LENGTH_LONG).show()
        }

        /*
        * Almacena la configuracion del usuario en las preferencias
        * */
        fun guardarConfig(context: Context):Boolean {
            var l_guardo = false;

            // Obtiene la preferencia de configuracion
            var preferences = context.getSharedPreferences(
                    Constantes.TAG_CONFIG,
                    Constantes.PREFERENCE_PRIVATE_MODE
            )

            if (preferences != null &&  context != null && Constantes.config != null){
                //Permite editar el archivo de las preferencias
                val editor: SharedPreferences.Editor = preferences.edit()

                //agrega la configuracion en formato JSON a las preferencias
                editor.putString(Constantes.PREFERENCES_CONFIG, Gson().toJson(Constantes.config))

                // Guarda en preferencias
                l_guardo = editor.commit()
            }

            return l_guardo
        }


        /**************************************
         * Obtiene los datos de las preferencias y las asigna a las constantes globales
         */
        fun getPreferences(context: Context){
            val pref = context.getSharedPreferences(
                Constantes.TAG_CONFIG,
                Context.MODE_PRIVATE
            )

            if (pref != null){
                //Obtiene el json en String y lo convierte a obj Config

                val jsonConfig = pref.getString(Constantes.PREFERENCES_CONFIG,null)

                if (jsonConfig != null){
                    Constantes.config = Gson().fromJson(jsonConfig, Config::class.java)
                }

            }
        }


        /*********************************************************************
        * Construye alertDialog y muestra segun parametros obtenidos
        ************************************************************************/
        fun showInfoAlert(
            context: Context,
            title: String,
            msg :String,
            textBtnPos:String,
            textBtnNeg:String,
            callback: AlertCallback){
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(textBtnPos, object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        callback.onClickPositive()
                    }
                })
                .setNegativeButton(textBtnNeg, object: DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        callback.onClickNegative()
                    }
                })
                .show()
        }

        /**************************************************************************
         * Valida conexion a internet
         **************************************************************************/
        fun validarInternet(context: Context): Boolean{
            var hasInternet = false
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo

            if (activeNetwork != null)
                hasInternet = activeNetwork.isConnectedOrConnecting

            return hasInternet

        }
    }


}