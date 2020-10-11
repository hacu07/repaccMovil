package com.example.repacc.vehiculoAgregar.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.repacc.BuildConfig
import com.example.repacc.R
import com.example.repacc.pojo.Vehiculo
import com.example.repacc.util.AlertCallback
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import com.example.repacc.vehiculo.view.VehiculoActivity
import com.example.repacc.vehiculoAgregar.VehiculoAgregarPresenter
import com.example.repacc.vehiculoAgregar.VehiculoAgregarPresenterClass
import kotlinx.android.synthetic.main.activity_vehiculo_agregar.*
import kotlinx.android.synthetic.main.perfil_contenido_edicion.*
import kotlinx.android.synthetic.main.vehiculo_contenido_edicion.*
import petrov.kristiyan.colorpicker.ColorPicker
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class VehiculoAgregarActivity : AppCompatActivity(), VehiculoAgregarView {
    private var colores: Array<String>? = null
    private lateinit var mPresenter: VehiculoAgregarPresenter
    private val context :Context = this
    private var vehiculoActivity: VehiculoActivity? = null

    // Imagen vehiculo
    private lateinit var mCurrentPhotoPath : String
    private var mPhotoSelectedUri: Uri? = null
    private lateinit var bitmap: Bitmap
    private val MY_PHOTO = "my_photo" // Nombre de foto, nombre local y en nube

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehiculo_agregar)

        inicializar()
        configToolbar()
        mPresenter = VehiculoAgregarPresenterClass(this)
        mPresenter.onCreate()
        mPresenter.obtenerTipos(this)
    }

    private fun inicializar() {
        //this.vehiculoActivity = intent.extras?.getSerializable(VehiculoActivity.FLAG_VEHICULO_ACTIVITY) as VehiculoActivity
        if(Constantes.vehiculoAgregar != null){
            this.vehiculoActivity = Constantes.vehiculoAgregar
        }

    }

    private fun configToolbar() {
        setSupportActionBar(toolbarVehiculoEdicion)
    }

    fun changeVehiclePic(view: View) {
        Util.showInfoAlert(
            context = this,
            title = getString(R.string.cargar_imagen_reporte),
            msg = getString(R.string.seleccion_carga_imagen),
            textBtnPos = getString(R.string.camara),
            textBtnNeg = getString(R.string.galeria),
            callback = object: AlertCallback {
                override fun onClickPositive() {
                    // Camara
                    checkPermissionToApp(Manifest.permission.READ_EXTERNAL_STORAGE, Util.RP_CAMERA)
                }

                override fun onClickNegative() {
                    // Galeria
                    checkPermissionToApp(Manifest.permission.READ_EXTERNAL_STORAGE, Util.RP_STORAGE)
                }
            }
        )
    }

    /*********************************
     * Sirve para solicitar servicios
     * NO SE USA
     */
    private fun checkPermissionToApp(permissionStr: String, requestPermission: Int) {
        // Valida los permisos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this,permissionStr) != PackageManager.PERMISSION_GRANTED){
                // Si el permiso no se ha concedido, se solicita
                val arrayPermission = arrayOf(permissionStr)
                ActivityCompat.requestPermissions(this,arrayPermission, requestPermission)
                return // Corta proceso y queda a la espera de la respuesta del usuario
            }
        }

        when(requestPermission){
            Util.RP_STORAGE -> {
                fromGallery()
            }
            Util.RP_CAMERA->{
                dispacthTakePictureIntent()
            }
        }
    }

    private fun dispacthTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val photoFile: File?
            photoFile = CreateImageFile()
            if (photoFile != null) {
                //Extrae ubicacion del archivo del fileprovider (facilita de forma segura compartir archivos asociados a la app)
                val photoUri: Uri = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".provider", photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(takePictureIntent, Util.RC_CAMERA)
            }
        }
    }

    private fun CreateImageFile(): File? {
        val timestamp: String =
            SimpleDateFormat("dd-MM-yyyy_HHmmss", Locale.ROOT).format(Date())
        val imageFileName: String = MY_PHOTO.toString() + timestamp + "_"

        //Directorio unico donde se aloja la imagen

        //Directorio unico donde se aloja la imagen
        val storageDir =
            getExternalFilesDir(Environment.DIRECTORY_PICTURES) //Hace que las fotos sean solo privadas para la app


        var image: File? = null

        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir)
            //Extrae ruta de la imagen temporal
            mCurrentPhotoPath = image.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return image
    }

    /*********************************
     * Obtiene imagen desde la galeria
     */
    private fun fromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, Util.RC_GALLERY)
    }

    /*********************************+
     * Respuesta a Activity for result
     * se usa para obtener imagen de la galeria o de la camara
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            // Controla respuesta desde la galeria o camara
            when(requestCode){
                // Respuesta de la galeria
                Util.RC_GALLERY -> {
                    getImageFromGallery(data)
                }

                // Respuesta de la camara
                Util.RC_CAMERA -> {
                    mPhotoSelectedUri = addPicGallery()!!
                    var bitmap: Bitmap? = null
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            mPhotoSelectedUri
                        )
                        this.bitmap = bitmap
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    asignarFotoVehiculo(bitmap)
                    rotarImgVeh.visibility = View.VISIBLE
                }
            }
        }
    }

    //Imagen de la camara
    private fun addPicGallery(): Uri? {
        // Indica que se escanee el archivo y lo añade a la BD multimedia del dispositivo -  si no, no aparece en galeria
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val file = File(mCurrentPhotoPath)
        val contentUri = Uri.fromFile(file) // Envia desde una Uri
        mediaScanIntent.data = contentUri // Envia como parametro
        this.sendBroadcast(mediaScanIntent)
        //mCurrentPhotoPath = null
        return contentUri
    }

    /*******************************
     * Carga la imagen tomada desde la galeria
     */
    private fun getImageFromGallery(data: Intent?) {
        if (data != null){
            mPhotoSelectedUri = data.data!!

            try {
                //Contiene el bitmap segun la Uri especificada
                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, mPhotoSelectedUri)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
                asignarFotoVehiculo(bitmap)
                rotarImgVeh.visibility = View.VISIBLE
            }catch (e: Exception){
                mostrarMsj(getString(R.string.error_load_image))
            }
        }
    }

    private fun asignarFotoVehiculo(bitmap: Bitmap?) {
        civFotoVehi.setImageBitmap(bitmap)
        civFotoVehi.requestLayout()
    }

    fun rotarImagenVehiculo(view: View) {
        if (this.bitmap != null){
            val bitmap: Bitmap? = Util.rotateBitmap(this.bitmap, Util.ROTATE_90 )
            if (bitmap != null){
                this.bitmap = bitmap
                civFotoVehi.setImageBitmap(bitmap)
            }
        }
    }

    /*
    * Opcion para guarda vehiculo en toolbar
    * */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_vehiculo_edicion, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mnGuardarVehiculo -> guardarPerfil()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun guardarPerfil() {
        if(validarDatos()){
            mPresenter.registrarVehiculo(
                this,
                etPlacaVeh.text.toString().trim(),
                swEsParticular.isChecked,
                this.colores!!,
                mPhotoSelectedUri
            )
        }
    }

    private fun validarDatos(): Boolean {
        var esValido = true

        if (etPlacaVeh.text.toString().isNullOrEmpty()){
            esValido = false
            etPlacaVeh.error = getString(R.string.error_placa)
            etPlacaVeh.requestFocus()
        }else{
            etPlacaVeh.error = null
        }

        if (colores == null){
            esValido = false
            mostrarMsj(getString(R.string.error_color_vehiculo))
        }

        return esValido
    }

    override fun onDestroy() {
        Constantes.vehiculoAgregar = null
        mPresenter.onDestroy()
        super.onDestroy()
    }

    override fun mostrarProgreso(siMostrar: Boolean) {
        pbVehiculoAgregar.visibility = if(siMostrar) View.VISIBLE else View.GONE
    }

    override fun habilitarElementos(siHabilita: Boolean) {
        tilPlacaVeh.isEnabled = siHabilita
        spiTipVeh.isEnabled = siHabilita
        spiMarVeh.isEnabled = siHabilita
        spiModVeh.isEnabled = siHabilita
        btnColorVeh.isEnabled = siHabilita
        swEsParticular.isEnabled = siHabilita
    }

    override fun cargarSpiTipos(listaTipos: List<String>) {
        spiTipVeh.adapter = obtenerArrayAdapter(listaTipos)

        spiTipVeh.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mPresenter.obtenerMarcas(context,position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun cargarSpiMarcas(listaMarcas: List<String>) {
        spiMarVeh.adapter = obtenerArrayAdapter(listaMarcas)
        spiMarVeh.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mPresenter.obtenerModelos(context,position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun cargarSpiModelos(listaModelos: List<String>) {
        spiModVeh.adapter = obtenerArrayAdapter(listaModelos)
        spiModVeh.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mPresenter.asignarModelo(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun mostrarMsj(msj: String) {
        Util.mostrarToast(this,msj)
    }

    override fun finalizar() {
        finish()
    }

    private fun obtenerArrayAdapter(lista: List<String>): SpinnerAdapter? {
        val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, lista)

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        return dataAdapter
    }

    fun mostrarColorPicker(view: View){
        val colorPicker = ColorPicker(this)

        val listaColores = ArrayList<String>()
        listaColores.add("#000000") //NEGRO
        listaColores.add("#FFFFFF") //BLANCO
        listaColores.add("#424242") //GRIS
        listaColores.add("#d50000") //ROJO
        listaColores.add("#0d47a1") //AZUL
        listaColores.add("#2e7d32") //VERDE
        listaColores.add("#f9a825") //AMARILLO
        listaColores.add("#ef6c00") //NARANJA

        colorPicker.setColors(listaColores)
            .setColumns(5)
            .setTitle(getString(R.string.title_color_picker))
            .setRoundColorButton(true)
            .setOnFastChooseColorListener(object: ColorPicker.OnFastChooseColorListener{
                override fun setOnFastChooseColorListener(position: Int, color: Int) {
                    asignarColor(listaColores[position])
                }

                override fun onCancel() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
            .show()
    }

    private fun asignarColor(color: String) {
        colores = arrayOf(color)
        btnColorVeh.setBackgroundColor(Color.parseColor(color))
    }

    override fun agregarVehiculo(vehiculo: Vehiculo) {
        vehiculoActivity?.agregarVehiculo(vehiculo)
    }
}
