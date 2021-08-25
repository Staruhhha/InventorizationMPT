package com.example.inventorizationmpt.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.inventorizationmpt.R
import com.example.inventorizationmpt.models.ItemOfClasses

class ItemAdapter(
    private val context: Context,
    private val aClasses: List<ItemOfClasses>,
    val listener: (ItemOfClasses) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ClassViewHolder>() {
    class ClassViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val classNumber = view.findViewById<TextView>(R.id.class_number)!!

        fun bindView(class_numb: ItemOfClasses, listener: (ItemOfClasses) -> Unit) {
            classNumber.text = class_numb.class_Number.toString()
            itemView.setOnClickListener { listener(class_numb) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder =
        ClassViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list, parent, false))

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.bindView(aClasses[position], listener)
    }

    override fun getItemCount(): Int = aClasses.size
}