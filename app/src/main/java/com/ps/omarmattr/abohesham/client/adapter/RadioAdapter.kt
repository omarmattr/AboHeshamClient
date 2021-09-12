package com.ps.omarmattr.abohesham.client.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.ps.omarmattr.abohesham.client.R
import com.ps.omarmattr.abohesham.client.model.Category
import kotlinx.android.synthetic.main.item_category_home.view.*

class RadioAdapter (
    var activity: Activity,
    var data: ArrayList<Category>,

    private val click: OnClick
    ) :
        RecyclerView.Adapter<RadioAdapter.ViewHolder>() {
    interface OnClick {
        fun onClick(category:Category)
    }
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

private val arrayView=ArrayList<RadioButton>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(activity).inflate(R.layout.item_category_home, parent, false)
            )
        }
        override fun getItemCount(): Int {
            return data.size
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.apply {
                arrayView.add(tvRadioBtn)
                tvRadioBtn.text=data[position].name
                tvRadioBtn.setOnClickListener{
                    for (i in arrayView){i.isChecked=false}
                    tvRadioBtn.isChecked=true
                    click.onClick(data[position])

                }

            }

        }
    }
