package kr.book_stack.fragment

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.text.parseAsHtml
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
import com.ahmadhamwi.tabsync.TabbedListMediator
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import kr.book_stack.*
import kr.book_stack.adapter.CategoriesAdapter
import kr.book_stack.adapter.RecyclerViewAdapter
import kr.book_stack.api.ApiData
import kr.book_stack.appDB.data.Book
import kr.book_stack.databinding.DialogAddSearchBinding
import kr.book_stack.databinding.DialogMainSummaryBinding

import kr.book_stack.databinding.FragmentMainBinding
import kr.book_stack.notion.NotionAPI
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null;
    private val binding get() = _binding!!
    fun newInstance(): MainFragment {
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
        binding.btnSummary.setOnClickListener {
            dialogSummary()
        }


    }

    @SuppressLint("SetTextI18n")
    private fun dialogSummary() {
        //val view = View.inflate(this@ActivityAdminMain, R.layout.dialog_add_customer, null)
        val binding = DialogMainSummaryBinding.inflate(requireActivity().layoutInflater)
        val builder = AlertDialog.Builder(requireActivity(), R.style.popCasterDlgTheme)
        builder.setView(binding.root)
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()

        var test = ArrayList<Category>()
        var test2 = ArrayList<Book>()
        test2.add(
            Book(
                "test", "test", "test", "test", "test", "test",
                "test",
                "test",
                "test",
                "test",

                "test",
                "test",
                "test",
                "test",
                "test",
                "test"

            )
        )
        test2.add(
            Book(
                "test2", "test", "test", "test", "test", "test",
                "test",
                "test",
                "test",
                "test",

                "test",
                "test",
                "test",
                "test",
                "test",
                "test"
            )
        )
        test2.add(
            Book(
                "test3", "test", "test", "test", "test", "test",
                "test",
                "test",
                "test",
                "test",

                "test",
                "test",
                "test",
                "test",
                "test",
                "test"
            )
        )
        var test3 = ArrayList<Book>()
        test3.add(
            Book(
                "test9", "test", "test", "test", "test", "test",
                "test",
                "test",
                "test",
                "test",

                "test",
                "test",
                "test",
                "test",
                "test",
                "test"
            )
        )
        test3.add(
            Book(
                "test10", "test", "test", "test", "test", "test",
                "test",
                "test",
                "test",
                "test",

                "test",
                "test",
                "test",
                "test",
                "test",
                "test"
            )
        )
        test.add(Category("test1 group", test2))
        test.add(Category("test2 group", test3))
        test.add(Category("test3 group", test2))
        test.add(Category("test4 group", test2))
        test.add(Category("test5 group", test2))

        for (te in test) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(te.name))
        }

        binding.recyclerViewMainSummary.adapter = CategoriesAdapter(requireActivity(), test)
        TabbedListMediator(
            binding.recyclerViewMainSummary,
            binding.tabLayout,
            test.indices.toList(),
            true
        ).attach()

        binding.imgClose.setOnClickListener {
            MyUtil.dialogCloseTypeView(requireActivity(), "종료", "종료함?")
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}