package com.example.inventorizationmpt.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ItemOfInventory (
        var id_Inventory : Int = 0,
        var inventory_Name : String? = null
): Parcelable{

        fun getId() : Int{
                return id_Inventory
        }

        override fun toString(): String {
                return inventory_Name.toString()
        }

        fun getName() : String{
                return inventory_Name.toString()
        }


}