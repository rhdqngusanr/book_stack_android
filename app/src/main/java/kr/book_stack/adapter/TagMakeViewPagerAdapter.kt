package kr.book_stack.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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
    var change: ChangeListen? = null
    interface ChangeListen {
        fun onChange(view: View)
    }
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
        @SuppressLint("NotifyDataSetChanged")
        fun bind(data: ArrayList<DefaultTag>, context: Context, listener: ChangeListen?) {
            binding.tagMakeRecycler.layoutManager = GridLayoutManager(
                context,
               4
            )
            val mAdapter = RecyclerTagAdapter(data)
            binding.tagMakeRecycler.adapter = mAdapter
            mAdapter.notifyDataSetChanged()
            mAdapter.itemClick = object : RecyclerTagAdapter.ItemClick {
                @SuppressLint("NotifyDataSetChanged")
                override fun onClick(view: View, position: Int) {
                    Log.e("TagMakeViewPagerAdapter", "onClick")
                   mAdapter.notifyDataSetChanged()
                    listener?.onChange(view)
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
        holder.bind(tagList[position],context,change)
    }
}