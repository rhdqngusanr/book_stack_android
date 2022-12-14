package kr.book_stack.fragment_reg

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.google.android.material.chip.Chip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.book_stack.*
import kr.book_stack.appDB.AppDatabase
import kr.book_stack.appDB.DAO
import kr.book_stack.appDB.Repository
import kr.book_stack.appDB.data.DefaultTag
import kr.book_stack.appDB.data.ResultTag
import kr.book_stack.appDB.data.Tag
import kr.book_stack.databinding.RegFragmentTag2Binding
import kr.book_stack.notion.NotionAPI


class TagFragment2 : Fragment() {
    private var _binding: RegFragmentTag2Binding? = null;
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by viewModels()

    fun newInstance(): TagFragment2 {
        return TagFragment2()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegFragmentTag2Binding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("UseCompatLoadingForDrawables", "Recycle")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mActivity = activity as RegActivity
        val stringsTag = resources.getStringArray(R.array.tag_name)
        val stringsTagImg = resources.getStringArray(R.array.tag_img_name)
        val stringsTagMakeImgName = resources.getStringArray(R.array.tag_make_img_name)
        val images = resources.obtainTypedArray(R.array.tag_images)
        val tagStringArray = ArrayList<String>()
        var userInfo = ""
        viewModel.getUser("test").observe(viewLifecycleOwner, Observer { user ->
            user?.let { userInfo = user.tagPageId.toString()}

        })

        binding.btnTagConfirm.setOnClickListener {
            tagStringArray.clear()
            CoroutineScope(Dispatchers.Main).launch {
                val ids = binding.chipGroupTag.checkedChipIds
                if (ids.size == 0) {
                    Toast.makeText(requireActivity(), "태그를 선택해주세요.", Toast.LENGTH_LONG).show()
                } else {
                    mActivity.progressView(15000)
                    try {

                        for (id in ids) {
                            val chip: Chip = binding.chipGroupTag.findViewById(id)
                            val checkArray = viewModel.getAllTag().value
                                for (j in checkArray!!.indices) {

                                if (checkArray[j].tag == chip.text) {

                                    val tagDbPageId = NotionAPI.createTagPage(
                                        "테스트",
                                        userInfo,
                                        chip.text.toString(),
                                        checkArray[j].tagImg.toString()
                                    )
                                    NotionAPI.updateTagPageId(tagDbPageId)
                                    viewModel.insertResultTag(
                                        ResultTag(chip.text.toString(), checkArray[j].tagImg.toString(),tagDbPageId)
                                    )
                                }
                            }

                        }
                    } catch (e: Exception) {
                        Log.e("TagFragment2", "$e")
                        Toast.makeText(requireActivity(), "태그 API 오류.", Toast.LENGTH_LONG).show()
                    } finally {
                        mActivity.progressDismiss()
                        //mActivity.goH3Fragment()
                        mActivity.goFragment(HFragment3(),null)
                    }
                }
            }


        }
        binding.tvMakeTag.setOnClickListener {
            //mActivity.goTagMakeFragment()
            mActivity.goFragment(TagMakeFragment2(),null)
        }


        viewModel.getAllTag().observe(viewLifecycleOwner, Observer { tag ->
            // Update the cached copy of the users in the adapter.
            tag?.let {
                if (tag.isEmpty()) {
                    for (i in stringsTag.indices) {
                        viewModel.insertTag(
                            Tag(stringsTag[i],stringsTagImg[i])
                        )
                    }
                } else {
                    for (i in tag.indices.reversed() ) {
                        binding.chipGroupTag.addView(Chip(requireActivity()).apply {
                            isCheckable = true
                            text = "${tag[i].tag}"
                            chipMinHeight = dpToPx(requireActivity(),40f)
                            setTextAppearance(R.style.label_2)
                            chipIconSize= dpToPx(requireActivity(),18f)
                            iconEndPadding = dpToPx(requireActivity(),-4f)
                            chipStartPadding = dpToPx(requireActivity(),12f)
                            chipEndPadding = dpToPx(requireActivity(),12f)
                            isCheckedIconVisible = false

                            setChipBackgroundColorResource(R.color.chip_bg)
/*                            setOnCloseIconClickListener {
                                binding.chipGroupTag.removeView(it)
                            }*/
                            setOnCheckedChangeListener { _, b ->
                                if (b){
                                    closeIcon = ContextCompat.getDrawable(requireActivity(),R.drawable.svg_tag_check)
                                    isCloseIconVisible = true
                                    closeIconStartPadding = dpToPx(requireActivity(),-4f)
                                    chipEndPadding = dpToPx(requireActivity(),12f)
                                }else{
                                    isCloseIconVisible = false
                                    chipEndPadding = dpToPx(requireActivity(),12f)
                                }
                            }


                            for (j in stringsTagMakeImgName.indices) {
                                if (stringsTagMakeImgName[j] == tag[i].tagImg) {
                                    chipIcon = ContextCompat.getDrawable(
                                        requireActivity(),
                                        images.getResourceId(j, -1)
                                    )
                                }
                            }
                        })
                    }
                }
            }
        })

    }
    fun dpToPx(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}