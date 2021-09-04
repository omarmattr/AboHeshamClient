package com.ps.omarmattr.abohesham.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ps.omarmattr.abohesham.client.model.Ticket
import kotlinx.android.synthetic.main.item_ticket.view.*


class GenericAdapter<T>(
    @LayoutRes val layoutId: Int,
    var type: Int,
    val itemclick: OnListItemViewClickListener<T>
) :
    RecyclerView.Adapter<GenericAdapter.GenericViewHolder<T>>() {
    // var inflater: LayoutInflater? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<T> {
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
            )

        return GenericViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) {
        try {
            val itemViewModel = data[position]
            holder.bind(itemViewModel, type)
            holder.itemView.apply {
                setOnClickListener {
                    itemclick.onClickItem(itemViewModel, 1, position)
                }
                if(itemViewModel is Ticket ){
                    btnDelete.setOnClickListener {  itemclick.onClickDelete(itemViewModel) }

                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    class GenericViewHolder<T>(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemViewModel: T, F: Int) {
            try {
                binding.setVariable(F, itemViewModel)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


    interface OnListItemViewClickListener<T> {
        fun onClickItem(itemViewModel: T, type: Int, position: Int)
        fun onClickDelete(itemViewModel: T)
    }

    val diffCallback = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    val differ = AsyncListDiffer(this, diffCallback)

    var data: List<T>
        get() = differ.currentList
        set(value) = differ.submitList(value)


}