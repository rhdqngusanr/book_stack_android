package kr.book_stack.fragment_reg

import KeyboardVisibilityUtils
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import kr.book_stack.AppViewModel
import kr.book_stack.R
import kr.book_stack.RegActivity
import kr.book_stack.StructData
import kr.book_stack.adapter.TagMakeViewPagerAdapter
import kr.book_stack.appDB.data.DefaultTag
import kr.book_stack.appDB.data.Tag
import kr.book_stack.databinding.DialogTagMakeBinding
import kr.book_stack.databinding.RegFragmentTagMake2Binding
import java.util.*


class TagMakeFragment2  : Fragment() {
    private var _binding: RegFragmentTagMake2Binding? = null;
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by viewModels()
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils
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

/*        binding.editTagName.setOnFocusChangeListener { _, b ->

            if (b){
                binding.editTagName.hint = "이름을 입력해주세요."
                binding.tilInfoName.isCounterEnabled = true
                binding.editTagName.isFocusable = true
            }

        }*/
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.editTagName.setOnFocusChangeListener { _, p1 ->
            if (p1){


                imm.showSoftInput(binding.editTagName, 0)

            }
        }
        binding.layBackgroundInfo2.setOnClickListener{
            imm.hideSoftInputFromWindow(binding.editTagName.windowToken, 0)
        }

        keyboardVisibilityUtils = KeyboardVisibilityUtils(requireActivity().window,
            onShowKeyboard = {
                binding.layTagAdd.setPadding(0,0,0,0)
                binding.tvTagAdd.setBackgroundResource(R.drawable.enable_btn_full)
                binding.editTagName.hint = "이름을 입력해주세요."
                binding.tilInfoName.isCounterEnabled = true
                binding.editTagName.isFocusable = true
            }, onHideKeyboard = {
                binding.layTagAdd.setPadding(16,16,16,16)
                binding.tvTagAdd.setBackgroundResource(R.drawable.enable_btn)
                binding.editTagName.hint = ""
                binding.tilInfoName.isCounterEnabled = false
                binding.editTagName.clearFocus()
            })

        binding.tvTagMakeTest.setOnClickListener {
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
        keyboardVisibilityUtils.detachKeyboardListeners()
    }
}