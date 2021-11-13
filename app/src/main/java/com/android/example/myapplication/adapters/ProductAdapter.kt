package com.android.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.example.myapplication.databinding.ItemProductBinding
import com.android.example.myapplication.models.ProductData

class ProductAdapter :
    PagingDataAdapter<ProductData, ProductAdapter.Vh>(MyDiffUtil()) {
    private var listenerEdit: ((ProductData, Int) -> Unit)? = null
    private var listenerDelete: ((ProductData, Int) -> Unit)? = null

    class MyDiffUtil : DiffUtil.ItemCallback<ProductData>() {
        override fun areItemsTheSame(oldItem: ProductData, newItem: ProductData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductData, newItem: ProductData): Boolean {
            return oldItem == newItem
        }

    }

    inner class Vh(private val itemProductBinding: ItemProductBinding) :
        RecyclerView.ViewHolder(itemProductBinding.root) {
        fun onBind(product: ProductData, position: Int) {
            itemProductBinding.name.text = product.name_uz
            itemProductBinding.cost.text = "$${product.cost}"
            itemProductBinding.address.text = product.address
            itemProductBinding.editBtn.setOnClickListener {
                listenerEdit?.invoke(product, position)
            }
            itemProductBinding.deleteBtn.setOnClickListener {
                listenerDelete?.invoke(product, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh = Vh(
        ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: Vh, position: Int) {
        getItem(position)?.let { holder.onBind(it, position) }
    }

    fun setEditListener(f: (ProductData, Int) -> Unit) {
        listenerEdit = f
    }

    fun setDeleteListener(f: (ProductData, Int) -> Unit) {
        listenerDelete = f
    }

}