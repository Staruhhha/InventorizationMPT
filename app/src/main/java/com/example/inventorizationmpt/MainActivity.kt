package com.example.inventorizationmpt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.example.inventorizationmpt.fragments.AddInventoryFragment
import com.example.inventorizationmpt.fragments.ListEquipmentFragment
import com.example.inventorizationmpt.fragments.NakhimFragment
import com.example.inventorizationmpt.fragments.NezhinskayaFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolBar : Toolbar
    lateinit var drawerLayout : DrawerLayout
    lateinit var navView : NavigationView
    lateinit var addInventoryFragment: AddInventoryFragment
    lateinit var nakhimFragment: NakhimFragment
    lateinit var nezhinskayaFragment: NezhinskayaFragment
    lateinit var listEquipmentFragment: ListEquipmentFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawer_layout)
        toolBar = findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar


        val drawerToggle : ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolBar,
            (R.string.open),
            (R.string.close)
        ){

        }

        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        nezhinskayaFragment = NezhinskayaFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, nezhinskayaFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.corp1 ->{
                nezhinskayaFragment = NezhinskayaFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, nezhinskayaFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.corp2 ->{
                nakhimFragment = NakhimFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, nakhimFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.add_invent ->{
                addInventoryFragment = AddInventoryFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, addInventoryFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.list_inventory ->{
                listEquipmentFragment = ListEquipmentFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, listEquipmentFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }

    }
}