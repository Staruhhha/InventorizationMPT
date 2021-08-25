package com.example.inventorizationmpt.adapters

import android.app.AlertDialog
import android.content.Context
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.inventorizationmpt.R
import com.example.inventorizationmpt.models.ItemOfInventory
import com.example.inventorizationmpt.models.ItemOfInventoryClass
import com.example.inventorizationmpt.services.InventorizationService
import com.example.inventorizationmpt.services.ServiceBuilder
import kotlinx.android.synthetic.main.edit_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InventoryClassAdapter(var context: Context?,
                            var inventoryClass : ArrayList<ItemOfInventoryClass>, val onClickDelete : (Int) -> Unit)
    : RecyclerView.Adapter<InventoryClassAdapter.InventoryClaasViewHolder>() {

    private var listData = inventoryClass
    inner class InventoryClaasViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val inventoryNameClass = view.findViewById<TextView>(R.id.inventory_name_class)
        val inventoryCount = view.findViewById<TextView>(R.id.inventory_count_class)
        val deleteInv = view.findViewById<ImageButton>(R.id.delete_inv)
        val editInv = view.findViewById<ImageButton>(R.id.edit_inv)


        fun bindView(class_inventory : ItemOfInventoryClass, index : Int) {

            getNameInventory(class_inventory.inventory_Id, inventoryNameClass)
            inventoryCount.setText(class_inventory.inventory_Quantity.toString())

            deleteInv.setOnClickListener { view ->

                val inventoryClassDelete = ItemOfInventoryClass(class_inventory.id_Classes_Inventories,class_inventory.class_Id,
                class_inventory.inventory_Id, class_inventory.inventory_Quantity)
                val inventorizationService = ServiceBuilder.buildService(InventorizationService::class.java)
                val requestCall = inventorizationService.deleteClassInventory(inventoryClassDelete.id_Classes_Inventories)
                onClickDelete(index)
                requestCall.enqueue(object : Callback<Unit>{
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        if (response.isSuccessful){
                            Toast.makeText(view.context, "Оборудование удалено", Toast.LENGTH_LONG).show()
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



        fun getNameInventory(id : Int, tvView : TextView?) {

            val inventorizationService = ServiceBuilder.buildService(InventorizationService::class.java)
            val requestCall = inventorizationService.getInventory(id)

            requestCall.enqueue(object : Callback<ItemOfInventory>{
                override fun onResponse(
                    call: Call<ItemOfInventory>,
                    response: Response<ItemOfInventory>
                ) {
                    if (response.isSuccessful){

                        val inventoryInClass = response.body()
                        inventoryInClass.let {
                            tvView!!.setText(inventoryInClass!!.inventory_Name.toString())
                        }

                    }else{
                        Toast.makeText(itemView.context, "Не удалось загрузить оборудование", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ItemOfInventory>, t: Throwable) {
                    Toast.makeText(itemView.context, "Не удалось загрузить оборудование", Toast.LENGTH_LONG).show()
                }
            })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryClaasViewHolder =
        InventoryClaasViewHolder(LayoutInflater.from(context).inflate(R.layout.inventory_list_class,parent,false))

    override fun onBindViewHolder(holder: InventoryClaasViewHolder, position: Int) {
        holder.bindView(inventoryClass[position], position)



        //val editInv = findViewById<ImageButton>(R.id.edit_inv)

        holder.editInv.setOnClickListener {
            val editCountDialog = LayoutInflater.from(context).inflate(R.layout.edit_dialog, null)
            val mBuilder = AlertDialog.Builder(context)
                .setView(editCountDialog)
            val mAlertDialog = mBuilder.show()
            //editCountDialog.edit_count.setText(class_inventory.inventory_Quantity)

            editCountDialog.close_dialog.setOnClickListener {mAlertDialog.dismiss()}
            editCountDialog.apply_change.setOnClickListener {
                var check = true
                if (editCountDialog.edit_count.text.length < 1){
                    editCountDialog.edit_count.setError("Заполните количество оборудования")
                    check = false
                }
                if (check){
                    var editQuantity = 0
                    if (editCountDialog.edit_count.text.length == 1){
                        editQuantity += editCountDialog.edit_count.text.get(0).digitToInt(10)
                    }else if(editCountDialog.edit_count.text.length == 2){
                        editQuantity = editCountDialog.edit_count.text.get(0).digitToInt(10) * 10
                        editQuantity += editCountDialog.edit_count.text.get(1).digitToInt(10)
                    }
                    val itemOf = ItemOfInventoryClass(inventoryClass[position].id_Classes_Inventories, inventoryClass[position].class_Id,
                        inventoryClass[position].inventory_Id, editQuantity)
                    val inventorizationService = ServiceBuilder.buildService(InventorizationService::class.java)
                    val requestCallEdit = inventorizationService.updateClass_Inventory(inventoryClass[position].id_Classes_Inventories,
                        itemOf)
                    requestCallEdit?.enqueue(object : Callback<ItemOfInventoryClass>{
                        override fun onResponse(
                            call: Call<ItemOfInventoryClass>,
                            response: Response<ItemOfInventoryClass>
                        ) {
                            if (response.isSuccessful){
                                Toast.makeText(context, "Количество успешно изменено", Toast.LENGTH_LONG).show()
                                inventoryClass.set(position, itemOf)
                                notifyDataSetChanged()
                                mAlertDialog.dismiss()
                            }else {
                                Toast.makeText(context, "Не удалось изменить количество", Toast.LENGTH_LONG).show()
                            }

                        }

                        override fun onFailure(call: Call<ItemOfInventoryClass>, t: Throwable) {
                            Toast.makeText(context, "Не удалось изменить количество2", Toast.LENGTH_LONG).show()
                        }
                    })
                }
            }
        }
    }

    override fun getItemCount(): Int = inventoryClass.size

    fun setItem(items: ArrayList<ItemOfInventoryClass>){
        listData = items
        notifyDataSetChanged()
    }


}