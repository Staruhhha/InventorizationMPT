package com.example.inventorizationmpt.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ItemOfInventoryClass (
    var id_Classes_Inventories : Int = 0,
    var class_Id: Int = 0,
    var inventory_Id:Int = 0,
    var inventory_Quantity:Int = 0) : Parcelable