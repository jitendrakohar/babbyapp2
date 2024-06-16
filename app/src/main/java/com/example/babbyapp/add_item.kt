package com.example.babbyapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.FileOutputStream
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import com.example.babbyapp.databinding.ActivityAddItemBinding
import com.example.babyapp.recyclerview.ItemList
import com.google.gson.Gson

class add_item : AppCompatActivity() {
    private lateinit var binding: ActivityAddItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fabAddImage.setOnClickListener(
            {
                openGallery()
            }
        )
        binding.ivAddItem.setOnClickListener({
            openCamera()
        })

        binding.btnAddItem.setOnClickListener {
            if (binding.itemName.text.isNotEmpty() && binding.itemCost.text.isNotEmpty() && binding.itemDescription.text.isNotEmpty()&& binding.location.text.isNotEmpty()) {
                Toast.makeText(this, "Item Added: ", Toast.LENGTH_SHORT).show()
                val bitmap: Bitmap? = (binding.ivAddItem.drawable as? BitmapDrawable)?.bitmap

                if (bitmap != null) {
                    val fileName = "image_${System.currentTimeMillis()}.png"
                    saveImageToFile(this,bitmap, fileName)
                    val itemList : List<ItemList>
                    itemList = getItemListFromSharedPreferences(this@add_item);
                    itemList.add(ItemList(binding.itemName.text.toString(),binding.itemCost.text.toString(),binding.itemDescription.text.toString(),fileName, binding.location.text.toString(), false))
                    saveItemListToSharedPreferences(this@add_item,itemList)
                    Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

            } else {
                if (binding.itemName.text.isEmpty()) {
                    binding.itemName.error = "Please enter item name"
                } else if (binding.itemCost.text.isEmpty()) {
                    binding.itemCost.error = "Please enter item cost"
                }
                else if(binding.location.text.isEmpty()){
                    binding.location.error = "Please enter location.."
                }
                else {
                    binding.itemDescription.error = "Please enter item description"
                }

            }

        }
        binding.goback.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private val PICK_IMAGE_REQUEST = 1
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private val REQUEST_CAMERA_IMAGE = 102

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val imageUri = data?.data
                    binding.ivAddItem.setImageURI(imageUri)
                }
                REQUEST_CAMERA_IMAGE -> {
                    val imageUri = data?.data
                    val imageBitmap = data?.extras?.get("data") as Bitmap?
                    binding.ivAddItem.setImageBitmap(imageBitmap)
                }
            }
        }
    }
    private fun getFileNameFromUri(uri: Uri?): String? {
        if (uri == null) return null

        val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
        val cursor = contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val fileNameIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                return it.getString(fileNameIndex)
            }
        }
        return null
    }

    fun saveItemListToSharedPreferences(context: Context, itemList: List<ItemList>) {
        val json = convertItemListToJson(itemList)
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("itemList", json)
        editor.apply()

    }

    fun convertItemListToJson(itemList: List<ItemList>): String {
        return Gson().toJson(itemList)
    }

    fun getItemListFromSharedPreferences(context: Context): MutableList<ItemList> {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("itemList", null)
        return if (json != null) {
            Gson().fromJson(json, Array<ItemList>::class.java).toMutableList()
        } else {
            mutableListOf()
        }
    }


    // Save image to file
    fun saveImageToFile(context: Context,bitmap: Bitmap, filename: String) {
        val outputStream: FileOutputStream
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


   fun loadImageFromFile(context:Context, filename: String): Bitmap? {
        return try {
            val inputStream = context.openFileInput(filename)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }



}