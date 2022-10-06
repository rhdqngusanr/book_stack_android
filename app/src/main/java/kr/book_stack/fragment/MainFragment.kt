package kr.book_stack.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import kr.book_stack.AppViewModel
import kr.book_stack.R
import kr.book_stack.adapter.RecyclerViewAdapter
import kr.book_stack.appDB.data.Book

import kr.book_stack.databinding.FragmentMainBinding
import kr.book_stack.notion.NotionAPI

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null;
    private val binding get() = _binding!!
    fun newInstance() : MainFragment {
        return MainFragment()
    }
    private var mAdapter: RecyclerViewAdapter? = null
    private val viewModel: AppViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_tuto, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.menu_tuto_skip -> {
                        // clearCompletedTasks()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*        binding.shareBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val data = NotionAPI.queryBookDatabaseFilters("email","test2")
                for (i in data.indices){
                    viewModel.insert(
                        Book(data[i].email,
                            data[i].page_id,
                            data[i].isbn,
                            data[i].book_status,
                            data[i].book_page,
                            data[i].look_page,
                            data[i].look_first,
                            data[i].look_last,
                            data[i].img
                        )
                    )
                }

            }
        }
        viewModel.getUser("2407948260").observe(requireActivity(), Observer { book ->
            // Update the cached copy of the users in the adapter.
            book?.let { binding.textView2.text =  "${binding.textView2.text } ${book}"}
        })*/

        //TODO 아이템리스트 연동할곳
/*        binding.recyclerViewSummary.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        mAdapter = RecyclerViewAdapter()
        binding.recyclerViewSummary.adapter = mAdapter
        binding.recyclerViewSummary.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            )
        )
        mAdapter?.itemClick = object : RecyclerViewAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                
            }
        }*/
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}