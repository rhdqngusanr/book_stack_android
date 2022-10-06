package kr.book_stack.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.book_stack.api.ApiData
import kr.book_stack.databinding.RecyclerHighlightBinding
import kr.book_stack.databinding.RecyclerItemBinding

class RecyclerHighlightAdapter(private val dataSet: ArrayList<String>) :
    RecyclerView.Adapter<RecyclerHighlightAdapter.ViewHolder>() {
    var itemClick: ItemClick? = null
    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            RecyclerHighlightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(private val binding: RecyclerHighlightBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            Glide
                .with(binding.imageView.context)
                .load(data)
                .into(binding.imageView)
            binding.tvHighlightTitle.text = data
            binding.tvHighlightRange.text = data

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])

        holder.itemView.setOnClickListener {v->
            itemClick?.onClick(v, position)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}