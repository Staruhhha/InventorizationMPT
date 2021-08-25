package com.example.inventorizationmpt.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inventorizationmpt.R
import com.example.inventorizationmpt.adapters.InventoryAdapter
import com.example.inventorizationmpt.models.ItemOfInventory
import com.example.inventorizationmpt.services.InventorizationService
import com.example.inventorizationmpt.services.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListEquipmentFragment : Fragment() {

    lateinit var equipmentRecyclerView: RecyclerView
    lateinit var equipmentProgressBar: ProgressBar
    lateinit var adapter : InventoryAdapter
    var listOfInventiry : ArrayList<ItemOfInventory> = ArrayList<ItemOfInventory>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadInventory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val equipmentView = inflater.inflate(R.layout.fragment_list_equipment, container, false)
        equipmentRecyclerView = equipmentView.findViewById(R.id.equipment_recyclerview)
        equipmentProgressBar = equipmentView.findViewById(R.id.equipment_progressBar)
        equipmentProgressBar.visibility = View.VISIBLE
        return equipmentView
    }


    fun loadInventory() {

        val inventorizationService = ServiceBuilder.buildService(InventorizationService::class.java)
        val requestCall = inventorizationService.getInventoriesList()
        requestCall.enqueue(object : Callback<List<ItemOfInventory>> {
            override fun onResponse(
                call: Call<List<ItemOfInventory>>,
                response: Response<List<ItemOfInventory>>
            ) {
                if (response.isSuccessful) {

                    activity?.runOnUiThread {

                        val inventoriesList = response.body()!!
                        for (i in 0..inventoriesList.size - 1){

                            listOfInventiry.add(
                                ItemOfInventory(
                                    inventoriesList[i].id_Inventory,
                                    inventoriesList[i].inventory_Name
                                )
                            )

                        }

                        adapter = InventoryAdapter(context,listOfInventiry){index -> deleteItem(index)}
                        adapter.notifyDataSetChanged()
                        equipmentRecyclerView.layoutManager = LinearLayoutManager(activity)
                        equipmentRecyclerView.setHasFixedSize(true)
                        equipmentRecyclerView.adapter = adapter
                        //equipmentRecyclerView.adapter!!.notifyDataSetChanged()
                    }
                    equipmentProgressBar.visibility = View.INVISIBLE
                }else {
                    Toast.makeText(context, "Что-то пошло не так. Ошибка со стороны сервера", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<ItemOfInventory>>, t: Throwable) {
                Toast.makeText(context, "Что-то пошло не так." + t.toString(), Toast.LENGTH_LONG).show()
            }

        })

    }

    fun deleteItem(index : Int){
        listOfInventiry.removeAt(index)
        adapter.setItem(listOfInventiry)
    }

}