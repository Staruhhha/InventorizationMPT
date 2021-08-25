package com.example.inventorizationmpt.services

import com.example.inventorizationmpt.models.ItemOfClasses
import com.example.inventorizationmpt.models.ItemOfInventory
import com.example.inventorizationmpt.models.ItemOfInventoryClass
import retrofit2.Call
import retrofit2.http.*

interface InventorizationService {

    @GET("classes")
    fun getClassesList() : Call<List<ItemOfClasses>>

    @GET("inventories")
    fun getInventoriesList() : Call<List<ItemOfInventory>>

    @GET ("class_inventory")
    fun getInventoryClassList() : Call<List<ItemOfInventoryClass>>

    @GET ("inventories/{id}")
    fun getInventory(@Path("id") id: Int) : Call<ItemOfInventory>

    @POST("inventories")
    fun addInventory(@Body newInventory : ItemOfInventory) : Call<ItemOfInventory>

    @POST("class_inventory")
    fun addClassInventory(@Body newClassInventory : ItemOfInventoryClass) : Call<ItemOfInventoryClass>

    @POST("classes")
    fun addClass(@Body newClass : ItemOfClasses) : Call<ItemOfClasses>

    @DELETE("classes/{id}")
    fun deleteClass(@Path("id") id : Int) : Call <Unit>

    @DELETE("inventories/{id}")
    fun deleteInventory(@Path("id") id : Int) : Call<Unit>

    @DELETE("class_inventory/{id}")
    fun deleteClassInventory(@Path("id") id : Int) : Call<Unit>


    @PUT("class_inventory/{id}")
    fun updateClass_Inventory(
        @Path("id") id : Int,
        @Body updateClassInv : ItemOfInventoryClass
    ) : Call<ItemOfInventoryClass>

}