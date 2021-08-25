package com.example.inventorizationmpt.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.inventorizationmpt.R
import com.example.inventorizationmpt.models.AddingClass
import com.example.inventorizationmpt.models.ItemOfSave
import kotlinx.android.synthetic.main.edit_dialog.view.*

class AddInventoryInClassAdapter(var context: Context?, var inventoryItem : ArrayList<ItemOfSave>, val deletOnClick : (Int) -> Unit) :
    RecyclerView.Adapter<AddInventoryInClassAdapter.ViewHolder>(){

    private var listData = inventoryItem

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val inventoryNameClass = view.findViewById<TextView>(R.id.inventory_name_class)
        val inventoryCount = view.findViewById<TextView>(R.id.inventory_count_class)
        val deleteInv = view.findViewById<ImageButton>(R.id.delete_inv)
        val editInv = view.findViewById<ImageButton>(R.id.edit_inv)

        fun bindView(addInventory : ItemOfSave, index : Int){

            inventoryNameClass.text = addInventory.inventory!!.inventory_Name
            inventoryCount.text = addInventory.quantity.toString()

            deleteInv.setOnClickListener { view ->
                val item = ItemOfSave(addInventory.inventory)
                AddingClass.deleteItem(item)
                deletOnClick(index)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.inventory_list_class, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listData[position], position)

        holder.editInv.setOnClickListener {
            val editCountDialog = LayoutInflater.from(context).inflate(R.layout.edit_dialog, null)
            val mBuilder = AlertDialog.Builder(context)
                .setView(editCountDialog)
            val mAlertDialog = mBuilder.show()
            //editCountDialog.edit_count.setText(class_inventory.inventory_Quantity)

            editCountDialog.close_dialog.setOnClickListener { mAlertDialog.dismiss() }
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
                    AddingClass.changeCount(listData[position],editQuantity, position )
                    notifyDataSetChanged()
                    mAlertDialog.dismiss()
                }
            }
        }
    }
    override fun getItemCount(): Int = listData.size

    fun setItem (items: ArrayList<ItemOfSave>){
        listData = items
        notifyDataSetChanged()
    }
}