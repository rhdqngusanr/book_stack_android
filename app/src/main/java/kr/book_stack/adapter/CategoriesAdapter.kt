package kr.book_stack.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.book_stack.Category
import kr.book_stack.ItemsAdapter
import kr.book_stack.R


class CategoriesAdapter(
    private val context: Context,
    private val listOfCategories: List<Category>
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(listOfCategories[position])
    }

    override fun getItemCount(): Int {
        return listOfCategories.size
    }

    class CategoryViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {

        fun bind(category: Category) {
            view.findViewById<TextView>(R.id.categoryName).text = category.name
            view.findViewById<RecyclerView>(R.id.recyclerView_in_summary).adapter =
                ItemsAdapter(view.context, category.listOfItems)
        }
    }



}
