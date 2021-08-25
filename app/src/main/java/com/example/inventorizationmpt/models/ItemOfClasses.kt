package com.example.inventorizationmpt.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ItemOfClasses (
    var id_Class : Int = 0,
    var class_Number : Int = 0,
    var corps_Id : Int = 0
) : Parcelable