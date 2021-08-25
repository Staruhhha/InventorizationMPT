package com.example.inventorizationmpt.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.inventorizationmpt.R
import com.example.inventorizationmpt.models.ItemOfInventory
import com.example.inventorizationmpt.services.InventorizationService
import com.example.inventorizationmpt.services.ServiceBuilder
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InventoryAdapter(var context : Context?, var inventories : List<ItemOfInventory>, val onClickDelete : (Int) -> Unit) :
    RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder>() {

    private var listData = inventories

    inner class InventoryViewHolder(view : View) : RecyclerView.ViewHolder(view) {



        val inventoryName = view.findViewById<TextView>(R.id.inventory_name)
        val deleteInventory = view.findViewById<ImageButton>(R.id.delete_inventory)


        fun bindView(inventory_name : ItemOfInventory, index : Int){
            inventoryName.text = inventory_name.inventory_Name


                deleteInventory.setOnClickListener { view ->

                    val inventoryDel = ItemOfInventory(inventory_name.id_Inventory, inventory_name.inventory_Name)
                    val inventorizationService = ServiceBuilder.buildService(InventorizationService::class.java)
                    val requestCall = inventorizationService.deleteInventory(inventoryDel.id_Inventory)
                    onClickDelete(index)

                    requestCall.enqueue(object : Callback<Unit>{
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            if (response.isSuccessful){
                                Toast.makeText(view.context, "Оборудование ${inventoryDel.inventory_Name} удалено", Toast.LENGTH_LONG).show()
                            }else {
                                Toast.makeText(view.context, "Не удалось удалить оборудование", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            Toast.makeText(view.context, "Не удалось удалить оборудование", Toast.LENGTH_LONG).show()
                        }
                    })
                }



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder =
        InventoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.inventory_item, parent,false))

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        holder.bindView(inventories[position], position)
    }

    override fun getItemCount(): Int = inventories.size

    fun setItem(items : List<ItemOfInventory>){
        listData = items
        notifyDataSetChanged()
    }
}