package kr.book_stack.fragment


import KeyboardVisibilityUtils
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import kr.book_stack.AppViewModel
import kr.book_stack.R

import kr.book_stack.RegActivity
import kr.book_stack.appDB.data.User
import kr.book_stack.databinding.DialogDatepickerBinding

import kr.book_stack.databinding.FragmentHighlight2Binding

import kr.book_stack.fragment_reg.HFragment3
import kr.book_stack.notion.NotionAPI
import java.util.*


class HighLightFragment : Fragment() {
    private var _binding: FragmentHighlight2Binding? = null;
    private val binding get() = _binding!!
    private val cal = Calendar.getInstance()
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils
    fun newInstance(): HighLightFragment {
        return HighLightFragment()
    }


    private val viewModel: AppViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHighlight2Binding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("Recycle")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mActivity = activity as RegActivity
        val info = arguments?.getString("bookInfo")
        val title = arguments?.getString("bookName")
        val des = arguments?.getString("bookDes")
        val cover = arguments?.getString("bookCover")
        Glide
            .with(binding.hAddImgBookCover.context)
            .load(cover)
            .into(binding.hAddImgBookCover)
        binding.hAddTvBookTitle.text = title
        binding.hAddTvBookInfo.text = info

        val stringsTag = resources.getStringArray(R.array.tag_name)
        val stringsTagImg = resources.getStringArray(R.array.tag_img_name)
        val images = resources.obtainTypedArray(R.array.tag_images)
        viewModel.getAllResultTag().observe(viewLifecycleOwner, Observer { tag ->
            // Update the cached copy of the users in the adapter.
            tag?.let {
                if (tag.isEmpty()) {

                } else {
                    for (i in tag.indices) {
                        binding.chipsHighLightTag.addView(Chip(requireActivity()).apply {
                            isCheckable = true
                            text = "${tag[i].tag}"
                            isCloseIconVisible = true
                            setChipBackgroundColorResource(R.color.chip_bg)
                            setOnCloseIconClickListener {
                                binding.chipsHighLightTag.removeView(it)
                            }
                            for (j in stringsTagImg.indices) {
                                if (stringsTagImg[j] == tag[i].tagImg) {
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

        binding.hAddTvRange.text = "${cal.get(Calendar.YEAR)}년"

        binding.btnDateRange.setOnClickListener {
            dialogDate(binding.hAddTvRange)
        }
        binding.tilInfoComent.setOnClickListener {
            binding.scrollHighlight.fullScroll(ScrollView.FOCUS_UP)
        }

        var userInfo : User? = null
        viewModel.getUser("2407948260").observe(viewLifecycleOwner, Observer { user ->
            user?.let { userInfo = user }

        })
        binding.tvBtnAddHighlight.setOnClickListener {

            var tagString = ""
            var tagImgString = ""
            CoroutineScope(Dispatchers.Main).launch {

                val ids = binding.chipsHighLightTag.checkedChipIds
                if (ids.size == 0) {
                    Toast.makeText(requireActivity(), "태그를 선택해주세요.", Toast.LENGTH_LONG).show()
                } else {
                    mActivity.progressView(15000)
                    try {

                        for (id in ids) {
                            val chip: Chip = binding.chipsHighLightTag.findViewById(id)
                            val checkArray = viewModel.getAllResultTag().value
                            for (j in checkArray!!.indices) {

                                if (checkArray[j].tag == chip.text) {

                                    tagString += "${chip.text}@"
                                    tagImgString += "${checkArray[j].tagImg}@"
                                }
                            }

                        }

                        //123으로 저장된곳 협의후 수정
                         val bookDbPageId = NotionAPI.createBookPage(
                             "테스트",
                             userInfo?.bookPageId.toString(),
                             userInfo?.name.toString(),
                             "123",
                             "123",
                             "123",
                             "123",
                             "123",
                             "123",
                             cover.toString(),
                             tagString,
                             tagImgString,
                             "0",
                             binding.editInfoComent.text.toString()

                         )
                         NotionAPI.updateBookPageId(bookDbPageId)
                        //TODO 추후 룸 DB 연결부분 협의후 수정
/*                      viewModel.insert(
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
                    )*/
                    } catch (e: Exception) {
                        Log.e("HighLightFragment", "$e")
                        Toast.makeText(requireActivity(), "하이라이트 추가 API 오류.", Toast.LENGTH_LONG)
                            .show()
                    } finally {
                        mActivity.progressDismiss()
                        //mActivity.goH3Fragment()
                        mActivity.goFragment(HFragment3(), null)
                    }
                }
            }


        }
        keyboardVisibilityUtils = KeyboardVisibilityUtils(requireActivity().window,
            onShowKeyboard = { keyboardHeight ->
                binding.scrollHighlight.run {
                    smoothScrollTo(scrollX, scrollY + keyboardHeight)
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun dialogDate(inTextView: TextView) {

        val binding = DialogDatepickerBinding.inflate(requireActivity().layoutInflater)
        val dialog = BottomSheetDialog(requireActivity())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)

        val monthStr = ArrayList<String>()
        val yearStr = ArrayList<String>()
        val day30Str = ArrayList<String>()
        val day31Str = ArrayList<String>()
        for (i in 1..12) {
            monthStr.add("${i}월")
        }
        for (i in 1..30) {
            day30Str.add("${i}일")
        }
        for (i in 1..31) {
            day31Str.add("${i}일")
        }
        for (i in 1990..2030) {
            yearStr.add("${i}년")
        }

        binding.btnMonth.setOnClickListener {
            binding.btnMonth.isChecked = true
            binding.btnYear.isChecked = false
            binding.btnRange.isChecked = false

            binding.layMonth.visibility = View.VISIBLE
            binding.layYear.visibility = View.GONE
            binding.layRange.visibility = View.GONE

            binding.monthPickerYear.wrapSelectorWheel = false
            binding.monthPickerYear.minValue = 1990
            binding.monthPickerYear.maxValue = 2030
            binding.monthPickerYear.value = cal.get(Calendar.YEAR)
            binding.monthPickerYear.displayedValues =
                yearStr.toArray(arrayOfNulls<String>(yearStr.size))

            binding.monthPickerMonth.wrapSelectorWheel = false
            binding.monthPickerMonth.minValue = 1
            binding.monthPickerMonth.maxValue = 12
            binding.monthPickerMonth.value = cal.get(Calendar.MONTH) + 1
            binding.monthPickerMonth.displayedValues =
                monthStr.toArray(arrayOfNulls<String>(monthStr.size))

            binding.tvMonthPicker.text =
                "${binding.monthPickerYear.value}년 ${binding.monthPickerMonth.value}월 독서완료"
        }
        binding.monthPickerYear.setOnValueChangedListener { _, _, _ ->
            binding.tvMonthPicker.text =
                "${binding.monthPickerYear.value}년 ${binding.monthPickerMonth.value}월 독서완료"
        }
        binding.monthPickerMonth.setOnValueChangedListener { _, _, _ ->
            binding.tvMonthPicker.text =
                "${binding.monthPickerYear.value}년 ${binding.monthPickerMonth.value}월 독서완료"
        }

        binding.btnYear.setOnClickListener {
            binding.btnMonth.isChecked = false
            binding.btnYear.isChecked = true
            binding.btnRange.isChecked = false

            binding.layMonth.visibility = View.GONE
            binding.layYear.visibility = View.VISIBLE
            binding.layRange.visibility = View.GONE

            binding.yearPickerYear.wrapSelectorWheel = false
            binding.yearPickerYear.minValue = 1990
            binding.yearPickerYear.maxValue = 2030
            binding.yearPickerYear.value = cal.get(Calendar.YEAR)
            binding.yearPickerYear.displayedValues =
                yearStr.toArray(arrayOfNulls<String>(yearStr.size))

            binding.tvYearPicker.text = "${binding.yearPickerYear.value}년 독서완료"
        }
        binding.yearPickerYear.setOnValueChangedListener { _, _, _ ->
            binding.tvYearPicker.text = "${binding.yearPickerYear.value}년 독서완료"
        }


        binding.btnRange.setOnClickListener {
            binding.btnMonth.isChecked = false
            binding.btnYear.isChecked = false
            binding.btnRange.isChecked = true

            binding.layMonth.visibility = View.GONE
            binding.layYear.visibility = View.GONE
            binding.layRange.visibility = View.VISIBLE


            binding.rangePickerYear.wrapSelectorWheel = false
            binding.rangePickerYear.minValue = 1990
            binding.rangePickerYear.maxValue = 2030
            binding.rangePickerYear.value = cal.get(Calendar.YEAR)
            binding.rangePickerYear.displayedValues =
                yearStr.toArray(arrayOfNulls<String>(yearStr.size))

            binding.rangePickerMonth.wrapSelectorWheel = false
            binding.rangePickerMonth.minValue = 1
            binding.rangePickerMonth.maxValue = 12
            binding.rangePickerMonth.value = cal.get(Calendar.MONTH) + 1
            binding.rangePickerMonth.displayedValues =
                monthStr.toArray(arrayOfNulls<String>(monthStr.size))

            binding.rangePickerDay.wrapSelectorWheel = false
            binding.rangePickerDay.minValue = 1

            binding.rangePickerDay.displayedValues =
                day31Str.toArray(arrayOfNulls<String>(day31Str.size))
            binding.rangePickerDay.maxValue = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            binding.rangePickerDay.value = cal.get(Calendar.DATE)
            binding.toggleFrom.text =
                "${binding.rangePickerYear.value}년 ${binding.rangePickerMonth.value}월 ${binding.rangePickerDay.value}일"
            binding.toggleTo.text =
                "${binding.rangePickerYear.value}년 ${binding.rangePickerMonth.value}월 ${binding.rangePickerDay.value}일"
        }

        binding.rangePickerYear.setOnValueChangedListener { _, _, _ ->

            toggleRange(binding)

        }
        binding.rangePickerMonth.setOnValueChangedListener { _, _, _ ->
            toggleRange(binding)
        }
        binding.rangePickerDay.setOnValueChangedListener { _, _, _ ->
            toggleRange(binding)
        }

        binding.toggleFrom.setOnClickListener {
            binding.toggleFrom.isChecked = true
            binding.toggleTo.isChecked = false
        }
        binding.toggleTo.setOnClickListener {
            binding.toggleFrom.isChecked = false
            binding.toggleTo.isChecked = true
        }

        binding.btnDateConfirm.setOnClickListener {
            dialog.dismiss()

            if (binding.btnMonth.isChecked){
                inTextView.text = binding.tvMonthPicker.text
            }else if(binding.btnYear.isChecked){
                inTextView.text = binding.tvYearPicker.text
            }else if(binding.btnRange.isChecked){
                inTextView.text =  "${binding.toggleFrom.text}~${binding.toggleTo.text}"
            }

        }
        dialog.show()

        binding.btnMonth.performClick()


    }

    private fun toggleRange(inBinding: DialogDatepickerBinding) {
        cal.set(
            inBinding.rangePickerYear.value,
            inBinding.rangePickerMonth.value - 1,
            cal.get(Calendar.DATE)
        );
        inBinding.rangePickerDay.maxValue = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val formatter = SimpleDateFormat("yyyy년 MM월 dd일")
        val fromDate = formatter.parse(inBinding.toggleFrom.text.toString())

        if (inBinding.toggleFrom.isChecked) {
            inBinding.toggleFrom.text =
                "${inBinding.rangePickerYear.value}년 ${inBinding.rangePickerMonth.value}월 ${inBinding.rangePickerDay.value}일"
        } else if (inBinding.toggleTo.isChecked) {
            val toDate =
                formatter.parse("${inBinding.rangePickerYear.value}년 ${inBinding.rangePickerMonth.value}월 ${inBinding.rangePickerDay.value}일")
            val compare = toDate.compareTo(fromDate)
            if (compare < 0) {
                Toast.makeText(requireActivity(), "FROM이 TO보다 큽니다.", Toast.LENGTH_SHORT).show()
            } else {
                inBinding.toggleTo.text =
                    "${inBinding.rangePickerYear.value}년 ${inBinding.rangePickerMonth.value}월 ${inBinding.rangePickerDay.value}일"
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        keyboardVisibilityUtils.detachKeyboardListeners()
        _binding = null
    }

    fun scrollDown() {
        binding.scrollHighlight.post(Runnable {
            binding.scrollHighlight.fullScroll(ScrollView.FOCUS_DOWN)
        })

    }

}