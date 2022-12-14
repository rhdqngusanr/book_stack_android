package kr.book_stack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.book_stack.databinding.PagerLoginListBinding
import kr.book_stack.databinding.PagerTutoListBinding

class TutorialViewPagerAdapter (private val tvList: ArrayList<String>) :
    RecyclerView.Adapter<TutorialViewPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            PagerTutoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(private val binding: PagerTutoListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            binding.pagerTvTutorial.text = data

        }
    }
    override fun getItemCount(): Int {
        return tvList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tvList[position])
    }
}