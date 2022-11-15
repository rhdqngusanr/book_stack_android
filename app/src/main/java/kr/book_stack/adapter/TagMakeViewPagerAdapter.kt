package kr.book_stack.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.book_stack.appDB.data.DefaultTag
import kr.book_stack.databinding.PagerLoginListBinding
import kr.book_stack.databinding.PagerTagMakeBinding

class TagMakeViewPagerAdapter (private val tagList: ArrayList<ArrayList<DefaultTag>>,val context :Context) :
    RecyclerView.Adapter<TagMakeViewPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            PagerTagMakeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(private val binding: PagerTagMakeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ArrayList<DefaultTag>,context: Context) {
            binding.tagMakeRecycler.layoutManager = GridLayoutManager(
                context,
               4
            )
            val mAdapter = RecyclerTagAdapter(data)
            binding.tagMakeRecycler.adapter = mAdapter
            mAdapter.itemClick = object : RecyclerTagAdapter.ItemClick {
                @SuppressLint("NotifyDataSetChanged")
                override fun onClick(view: View, position: Int) {
                   mAdapter.notifyDataSetChanged()
                }
            }
/*            binding.tagMakeRecycler.addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    DividerItemDecoration.VERTICAL
                )
            )*/


        }
    }
    override fun getItemCount(): Int {
        return tagList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tagList[position],context)
    }
}