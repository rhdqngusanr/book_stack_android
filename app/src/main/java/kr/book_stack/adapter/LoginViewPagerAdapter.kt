package kr.book_stack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.book_stack.databinding.PagerLoginListBinding

class LoginViewPagerAdapter (private val tvBigList: ArrayList<String>, private val tvSmallList: ArrayList<String>) :
    RecyclerView.Adapter<LoginViewPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            PagerLoginListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(private val binding: PagerLoginListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String, data2: String) {
            binding.tvLayBig.text = data
            binding.tvLaySmall.text = data2
        }
    }
    override fun getItemCount(): Int {
        return tvBigList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tvBigList[position],tvSmallList[position])
    }
}