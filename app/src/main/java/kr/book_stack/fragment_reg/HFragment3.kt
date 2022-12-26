package kr.book_stack.fragment_reg

import android.os.Bundle
import android.view.*
import android.widget.ScrollView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kr.book_stack.AppViewModel
import kr.book_stack.R
import kr.book_stack.RegActivity
import kr.book_stack.adapter.RecyclerHighlightAdapter
import kr.book_stack.adapter.RecyclerViewAdapter
import kr.book_stack.api.ApiData
import kr.book_stack.appDB.data.Book
import kr.book_stack.appDB.data.User
import kr.book_stack.databinding.DialogAddSearchBinding
import kr.book_stack.databinding.FragmentHModifyBinding
import kr.book_stack.databinding.RegFragmentH3Binding
import kr.book_stack.fragment.SearchFragment


class HFragment3  : Fragment() {
    private var _binding: RegFragmentH3Binding? = null
    private val binding get() = _binding!!
    fun newInstance() : HFragment3 {
        return HFragment3()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegFragmentH3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mActivity = activity as RegActivity
        val viewModel: AppViewModel by viewModels()
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_reg_highlight, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_reg_skip -> {
                        // clearCompletedTasks()
                        true
                    }

                    else -> false
                }

            }
        }, viewLifecycleOwner, Lifecycle.State.CREATED)
        var bookInfo: List<Book>? = null
        viewModel.getAll().observe(viewLifecycleOwner, Observer { user ->
            user?.let { bookInfo = user }
            if (bookInfo!!.isEmpty()){
                binding.highImage.visibility = View.VISIBLE
                binding.recyclerViewHighlight.visibility = View.GONE
                binding.tvHighlightAddConfirm.isEnabled = false
            }else{
                binding.highImage.visibility = View.GONE
                binding.recyclerViewHighlight.visibility = View.VISIBLE
                binding.recyclerViewHighlight.layoutManager = LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                val mAdapter = RecyclerHighlightAdapter(bookInfo!!)
                binding.recyclerViewHighlight.adapter = mAdapter
                binding.tvHighlightAddConfirm.isEnabled = true

                mAdapter.itemClick = object : RecyclerHighlightAdapter.ItemClick {
                    override fun onClick(view: View, position: Int) {
                        dialogModifyHighlight()
                    }
                }
            }



        })

        binding.btnHighlightAdd.setOnClickListener {

           // mActivity.goSearchFragment()
            mActivity.goFragment(SearchFragment(),null)
           // dialogModifyHighlight()

        }

        binding.tvHighlightAddConfirm.setOnClickListener {
            mActivity.goMain()
        }
    }

    private fun dialogModifyHighlight() {
        //val view = View.inflate(this@ActivityAdminMain, R.layout.dialog_add_customer, null)
        val binding = FragmentHModifyBinding.inflate(requireActivity().layoutInflater)
        val builder = AlertDialog.Builder(requireActivity(), R.style.popCasterDlgTheme)
        builder.setView(binding.root)
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.window!!.attributes.windowAnimations = R.style.AnimationPopupStyle
        alertDialog.setCancelable(true)
        binding.tilInfoComent.setOnClickListener {
            binding.scrollHighlight.fullScroll(ScrollView.FOCUS_UP)
        }
        binding.imgClose.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}