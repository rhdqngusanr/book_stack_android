package kr.book_stack.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.book_stack.appDB.data.DefaultTag
import kr.book_stack.databinding.RecyclerItemTagBinding

class RecyclerTagAdapter(private val dataSet: ArrayList<DefaultTag>) :
    RecyclerView.Adapter<RecyclerTagAdapter.ViewHolder>() {
    private lateinit var context: Context

    var itemClick: ItemClick? = null
    interface ItemClick {
        fun onClick(view: View, position: Int)
    }


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
        fun bind(data: DefaultTag, context: Context) {
            val test = context.resources.getDrawable(data.img,null)
            binding.imgTag.setImageResource(data.img)
            binding.tagLay.setOnClickListener {

            }

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position],context)

        holder.itemView.setOnClickListener {v->
            itemClick?.onClick(v, position)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}