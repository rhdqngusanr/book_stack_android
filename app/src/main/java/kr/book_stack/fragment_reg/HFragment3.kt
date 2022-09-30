package kr.book_stack.fragment_reg

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.book_stack.RegActivity
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

        binding.btnHighlightAdd.setOnClickListener {

           // mActivity.goSearchFragment()
            mActivity.goFragment(SearchFragment(),null)

        }

        binding.tvHighlightAddConfirm.setOnClickListener {

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}