package kr.book_stack.fragment_reg

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import kr.book_stack.*
import kr.book_stack.appDB.data.DefaultTag
import kr.book_stack.databinding.RegFragmentTag2Binding

class TagFragment2  : Fragment() {
    private var _binding: RegFragmentTag2Binding? = null;
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by viewModels()

    fun newInstance() : TagFragment2 {
        return TagFragment2()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegFragmentTag2Binding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mActivity = activity as RegActivity

        binding.btnTagConfirm.setOnClickListener {
            mActivity.goHighLightFragment()
        }
        binding.tvMakeTag.setOnClickListener {
            mActivity.goTagMakeFragment()
        }

        binding.chipGroupTag.addView(Chip(requireActivity()).apply {
            text = "반갑다"
            isCloseIconVisible = true
            chipIcon = ContextCompat.getDrawable(requireActivity(),R.drawable.svg_back2)

        })

        viewModel.getAllTag().observe(requireActivity(), Observer { tag ->
            // Update the cached copy of the users in the adapter.
            val tagArray = Struct.defaultTag()
            tagArray.add(DefaultTag("슬픔",2131230909))

            val mAdapter = RecyclerTagAdapter(tagArray)

            binding.recyclerViewTag.layoutManager = GridLayoutManager(
                requireActivity(),
                6
            )
            binding.recyclerViewTag.adapter = mAdapter
            binding.recyclerViewTag.addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    DividerItemDecoration.VERTICAL
                )
            )
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}