package kr.book_stack.fragment_reg

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kr.book_stack.databinding.RegFragmentResult4Binding


class ResultFragment4  : Fragment() {
    private var _binding: RegFragmentResult4Binding? = null;
    private val binding get() = _binding!!
    fun newInstance() : ResultFragment4 {
        return ResultFragment4()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegFragmentResult4Binding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}