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
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import kr.book_stack.AppViewModel
import kr.book_stack.R
import kr.book_stack.RegActivity
import kr.book_stack.StructData
import kr.book_stack.adapter.LoginViewPagerAdapter
import kr.book_stack.adapter.TagMakeViewPagerAdapter
import kr.book_stack.appDB.data.Book
import kr.book_stack.appDB.data.DefaultTag
import kr.book_stack.appDB.data.Tag
import kr.book_stack.databinding.DialogTagMakeBinding

import kr.book_stack.databinding.RegFragmentTagMake2Binding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

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
        binding.editTagName.setOnFocusChangeListener { _, b ->
            binding.tilInfoName.isCounterEnabled = b
            if (b){
                binding.editTagName.hint = "이름을 입력해주세요."
            }else{
                binding.editTagName.hint = ""
            }

        }
        binding.tvTagmakeTest.setOnClickListener {
            dialogTagMake()
        }

    }

    private fun dialogTagMake() {
        val stringsTagMakeImg = resources.getStringArray(R.array.tag_make_img_name)
        val images = resources.obtainTypedArray(R.array.tag_images)

        StructData.arrayTag.clear()
        for (j in stringsTagMakeImg.indices) {
           StructData.arrayTag.add(DefaultTag(stringsTagMakeImg[j],images.getResourceId(j, -1),false))
        }
        val items = StructData.arrayTag.toTypedArray()
        var tagRange1 =  items.copyOfRange(0,8).toCollection(ArrayList<DefaultTag>())
        var tagRange2 =  items.copyOfRange(8,16).toCollection(ArrayList<DefaultTag>())
        var tagRange3 =  items.copyOfRange(16,24).toCollection(ArrayList<DefaultTag>())
        var tagRange4 =  items.copyOfRange(24,32).toCollection(ArrayList<DefaultTag>())
        var tagRange5 =  items.copyOfRange(32,40).toCollection(ArrayList<DefaultTag>())
        var tagRange6 =  items.copyOfRange(40,48).toCollection(ArrayList<DefaultTag>())

        val tagRangeList = ArrayList<ArrayList<DefaultTag>>()
        tagRangeList.add(tagRange1)
        tagRangeList.add(tagRange2)
        tagRangeList.add(tagRange3)
        tagRangeList.add(tagRange4)
        tagRangeList.add(tagRange5)
        tagRangeList.add(tagRange6)

        val binding = DialogTagMakeBinding.inflate(requireActivity().layoutInflater)
        binding.viewPagerTag.adapter = TagMakeViewPagerAdapter(tagRangeList,requireActivity()) // 어댑터 생성
        binding.viewPagerTag.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로
        binding.springDotsIndicatorTag.attachTo(binding.viewPagerTag)

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