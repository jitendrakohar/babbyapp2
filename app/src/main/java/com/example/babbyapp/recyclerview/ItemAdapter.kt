package com.example.babyapp.recyclerview

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.babbyapp.R

import com.google.gson.Gson

class ItemAdapter(private val itemList:  MutableList<ItemList>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameTextView: TextView = itemView.findViewById(R.id.item_name)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tv_description)
        val costTextView: TextView = itemView.findViewById(R.id.tv_cost)
        val deleteButton: Button = itemView.findViewById(R.id.button_delete)
        val shareButton: Button = itemView.findViewById(R.id.button_share)
        val imageView: ImageView = itemView.findViewById(R.id.imageView);
        val locationTextView: TextView = itemView.findViewById(R.id.tv_location)
        val purchasedTextView: TextView = itemView.findViewById(R.id.tv_purchased);
        val checkBox: CheckBox =itemView.findViewById(R.id.checkBox);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.itemNameTextView.text = item.itemName
        holder.descriptionTextView.text = item.description
        holder.costTextView.text = item.cost
        holder.locationTextView.text = item.location

        holder.purchasedTextView.text = if (item.purchased) "Purchased" else ""
        holder.checkBox.isChecked = item.purchased
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            item.purchased = isChecked

            holder.purchasedTextView.text = if (item.purchased) "Purchased" else ""
            saveItemListToSharedPreferences(holder.itemView.context, itemList)
        }

        var bitmap: Bitmap? = loadImageFromFile(holder.itemView.context, item.fileName)
        if(bitmap==null){
            val drawable = ContextCompat.getDrawable(holder.imageView.context, R.drawable.tshirt)
            holder.imageView.setImageDrawable(drawable)
        }
        else{
            holder.imageView.setImageBitmap(bitmap)
        }


        holder.deleteButton.setOnClickListener {
            itemList.removeAt(position)
            notifyItemRemoved(position)
            saveItemListToSharedPreferences(holder.itemView.context, itemList)
            Toast.makeText(holder.itemView.context, "Item deleted", Toast.LENGTH_SHORT).show()

        }
        holder.shareButton.setOnClickListener {

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Hey, The item is :\nName: ${item.itemName}\nDescription: ${item.description}\nCost: ${item.cost}\n location: ${item.location}")
                type = "text/plain"
            }
            holder.itemView.context.startActivity(Intent.createChooser(shareIntent, "Share item"))




        }
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
    override fun getItemCount(): Int {
        return itemList.size
    }

    fun loadImageFromFile(context: Context, filename: String): Bitmap? {
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



data class ItemList(
    val itemName: String,
    val description: String,
    val cost: String,
    val fileName:String,
    val location:String,
    var purchased:Boolean
)
