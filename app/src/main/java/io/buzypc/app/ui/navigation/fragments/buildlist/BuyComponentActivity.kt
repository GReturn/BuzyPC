package io.buzypc.app.ui.navigation.fragments.buildlist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.R
import io.buzypc.app.Data.AppSession.BuzyUserAppSession

class BuyComponentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_buy_component)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBackNavigation = findViewById<ImageView>(R.id.btn_back_navigation)
        btnBackNavigation.setOnClickListener {
            finish()
            return@setOnClickListener
        }

        val imgComponent = findViewById<ImageView>(R.id.imgComponent)
        val tvBrandName = findViewById<TextView>(R.id.tvBrandName)
        val tvComponentName = findViewById<TextView>(R.id.tvComponentName)
        val lvStoreList = findViewById<ListView>(R.id.lvStoreList)
        val app = application as BuzyUserAppSession

        val imageRes = when (app.component) {
            app.pc.cpu           -> R.drawable.cpu_64_bit
            app.pc.gpu           -> R.drawable.gpu
            app.pc.motherboard   -> R.drawable.motherboard_vector
            app.pc.ram           -> R.drawable.ram_vector
            app.pc.storageDevice -> R.drawable.storage_vector
            app.pc.psu           -> R.drawable.psu_vector
            else                 -> R.drawable.cpu_64_bit
        }
        imgComponent.setImageResource(imageRes)
        tvBrandName.text = app.component.brand
        tvComponentName.text = app.component.name
        val names = app.component.stores.map { it.name }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            names
        )
        lvStoreList.adapter = adapter
        lvStoreList.setOnItemClickListener { _, _, pos, _ ->
            val url = app.component.stores[pos].vendorSite
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }
}