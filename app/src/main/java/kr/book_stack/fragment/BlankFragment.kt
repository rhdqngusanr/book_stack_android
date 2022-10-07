package kr.book_stack.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.book_stack.AppViewModel
import kr.book_stack.Struct
import kr.book_stack.appDB.data.Book
import kr.book_stack.databinding.FragmentBlankBinding
import kr.book_stack.notion.NotionAPI

class BlankFragment : Fragment() {
    private var _binding: FragmentBlankBinding? = null;
    private val binding get() = _binding!!
    fun newInstance() : BlankFragment {
        return BlankFragment()
    }

    private val viewModel: AppViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlankBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shareBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
/*                val data = NotionAPI.queryBookDatabaseFilters("email","test2")
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

            */}
        }
        viewModel.getUser("2407948260").observe(requireActivity(), Observer { book ->
            // Update the cached copy of the users in the adapter.
            book?.let { binding.textView2.text =  "${binding.textView2.text } ${book}"}
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}