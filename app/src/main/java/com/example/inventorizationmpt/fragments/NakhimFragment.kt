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

class NakhimFragment : Fragment() {


    lateinit var nakhimRecyclerView: RecyclerView
    lateinit var nakhimProgressBar: ProgressBar
    lateinit var addClassNakhim : FloatingActionButton
    var listOfClassesNakhim: ArrayList<ItemOfClasses> = ArrayList<ItemOfClasses>()
    lateinit var adapter : ItemAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadClasses()
        addClassNakhim.setOnClickListener {
            val intent = Intent(context, AddClassesActivity::class.java)
            startActivity(intent)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val nakhimView = inflater.inflate(R.layout.fragment_nakhim, container, false)
        nakhimRecyclerView = nakhimView.findViewById(R.id.nakhim_recyclerview)
        nakhimProgressBar = nakhimView.findViewById(R.id.nakhim_progressBar)
        addClassNakhim = nakhimView.findViewById(R.id.add_class_nakhim)
        nakhimProgressBar.visibility = View.VISIBLE
        return nakhimView
    }

    fun loadClasses() {

        val inventorizationService = ServiceBuilder.buildService(InventorizationService::class.java)
        val requestCall = inventorizationService.getClassesList()
        requestCall.enqueue(object : retrofit2.Callback<List<ItemOfClasses>> {
            override fun onResponse(
                call: Call<List<ItemOfClasses>>,
                response: Response<List<ItemOfClasses>>
            ) {

                activity?.runOnUiThread {
                    val classesList = response.body()!!
                    for (i in 0..classesList.size - 1) {

                        if (classesList[i].corps_Id == 2) {

                            listOfClassesNakhim.add(
                                ItemOfClasses(
                                    classesList[i].id_Class,
                                    classesList[i].class_Number,
                                    classesList[i].corps_Id
                                )
                            )
                        }

                    }

                    //d("as", "${listOfClassesNakhim.last().class_Number}")
                    adapter = ItemAdapter(context!!, listOfClassesNakhim) {

                        val intent = Intent(context, DetailActivity::class.java)
                        intent.putExtra("CLASS_INFO", it)
                        startActivity(intent)

                    }
                    adapter.notifyDataSetChanged()
                    nakhimRecyclerView.layoutManager = LinearLayoutManager(activity)
                    nakhimRecyclerView.setHasFixedSize(true)
                    nakhimRecyclerView.adapter = adapter

                }
                nakhimProgressBar.visibility = View.INVISIBLE

            }

            override fun onFailure(call: Call<List<ItemOfClasses>>, t: Throwable) {
                Toast.makeText(context,"Что-то пошло не так." + t.toString(), Toast.LENGTH_LONG).show()
            }

        })

    }


}