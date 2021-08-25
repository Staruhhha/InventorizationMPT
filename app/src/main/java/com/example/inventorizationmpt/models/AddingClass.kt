package com.example.inventorizationmpt.models

import android.content.Context
import android.widget.Toast
import io.paperdb.Paper

class AddingClass {

    companion object{

        fun addItem(inventoryItem : ItemOfSave, quantityOf : Int, context: Context?){

            val inventory = AddingClass.getList()
            val targetItem = inventory.singleOrNull{it.inventory!!.id_Inventory == inventoryItem.inventory!!.id_Inventory}
            if (targetItem == null){
                inventoryItem.quantity = quantityOf
                inventory.add(inventoryItem)
            }else if(targetItem != null){
                Toast.makeText(context,"Данный инвентарь уже добавлени", Toast.LENGTH_LONG).show()
            }
            AddingClass.saveList(inventory)

        }

        fun deleteItem(inventoryItem : ItemOfSave){

            val inventory = AddingClass.getList()
            val targetItem = inventory.singleOrNull{it.inventory!!.id_Inventory == inventoryItem.inventory!!.id_Inventory}
            if (targetItem != null){
                inventory.remove(targetItem)
            }
            AddingClass.saveList(inventory)
        }

        fun changeCount (inventoryItem : ItemOfSave, quantityOf : Int, index : Int){

            val inventory = AddingClass.getList()
            val targetItem = inventory.singleOrNull{it.inventory!!.id_Inventory == inventoryItem.inventory!!.id_Inventory}
            if (targetItem != null){
                inventoryItem.quantity = quantityOf
                inventory.set(index,inventoryItem)
            }
            AddingClass.saveList(inventory)
        }

        fun deleteAll(){
            val inventory = AddingClass.getList()
            if (inventory.size > 0){
                Paper.book("inventory_list").destroy()
            }
        }

        fun saveList(inventory : MutableList<ItemOfSave>){
            Paper.book("inventory_list").write("inventory", inventory)
        }
        fun getList() : MutableList<ItemOfSave>{
            return Paper.book("inventory_list").read("inventory", mutableListOf())
        }

    }

}