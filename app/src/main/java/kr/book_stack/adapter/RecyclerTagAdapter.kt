package kr.book_stack.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import kr.book_stack.R
import kr.book_stack.StructData
import kr.book_stack.appDB.data.DefaultTag
import kr.book_stack.databinding.RecyclerItemTagBinding

class RecyclerTagAdapter(private val dataSet: ArrayList<DefaultTag>) :
    RecyclerView.Adapter<RecyclerTagAdapter.ViewHolder>() {
    private lateinit var context: Context


    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            RecyclerItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context


        return ViewHolder(binding)
    }

    class ViewHolder(val binding: RecyclerItemTagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(data: DefaultTag, context: Context, inPosition: Int, listener: ItemClick?) {
            val test = context.resources.getDrawable(data.img, null)
            Log.e("in", "in")
            for (i in StructData.arrayTag.indices) {
                if (StructData.arrayTag[i].name == data.name) {
                    if (StructData.arrayTag[i].check) {
                        binding.tagLay.setBackgroundResource(R.drawable.bg_radius_round_click)
                    } else {
                        binding.tagLay.setBackgroundResource(R.drawable.bg_radius_round)
                    }
                }

            }

            binding.imgTag.setImageResource(data.img)
            binding.tagLay.setOnClickListener { v ->
                for (i in StructData.arrayTag.indices) {
                    StructData.arrayTag[i].check = false
                    if (StructData.arrayTag[i].name == data.name) {
                        StructData.arrayTag[i].check = true

                    }

                }


                //binding.tagLay.setBackgroundResource(R.drawable.bg_radius_round_click)
                Log.e("RecyclerTagAdapter", "itemView onClick")
                listener?.onClick(v, inPosition)

            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position], context, position, itemClick)


    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}