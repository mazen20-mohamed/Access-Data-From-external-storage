package com.example.acessdata
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.ContentUris
import android.database.Cursor
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    data class Image(val uri: Uri, val name:String ,val date:Int)
    val images = mutableListOf<Image>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupPermission()
        makeImageList()
        showLetestPhoto()
    }
    private fun setupPermission(){
        val permission = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)

        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE),20000)
        //val request = registerForActivityResult(ActivityResultContracts.RequestPermission())
    }
    private fun makeImageList()
    {
        // from where
         val target:Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        //what data
         val projection = arrayOf(MediaStore.Images.Media._ID,
                            MediaStore.Images.Media.DISPLAY_NAME,
                            MediaStore.Images.Media.DATE_ADDED)
        //matching condition
        val selection ="${MediaStore.Images.Media.DATE_ADDED} >=?"
        val selectionArg = arrayOf<String>(getDaysAgo(-7))
        // how to sort
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val query:Cursor?= applicationContext.contentResolver.query(target,projection,selection,selectionArg,sortOrder)

        query?.use{
            cursor->
             val idColumn  =cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
             val nameColumn= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

            // while(selected data exist)
            while(cursor.moveToNext())
            {
                val id:Long = cursor.getLong(idColumn)
                val name:String = cursor.getString(nameColumn)
                val date:Int = cursor.getInt(dateColumn)

                val contentUri = ContentUris.withAppendedId(target,id)

                // add to image list
                images.add(Image(contentUri,name,date))
            }
            println("images ${images}")
        }
    }
    private fun showLetestPhoto()
    {
        if(images.size<=0)return

        findViewById<TextView>(R.id.textView).text = images.get(0).name
        findViewById<ImageView>(R.id.imageView).setImageURI(images.get(0).uri)

    }
}
