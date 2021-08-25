package com.example.inventorizationmpt.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inventorizationmpt.AddClassesActivity
import com.example.inventorizationmpt.DetailActivity
import com.example.inventorizationmpt.R
import com.example.inventorizationmpt.adapters.ItemAdapter
import com.example.inventorizationmpt.models.ItemOfClasses
import com.example.inventorizationmpt.services.InventorizationService
import com.example.inventorizationmpt.services.ServiceBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Response


class NezhinskayaFragment : Fragment() {

    lateinit var nezhkaRecyclerView: RecyclerView
    lateinit var nezhkaProgressBar: ProgressBar
    lateinit var addClassNezhka : FloatingActionButton
    var listOfClassesNezhka: ArrayList<ItemOfClasses> = ArrayList<ItemOfClasses>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadClasses()
        addClassNezhka.setOnClickListener {
            val intent = Intent(context, AddClassesActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val nezhkaView = inflater.inflate(R.layout.fragment_nezhinskaya, container, false)
        nezhkaRecyclerView = nezhkaView.findViewById(R.id.nezhka_recyclerview)
        nezhkaProgressBar = nezhkaView.findViewById(R.id.nezhka_progressBar)
        addClassNezhka = nezhkaView.findViewById(R.id.add_class_nezhinskay)
        nezhkaProgressBar.visibility = View.VISIBLE
        return nezhkaView
    }


    fun loadClasses() {

        val inventorizationService = ServiceBuilder.buildService(InventorizationService::class.java)
        val requestCall = inventorizationService.getClassesList()
        requestCall.enqueue(object : retrofit2.Callback<List<ItemOfClasses>> {
            override fun onResponse(
                call: Call<List<ItemOfClasses>>,
                response: Response<List<ItemOfClasses>>
            ) {
                if (response.isSuccessful){
                    activity?.runOnUiThread {
                        val classesList = response.body()!!
                        for (i in 0..classesList.size - 1) {

                            if (classesList[i].corps_Id == 1) {

                                listOfClassesNezhka.add(
                                    ItemOfClasses(
                                        classesList[i].id_Class,
                                        classesList[i].class_Number,
                                        classesList[i].corps_Id
                                    )
                                )
                            }

                        }

                        //d("as", "${listOfClassesNakhim.last().class_Number}")
                        nezhkaRecyclerView.layoutManager = LinearLayoutManager(activity)
                        nezhkaRecyclerView.setHasFixedSize(true)
                        nezhkaRecyclerView.adapter = ItemAdapter(context!!, listOfClassesNezhka) {

                            val intent = Intent(context, DetailActivity::class.java)
                            intent.putExtra("CLASS_INFO", it)
                            startActivity(intent)

                        }

                    }
                    nezhkaProgressBar.visibility = View.INVISIBLE
                }else {
                    Toast.makeText(context, "Что-то пошло не так. Ошибка со стороны сервера", Toast.LENGTH_LONG)
                }


            }

            override fun onFailure(call: Call<List<ItemOfClasses>>, t: Throwable) {
               Toast.makeText(context,"Что-то пошло не так." + t.toString(), Toast.LENGTH_LONG)
            }

        })

    }


}