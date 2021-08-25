package com.example.inventorizationmpt

import android.content.ClipData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.*
import androidx.core.text.isDigitsOnly
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inventorizationmpt.adapters.InventoryClassAdapter
import com.example.inventorizationmpt.models.ItemOfClasses
import com.example.inventorizationmpt.models.ItemOfInventory
import com.example.inventorizationmpt.models.ItemOfInventoryClass
import com.example.inventorizationmpt.services.InventorizationService
import com.example.inventorizationmpt.services.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    lateinit var classOfMPT : ItemOfClasses
    lateinit var listInventoryClass : RecyclerView
    lateinit var classProgressBar: ProgressBar
    var listOfInventoryClass : ArrayList<ItemOfInventoryClass> = ArrayList<ItemOfInventoryClass>()
    lateinit var adapter : InventoryClassAdapter
    lateinit var addInventoryClass : Button
    lateinit var countOfClassInventory : EditText
    lateinit var inventoryClassSpinner: Spinner
    var listInventory : ArrayList<ItemOfInventory> = ArrayList<ItemOfInventory>()
    private var inventory : ItemOfInventory
    init {
        this.inventory = ItemOfInventory(0,"")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        classOfMPT = intent.getParcelableExtra<ItemOfClasses>("CLASS_INFO")!!
        val corpsName = findViewById<TextView>(R.id.corps_name)
        val classNumberDetail = findViewById<TextView>(R.id.class_number_detail)
        val deleteClass = findViewById<Button>(R.id.delete_class)

        listInventoryClass = findViewById(R.id.list_inventory_class)
        classProgressBar = findViewById(R.id.class_progressBar)
        addInventoryClass = findViewById(R.id.add_inventory_class)
        countOfClassInventory = findViewById(R.id.count_of_inventory_class)
        inventoryClassSpinner = findViewById(R.id.inventory_class_spinner)
        getInventories()

        inventoryClassSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                inventory = parent!!.getSelectedItem() as ItemOfInventory
                inventory.getId()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        addInventoryClass.setOnClickListener {

            var check = true
            if (countOfClassInventory.text.length < 1){
                countOfClassInventory.setError("Заполните количество оборудования")
                check = false
            }
            if (check){

                val newInventoryClass = ItemOfInventoryClass()
                newInventoryClass.class_Id = classOfMPT.id_Class
                newInventoryClass.inventory_Id = inventory.getId()

                    if (countOfClassInventory.text.length == 1){
                        newInventoryClass.inventory_Quantity += countOfClassInventory.text.get(0).digitToInt(10)
                    }else if(countOfClassInventory.text.length == 2){
                        newInventoryClass.inventory_Quantity = countOfClassInventory.text.get(0).digitToInt(10) * 10
                        newInventoryClass.inventory_Quantity += countOfClassInventory.text.get(1).digitToInt(10)
                    }
                val inventoryService = ServiceBuilder.buildService(InventorizationService::class.java)
                val requestCall = inventoryService.addClassInventory(newInventoryClass)
                requestCall.enqueue(object : Callback<ItemOfInventoryClass>{
                    override fun onResponse(
                        call: Call<ItemOfInventoryClass>,
                        response: Response<ItemOfInventoryClass>
                    ) {
                        if (response.isSuccessful){
                            Toast.makeText(this@DetailActivity, "Новое оборудование успешно добавлено", Toast.LENGTH_LONG).show()
                            listOfInventoryClass.clear()
                            loadInventoryForClass()
                        }else {
                            Toast.makeText(this@DetailActivity, "Не удалось добавить новое оборудование", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<ItemOfInventoryClass>, t: Throwable) {
                        Toast.makeText(this@DetailActivity, "Не удалось добавить новое оборудование", Toast.LENGTH_LONG).show()
                    }
                })

            }

        }

        deleteClass.setOnClickListener {

            classDelete()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        if (classOfMPT!!.corps_Id == 1){
            corpsName.text = "Нежинская улица 7"
        }else if (classOfMPT!!.corps_Id == 2){
            corpsName.text = "Нахимовский проспект 21"
        }

        classNumberDetail.text = classOfMPT!!.class_Number.toString()

        classProgressBar.visibility = View.VISIBLE
        loadInventoryForClass()
    }

    fun classDelete(){

        val destinationService = ServiceBuilder.buildService(InventorizationService::class.java)
        val requestCall = destinationService.deleteClass(classOfMPT!!.id_Class)

        requestCall.enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful){
                    Toast.makeText(this@DetailActivity, "Класс ${classOfMPT!!.class_Number} удален", Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(this@DetailActivity, "Не удалось удалить класс", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Не удалось удалить класс", Toast.LENGTH_LONG).show()
            }

        })

    }

    fun loadInventoryForClass(){

        val inventorizationService = ServiceBuilder.buildService(InventorizationService::class.java)
        val requestCall = inventorizationService.getInventoryClassList()

        requestCall.enqueue(object : Callback<List<ItemOfInventoryClass>>{
            override fun onResponse(
                call: Call<List<ItemOfInventoryClass>>,
                response: Response<List<ItemOfInventoryClass>>
            ) {
               if (response.isSuccessful){

                   this@DetailActivity.runOnUiThread {

                       val listOf = response.body()!!
                       for (i in 0..listOf.size-1){

                            if (listOf[i].class_Id == classOfMPT!!.id_Class){
                                listOfInventoryClass.add(
                                    ItemOfInventoryClass(listOf[i].id_Classes_Inventories,
                                        listOf[i].class_Id,
                                        listOf[i].inventory_Id,
                                        listOf[i].inventory_Quantity)
                                )
                            }

                       }

                       adapter = InventoryClassAdapter(this@DetailActivity, listOfInventoryClass){index ->deleteItem(index)}
                       adapter.notifyDataSetChanged()
                       listInventoryClass.layoutManager = LinearLayoutManager(this@DetailActivity)
                       listInventoryClass.setHasFixedSize(true)
                       listInventoryClass.adapter = adapter
                   }
                   classProgressBar.visibility = View.INVISIBLE

               }else{
                    Toast.makeText(this@DetailActivity, "Не удалось получить данные с сервера", Toast.LENGTH_LONG).show()
               }


            }

            override fun onFailure(call: Call<List<ItemOfInventoryClass>>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Не удалось получить данные с сервера", Toast.LENGTH_LONG).show()
            }
        })

    }

    fun deleteItem(index : Int){
        listOfInventoryClass.removeAt(index)
        adapter.setItem(listOfInventoryClass)
    }



    fun getInventories(){

        val inventorizationService = ServiceBuilder.buildService(InventorizationService::class.java)
        val requestCall = inventorizationService.getInventoriesList()
        requestCall.enqueue(object : Callback<List<ItemOfInventory>>{
            override fun onResponse(
                call: Call<List<ItemOfInventory>>,
                response: Response<List<ItemOfInventory>>
            ) {
                if (response.isSuccessful){


                    val listForSpinner = response!!.body()
                    for (i in 0..listForSpinner!!.size - 1){

                        listInventory.add(
                            ItemOfInventory(
                                listForSpinner[i].id_Inventory,
                                listForSpinner[i].inventory_Name
                            )
                        )

                    }
                    spinAdapt()


                }else {
                    Toast.makeText(this@DetailActivity, "Что-то пошло не так. Ошибка со стороны сервера", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<ItemOfInventory>>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Что-то пошло не так. Ошибка со стороны сервера", Toast.LENGTH_LONG).show()
            }

        })

    }
    fun spinAdapt(){

        val spinnerAdapter = ArrayAdapter(this@DetailActivity, android.R.layout.simple_spinner_item,listInventory)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAdapter.notifyDataSetChanged()
        inventoryClassSpinner.adapter = spinnerAdapter

    }

}