package kr.book_stack.fragment


import KeyboardVisibilityUtils
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast

import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog

import kr.book_stack.AppViewModel

import kr.book_stack.RecyclerViewAdapter
import kr.book_stack.RegActivity
import kr.book_stack.databinding.DialogDatepickerBinding
import kr.book_stack.databinding.DialogDatepickerRangeBinding

import kr.book_stack.databinding.FragmentHighlight2Binding

import kr.book_stack.databinding.FragmentHighlightBinding
import java.util.*


class HighLightFragment : Fragment() {
    private var _binding: FragmentHighlight2Binding? = null;
    private val binding get() = _binding!!
    private var mAdapter: RecyclerViewAdapter? = null
    private val cal = Calendar.getInstance()
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils
    fun newInstance() : HighLightFragment {
        return HighLightFragment()
    }

    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHighlight2Binding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mActivity = activity as RegActivity
        binding.btnDateRange.setOnClickListener{
            dialogDate()
        }
        binding.tilInfoComent.setOnClickListener {
            binding.scrollHighlight.fullScroll(ScrollView.FOCUS_UP)
        }

        keyboardVisibilityUtils = KeyboardVisibilityUtils(requireActivity().window,
            onShowKeyboard = { keyboardHeight ->
                binding.scrollHighlight.run {
                    smoothScrollTo(scrollX, scrollY + keyboardHeight)
                }
            })
    }

    private fun dialogDate() {

        val binding = DialogDatepickerBinding.inflate(requireActivity().layoutInflater)
        val dialog = BottomSheetDialog(requireActivity())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)

        val monthStr = ArrayList<String>()
        val yearStr = ArrayList<String>()
        val day30Str = ArrayList<String>()
        val day31Str = ArrayList<String>()
        for (i in 1..12){
            monthStr.add("${i}월")
        }
        for (i in 1..30){
            day30Str.add("${i}일")
        }
        for (i in 1..31){
            day31Str.add("${i}일")
        }
        for (i in 1990..2030){
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
            binding.monthPickerYear.displayedValues = yearStr.toArray(arrayOfNulls<String>(yearStr.size))

            binding.monthPickerMonth.wrapSelectorWheel = false
            binding.monthPickerMonth.minValue = 1
            binding.monthPickerMonth.maxValue = 12
            binding.monthPickerMonth.value =  cal.get(Calendar.MONTH) + 1
            binding.monthPickerMonth.displayedValues = monthStr.toArray(arrayOfNulls<String>(monthStr.size))

            binding.tvMonthPicker.text = "${binding.monthPickerYear.value}년 ${binding.monthPickerMonth.value}월 독서완료"
        }
        binding.monthPickerYear.setOnValueChangedListener { _, _, _ ->
            binding.tvMonthPicker.text = "${binding.monthPickerYear.value}년 ${binding.monthPickerMonth.value}월 독서완료"
        }
        binding.monthPickerMonth.setOnValueChangedListener { _, _, _ ->
            binding.tvMonthPicker.text = "${binding.monthPickerYear.value}년 ${binding.monthPickerMonth.value}월 독서완료"
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
            binding.yearPickerYear.displayedValues = yearStr.toArray(arrayOfNulls<String>(yearStr.size))

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
            binding.rangePickerYear.displayedValues = yearStr.toArray(arrayOfNulls<String>(yearStr.size))

            binding.rangePickerMonth.wrapSelectorWheel = false
            binding.rangePickerMonth.minValue = 1
            binding.rangePickerMonth.maxValue = 12
            binding.rangePickerMonth.value =  cal.get(Calendar.MONTH) + 1
            binding.rangePickerMonth.displayedValues = monthStr.toArray(arrayOfNulls<String>(monthStr.size))

            binding.rangePickerDay.wrapSelectorWheel = false
            binding.rangePickerDay.minValue = 1

            binding.rangePickerDay.displayedValues = day31Str.toArray(arrayOfNulls<String>(day31Str.size))
            binding.rangePickerDay.maxValue = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            binding.rangePickerDay.value =  cal.get(Calendar.DATE)
            binding.toggleFrom.text = "${binding.rangePickerYear.value}년 ${binding.rangePickerMonth.value}월 ${binding.rangePickerDay.value}일"
            binding.toggleTo.text = "${binding.rangePickerYear.value}년 ${binding.rangePickerMonth.value}월 ${binding.rangePickerDay.value}일"
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
            dialogDateRange()
        }
        dialog.show()

        binding.btnMonth.performClick()
    }

    private fun toggleRange(inBinding:DialogDatepickerBinding){
        cal.set(inBinding.rangePickerYear.value,inBinding.rangePickerMonth.value-1,cal.get(Calendar.DATE));
        inBinding.rangePickerDay.maxValue = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val formatter = SimpleDateFormat("yyyy년 MM월 dd일")
        val fromDate = formatter.parse( inBinding.toggleFrom.text.toString())

        if (inBinding.toggleFrom.isChecked){
            inBinding.toggleFrom.text = "${inBinding.rangePickerYear.value}년 ${inBinding.rangePickerMonth.value}월 ${inBinding.rangePickerDay.value}일"
        }else if (inBinding.toggleTo.isChecked){
            val toDate = formatter.parse( "${inBinding.rangePickerYear.value}년 ${inBinding.rangePickerMonth.value}월 ${inBinding.rangePickerDay.value}일")
            val compare = toDate.compareTo(fromDate)
            if (compare < 0) {
                Toast.makeText(requireActivity(), "FROM이 TO보다 큽니다.", Toast.LENGTH_SHORT).show()
            }else{
                inBinding.toggleTo.text = "${inBinding.rangePickerYear.value}년 ${inBinding.rangePickerMonth.value}월 ${inBinding.rangePickerDay.value}일"
            }
                
        }
    }

    private fun dialogDateRange() {


        val binding = DialogDatepickerRangeBinding.inflate(requireActivity().layoutInflater)
        val dialog = BottomSheetDialog(requireActivity())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)

        dialog.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        keyboardVisibilityUtils.detachKeyboardListeners()
        _binding = null
    }

    fun scrollDown(){
        binding.scrollHighlight.post(Runnable {
            binding.scrollHighlight.fullScroll(ScrollView.FOCUS_DOWN)
        })

    }

}