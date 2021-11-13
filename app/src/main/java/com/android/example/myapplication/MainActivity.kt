package com.android.example.myapplication

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.example.myapplication.adapters.ProductAdapter
import com.android.example.myapplication.databinding.ActivityMainBinding
import com.android.example.myapplication.databinding.AddDialogBinding
import com.android.example.myapplication.models.ProductData
import com.android.example.myapplication.models.ProductTypeData
import com.android.example.myapplication.models.RequestProductData
import com.android.example.myapplication.retrofit.ApiClient.apiService
import com.android.example.myapplication.utils.NetworkHelper
import com.android.example.myapplication.viewmodels.ProductsViewModel
import com.android.example.myapplication.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ProductAdapter
    private lateinit var viewModel: ProductsViewModel
    private lateinit var listTypeString: ArrayList<String>
    private lateinit var listType: ArrayList<ProductTypeData>
    private lateinit var networkHelper: NetworkHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listTypeString = ArrayList()
        listType = ArrayList()
        networkHelper = NetworkHelper(this)

//        if (networkHelper.isNetworkConnected()){
        viewModel =
            ViewModelProvider(
                this,
                ViewModelFactory(apiService, networkHelper)
            )[ProductsViewModel::class.java]

        viewModel.productTypes.observe(this, {
            it.forEach { productType ->
                listTypeString.add(productType.name_uz)
                listType.add(productType)
            }
        })
//        }


        adapter = ProductAdapter()
        binding.rv.setHasFixedSize(true)
        binding.rv.adapter = adapter

        lifecycleScope.launch {
            viewModel.products.collectLatest {
                adapter.submitData(it)
            }
        }

        adapter.setEditListener { productData, _ ->
            baseDialog(true, productData)
        }

        adapter.setDeleteListener { productData, _ ->
            apiService.deleteProduct(productData.id).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        adapter.refresh()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }
            })
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add -> {
                baseDialog(false, null)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun baseDialog(forEdit: Boolean, productData: ProductData?) {
        val dialog = AlertDialog.Builder(this)
        val dialogBinding = AddDialogBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        var positiveTv = "Add"
        dialogBinding.spinner.adapter =
            ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_spinner_dropdown_item,
                listTypeString
            )
        if (forEdit) {
            positiveTv = "Edit"
            dialogBinding.apply {
                address.setText(productData?.address)
                name.setText(productData?.name_uz)
                cost.setText("${productData?.cost}")

            }
        }
        dialog.setPositiveButton(positiveTv) { p0, p1 ->
            val stringType = dialogBinding.spinner.selectedItem as String
            var type: ProductTypeData? = null
            listType.forEach {
                if (it.name_uz == stringType)
                    type = it
            }
            val name = dialogBinding.name.text.toString()
            val cost = dialogBinding.cost.text.toString()
            val address = dialogBinding.address.text.toString()
            val typeId = type?.id
            if (forEdit) {
                val date = Calendar.getInstance().time.time
                val product =
                    ProductData(address, cost.toInt(), date, productData!!.id, name, typeId!!)
                apiService.editProduct(product).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            adapter.refresh()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    }
                })
            } else {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                val date = simpleDateFormat.format(Calendar.getInstance().time)
                val product = RequestProductData(
                    address,
                    cost.toInt(),
                    date,
                    name,
                    typeId!!
                )
                apiService.addProduct(product).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            adapter.refresh()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    }

                })
            }
        }
        dialog.setNegativeButton("Cancel") { p0, p1 ->
        }
        dialog.show()
    }
}