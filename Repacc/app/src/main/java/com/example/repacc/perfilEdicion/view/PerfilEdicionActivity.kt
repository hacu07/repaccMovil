package com.example.repacc.perfilEdicion.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.repacc.BuildConfig
import com.example.repacc.R
import com.example.repacc.perfilEdicion.PerfilEdicionPresenter
import com.example.repacc.perfilEdicion.PerfilEdicionPresenterClass
import com.example.repacc.pojo.*
import com.example.repacc.util.AlertCallback
import com.example.repacc.util.Util
import kotlinx.android.synthetic.main.activity_perfil_edicion.*
import kotlinx.android.synthetic.main.perfil_contenido_edicion.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PerfilEdicionActivity : AppCompatActivity(), PerfilEdicionView {

    private var mPresenter: PerfilEdicionPresenter? = null
    // Imagen perfil
    private lateinit var mCurrentPhotoPath : String
    private var mPhotoSelectedUri: Uri? = null
    private lateinit var bitmap: Bitmap
    private val MY_PHOTO = "my_photo" // Nombre de foto, nombre local y en nube

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_edicion)

        Util.getPreferences(this)
        configToolbar()
        mPresenter = PerfilEdicionPresenterClass(this)
        mPresenter?.onCreate()
        mPresenter?.obtenerPaises(this)
        mPresenter?.cargarDatos()
    }

    override fun configSpiRH(listaRH: List<String>) {
        spiRHEdicion.adapter = Util.obtenerArrayAdapter(listaRH, this)
        spiRHEdicion.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mPresenter?.asignarRH(position)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    fun changeProfilePic(view: View) {
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

                    asignarFotoPerfil(bitmap)
                    rotarImgPer.visibility = View.VISIBLE
                }
            }
        }
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
                asignarFotoPerfil(bitmap)
                rotarImgPer.visibility = View.VISIBLE
            }catch (e: Exception){
                mostrarMsj(getString(R.string.error_load_image))
            }
        }
    }

    private fun asignarFotoPerfil(bitmap: Bitmap?) {
        imvFotoEdicion.setImageBitmap(bitmap)
        //imvFotoEdicion.scaleType = ImageView.ScaleType.CENTER_INSIDE
        //imvFotoEdicion.layoutParams.height = Util.IMAGE_HEIGHT
        imvFotoEdicion.requestLayout()
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

    private fun configToolbar() {
        setSupportActionBar(toolbarPerfilEdicion)
    }

    override fun onDestroy() {
        Util.getPreferences(this)
        mPresenter?.onDestroy()
        super.onDestroy()
    }

    /*
    * Opcion para editar perfil en toolbar
    * */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_perfil_edicion, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mnGuardarPerfil -> guardarPerfil()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun guardarPerfil() {
        if (validarDatos()){
            mPresenter?.asignarDatosUsuarioModif(
                etNombreEdicion.text.toString(),
                etCorreoEdicion.text.toString(),
                etCelularEdicion.text.toString()
            )
            mPresenter?.editarPerfil(this, mPhotoSelectedUri)
        }

    }

    private fun validarDatos(): Boolean {
        var isValid: Boolean = true

        if (etNombreEdicion.text.toString().isNullOrEmpty()){
            isValid = false
            etNombreEdicion.error = getString(R.string.error_data_empty)
            etNombreEdicion.requestFocus()
        }else if (etCorreoEdicion.text.toString().isNullOrEmpty()){
            isValid = false
            etCorreoEdicion.error = getString(R.string.error_data_empty)
            etCorreoEdicion.requestFocus()
        }else if (etCelularEdicion.text.toString().isNullOrEmpty()){
            isValid = false
            etCelularEdicion.error = getString(R.string.error_data_empty)
            etCelularEdicion.requestFocus()
        }
        return isValid
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    /**
     * PerfilEdicionView
     */
    override fun habilitarCampos(habilitar: Boolean) {
        imvFotoEdicion.isEnabled = habilitar
        rotarImgPer.isEnabled = habilitar
        tilNombreEdicion.isEnabled = habilitar
        tilUsuarioEdicion.isEnabled = habilitar
        tilCorreoEdicion.isEnabled = habilitar
        tilCelularEdicion.isEnabled = habilitar
        spiRHEdicion.isEnabled = habilitar
        swRecibNotif.isEnabled = habilitar
        spiPaisNotif.isEnabled = habilitar
        spiDepNotif.isEnabled = habilitar
        spiMunNotif.isEnabled = habilitar

    }

    override fun mostrarProgreso(mostrar: Boolean) {
        pbMenu.visibility = if (mostrar) View.VISIBLE else View.GONE
    }

    override fun mostrarMsj(msj: String) {
        Util.mostrarToast(this,msj)
    }

    override fun cargarPaises(paises: List<String>) {
        spiPaisNotif.adapter = Util.obtenerArrayAdapter(paises, this)

        spiPaisNotif.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mPresenter?.obtenerDepartamentos(this@PerfilEdicionActivity,position)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun cargarDepartamentos(departamentos: List<String>) {
        spiDepNotif.adapter = Util.obtenerArrayAdapter(departamentos, this)

        spiDepNotif.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    mPresenter?.obtenerMunicipios(this@PerfilEdicionActivity,position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun cargarMunicipios(municipios: List<String>) {
        spiMunNotif.adapter = Util.obtenerArrayAdapter(municipios, this)

        spiMunNotif.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mPresenter?.siCargoMunNotifUsuario(true)
                mPresenter?.asignarMunicipio(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun cargarDatos(usuario: Usuario) {
        if(usuario != null){
            etNombreEdicion.setText(usuario.nombre?.trim())
            etUsuarioEdicion.setText(usuario.usuario?.trim())
            etCorreoEdicion.setText(usuario.correo?.trim())
            etCelularEdicion.setText(usuario.celular?.trim())
            swRecibNotif.isChecked = usuario.recibirNotif

            // carga imagen de pefil
            if(usuario.foto != null){
                Glide.with(this)
                    .load(usuario.foto)
                    .error(R.drawable.user)
                    .centerCrop()
                    .into(imvFotoEdicion)
            }
        }
    }

    override fun cargarTipoSangre(position: Int) {
        spiRHEdicion.setSelection(position,true)
    }

    override fun getContext(): Context{
        return this
    }

    fun rotarImagenPerfil(view: View) {
        if (this.bitmap != null){
            val bitmap: Bitmap? = Util.rotateBitmap(this.bitmap, Util.ROTATE_90 )
            if (bitmap != null){
                this.bitmap = bitmap
                imvFotoEdicion.setImageBitmap(bitmap)
            }
        }
    }
}
