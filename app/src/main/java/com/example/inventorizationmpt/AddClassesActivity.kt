package com.example.inventorizationmpt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inventorizationmpt.adapters.AddInventoryInClassAdapter
import com.example.inventorizationmpt.models.*
import com.example.inventorizationmpt.services.InventorizationService
import com.example.inventorizationmpt.services.ServiceBuilder
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddClassesActivity : AppCompatActivity() {


    var listInventory : ArrayList<ItemOfInventory> = ArrayList<ItemOfInventory>()
    var listClasses : ArrayList<ItemOfClasses>  = ArrayList<ItemOfClasses>()
    var itemOfClassInv : ArrayList<ItemOfInventoryClass> = ArrayList<ItemOfInventoryClass>()
    var data : ArrayList<ItemOfSave> = ArrayList<ItemOfSave>()
    lateinit var adapter : AddInventoryInClassAdapter
    lateinit var classNumberAdd : EditText
    lateinit var corpsSpinner : Spinner
    lateinit var inventoriesSpinner: Spinner
    lateinit var countOfInventory: EditText
    lateinit var addInventory : Button
    lateinit var addInventoryInClass : RecyclerView
    lateinit var deleteAllInventory : Button
    lateinit var addClass : Button
    private var inventoryItem : ItemOfInventory
    init {
        this.inventoryItem = ItemOfInventory(0,"")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_classes)
        Paper.init(this@AddClassesActivity)

        inventoriesSpinner = findViewById(R.id.inventories_spinner)
        classNumberAdd = findViewById(R.id.class_number_add)
        corpsSpinner = findViewById(R.id.corps_spinner)
        countOfInventory = findViewById(R.id.count_of_inventory)
        addInventory = findViewById(R.id.add_inventory_list)
        addInventoryInClass = findViewById(R.id.add_inventory_in_class)
        deleteAllInventory = findViewById(R.id.delete_all_inventory)
        addClass = findViewById(R.id.add_class)

        data = AddingClass.getList() as ArrayList<ItemOfSave>
        adapter = AddInventoryInClassAdapter(this@AddClassesActivity, data,){index -> deleteItem(index)}
        adapter.notifyDataSetChanged()
        addInventoryInClass.layoutManager = LinearLayoutManager(this@AddClassesActivity)
        addInventoryInClass.setHasFixedSize(true)
        addInventoryInClass.adapter = adapter

        getInventories()

        inventoriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                inventoryItem = parent!!.getSelectedItem() as ItemOfInventory
                inventoryItem.getId()
                inventoryItem.getName()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        addInventory.setOnClickListener {
            addInventory()
        }
        deleteAllInventory.setOnClickListener {
            AddingClass.deleteAll()
            data.clear()
            adapter.setItem(data)
        }
        addClass.setOnClickListener {
            addClass()
        }
    }

    fun spinAdapt(){

        val spinnerAdapter = ArrayAdapter(this@AddClassesActivity, android.R.layout.simple_spinner_item,listInventory)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAdapter.notifyDataSetChanged()
        inventoriesSpinner.adapter = spinnerAdapter

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
                    Toast.makeText(this@AddClassesActivity, "Что-то пошло не так. Ошибка со стороны сервера", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<ItemOfInventory>>, t: Throwable) {
                Toast.makeText(this@AddClassesActivity, "Что-то пошло не так. Ошибка со стороны сервера", Toast.LENGTH_LONG).show()
            }

        })

    }

    fun deleteItem(index : Int){
        data.removeAt(index)
        adapter.setItem(data)

    }

    fun addInventory(){

        var check = true
        if (countOfInventory.text.length < 1){
            countOfInventory.setError("Заполните количество оборудования")
            check = false
        }
        if (check){

            var item = ItemOfSave(inventoryItem)
            var quant = 0
            item.inventory!!.id_Inventory = inventoryItem!!.getId()
            item.inventory!!.inventory_Name = inventoryItem!!.getName()

            if (countOfInventory.text.length == 1){
                quant += countOfInventory.text.get(0).digitToInt(10)
            }else if(countOfInventory.text.length == 2){
                quant = countOfInventory.text.get(0).digitToInt(10) * 10
                quant += countOfInventory.text.get(1).digitToInt(10)
            }
            AddingClass.addItem(item, quant, this@AddClassesActivity)
            adapter.notifyDataSetChanged()
            data = AddingClass.getList() as ArrayList<ItemOfSave>
            adapter.setItem(data)


        }

    }

    override fun onDestroy() {
        super.onDestroy()
        AddingClass.deleteAll()
    }

    fun addClass(){
        var check = true
        if (classNumberAdd.text.length != 3){
            classNumberAdd.setError("Номер кабинета должен состоять из 3 цифр")
            check = false
        }
        if (check){
            if (data.size < 1){

                var classNumb = 0
                var corpNumb = 0
                d("ss", "${corpsSpinner.selectedItemId}")
                if (corpsSpinner.selectedItemId.toInt() == 0){corpNumb += 1}
                else if (corpsSpinner.selectedItemId.toInt() == 1){corpNumb += 2}
                d("corp", "${corpNumb}")

                classNumb = classNumberAdd.text.get(0).digitToInt(10) * 100
                classNumb += classNumberAdd.text.get(1).digitToInt(10) * 10
                classNumb += classNumberAdd.text.get(2).digitToInt(10)

                val newClass = ItemOfClasses()
                newClass.class_Number = classNumb
                newClass.corps_Id = corpNumb
                val inventorizationService = ServiceBuilder.buildService(InventorizationService::class.java)
                val requestCall = inventorizationService.addClass(newClass)
                requestCall.enqueue(object : Callback<ItemOfClasses>{
                    override fun onResponse(
                        call: Call<ItemOfClasses>,
                        response: Response<ItemOfClasses>
                    ) {
                        if (response.isSuccessful){
                            Toast.makeText(this@AddClassesActivity, "Класс добавлен", Toast.LENGTH_LONG).show()
                        }else {
                            Toast.makeText(this@AddClassesActivity, "Такой класс уже существует", Toast.LENGTH_LONG).show()
                        }

                    }

                    override fun onFailure(call: Call<ItemOfClasses>, t: Throwable) {
                        Toast.makeText(this@AddClassesActivity, "Ошибка со стороный сервера", Toast.LENGTH_LONG).show()
                    }
                })
                AddingClass.deleteAll()
                startActivity(Intent(this@AddClassesActivity, MainActivity::class.java))
                finish()

            }else if (data.size > 0){
                var classNumb = 0
                var corpNumb = 0
                if (corpsSpinner.selectedItemId.toInt() == 0){corpNumb = 1}
                else if (corpsSpinner.selectedItemId.toInt() == 1){corpNumb = 2}

                classNumb = classNumberAdd.text.get(0).digitToInt(10) * 100
                classNumb += classNumberAdd.text.get(1).digitToInt(10) * 10
                classNumb += classNumberAdd.text.get(2).digitToInt(10)

                val newClass = ItemOfClasses()
                newClass.class_Number = classNumb
                newClass.corps_Id = corpNumb
                val inventorizationService = ServiceBuilder.buildService(InventorizationService::class.java)
                val requestCall = inventorizationService.addClass(newClass)
                requestCall.enqueue(object : Callback<ItemOfClasses>{
                    override fun onResponse(
                        call: Call<ItemOfClasses>,
                        response: Response<ItemOfClasses>
                    ) {
                        if (response.isSuccessful){
                            getClasses()
                        }else {
                            Toast.makeText(this@AddClassesActivity, "Такой класс уже существует", Toast.LENGTH_LONG).show()
                        }

                    }

                    override fun onFailure(call: Call<ItemOfClasses>, t: Throwable) {
                        Toast.makeText(this@AddClassesActivity, "Ошибка со стороный сервера", Toast.LENGTH_LONG).show()
                    }
                })


            }
        }


    }

    fun getClasses(){

        val inventorizationService = ServiceBuilder.buildService(InventorizationService::class.java)
        val requestCall = inventorizationService.getClassesList()
        requestCall.enqueue(object : Callback<List<ItemOfClasses>>{
            override fun onResponse(
                call: Call<List<ItemOfClasses>>,
                response: Response<List<ItemOfClasses>>
            ) {
                if (response.isSuccessful){
                    val listClass = response.body()!!
                    for (i in 0..listClass.size - 1){
                        listClasses.add(ItemOfClasses(listClass[i].id_Class, listClass[i].class_Number, listClass[i].corps_Id))
                    }
                    addInventoryInClass()
                }


            }

            override fun onFailure(call: Call<List<ItemOfClasses>>, t: Throwable) {
                Toast.makeText(this@AddClassesActivity, "Ошибка со стороный сервера", Toast.LENGTH_LONG).show()
            }
        })

    }

    fun addInventoryInClass(){

        var maxId = 0
        for (i in 0..listClasses.size - 1){
            if (listClasses[i].id_Class > maxId){
                maxId = listClasses[i].id_Class
            }
        }

        for (i in 0..data.size - 1){
            val newClassInventory = ItemOfInventoryClass()
            newClassInventory.inventory_Id = data[i].inventory!!.id_Inventory
            newClassInventory.class_Id = maxId
            newClassInventory.inventory_Quantity = data[i].quantity
            val inventServ = ServiceBuilder.buildService(InventorizationService::class.java)
            val reqCall = inventServ.addClassInventory(newClassInventory)
            reqCall.enqueue(object:Callback<ItemOfInventoryClass>{
                override fun onResponse(
                    call: Call<ItemOfInventoryClass>,
                    response: Response<ItemOfInventoryClass>
                ) {
                    if (response.isSuccessful){
                        Toast.makeText(this@AddClassesActivity, "Класс добавлен", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@AddClassesActivity, "Такой класс уже существует", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(
                    call: Call<ItemOfInventoryClass>,
                    t: Throwable
                ) {
                    Toast.makeText(this@AddClassesActivity, "Ошибка со стороный сервера", Toast.LENGTH_LONG).show()
                }
            })
        }
        AddingClass.deleteAll()
        startActivity(Intent(this@AddClassesActivity, MainActivity::class.java))
        finish()

    }

}