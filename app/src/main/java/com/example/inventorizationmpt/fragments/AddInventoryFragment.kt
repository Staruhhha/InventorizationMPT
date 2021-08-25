package com.example.inventorizationmpt.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.inventorizationmpt.R
import com.example.inventorizationmpt.models.ItemOfInventory
import com.example.inventorizationmpt.services.InventorizationService
import com.example.inventorizationmpt.services.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddInventoryFragment : Fragment() {

    lateinit var inventoryEditText : EditText
    lateinit var inventoryAddButton : Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inventoryAddButton.setOnClickListener {
            addInventory()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val addView =  inflater.inflate(R.layout.fragment_add_inventory, container, false)
        inventoryEditText = addView.findViewById(R.id.inventory_edit_text)
        inventoryAddButton = addView.findViewById(R.id.inventory_add_button)
        return addView
    }

    fun addInventory(){

        val newInventory = ItemOfInventory()
        newInventory!!.inventory_Name = inventoryEditText.text.toString()
        val inventoryService = ServiceBuilder.buildService(InventorizationService::class.java)
        val requestCall = inventoryService.addInventory(newInventory)

        requestCall.enqueue(object: Callback<ItemOfInventory>{
            override fun onResponse(
                call: Call<ItemOfInventory>,
                response: Response<ItemOfInventory>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(context, "Новое оборудование успешно добавлено", Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(context, "Не удалось добавить новое оборудование", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ItemOfInventory>, t: Throwable) {
                Toast.makeText(context, "Не удалось добавить новое оборудование", Toast.LENGTH_LONG).show()
            }

        })

    }

}