package kr.book_stack.fragment_reg

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import kr.book_stack.AppViewModel
import kr.book_stack.R
import kr.book_stack.RegActivity
import kr.book_stack.appDB.data.Book
import kr.book_stack.appDB.data.Tag
import kr.book_stack.databinding.DialogTagMakeBinding

import kr.book_stack.databinding.RegFragmentTagMake2Binding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TagMakeFragment2  : Fragment() {
    private var _binding: RegFragmentTagMake2Binding? = null;
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by viewModels()
    fun newInstance() : TagMakeFragment2 {
        return TagMakeFragment2()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegFragmentTagMake2Binding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mActivity = activity as RegActivity

        binding.tvTagAdd.setOnClickListener {
            viewModel.insertTag(
                Tag(binding.editTagName.text.toString(),"스마일")
            )
            mActivity.goFragment(TagFragment2(),null)
        }
        binding.tvTagmakeTest.setOnClickListener {
            dialogTagMake()
        }

    }

    private fun dialogTagMake() {

        val binding = DialogTagMakeBinding.inflate(requireActivity().layoutInflater)
        val dialog = BottomSheetDialog(requireActivity())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        binding.tvTagMakeConfirm.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}