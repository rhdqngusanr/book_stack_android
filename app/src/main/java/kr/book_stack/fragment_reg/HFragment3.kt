package kr.book_stack.fragment_reg

import KeyboardVisibilityUtils
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.book_stack.*
import kr.book_stack.adapter.RecyclerHighlightAdapter
import kr.book_stack.adapter.RecyclerViewAdapter
import kr.book_stack.api.ApiData
import kr.book_stack.appDB.data.Book
import kr.book_stack.appDB.data.User
import kr.book_stack.databinding.*
import kr.book_stack.fragment.SearchFragment
import kr.book_stack.notion.NotionAPI
import java.util.*


class HFragment3 : Fragment() {
    private var _binding: RegFragmentH3Binding? = null
    private val binding get() = _binding!!
    private val cal = Calendar.getInstance()
    private  var keyboardVisibilityUtils: KeyboardVisibilityUtils? = null
    var mActivity : RegActivity? = null
    fun newInstance(): HFragment3 {
        return HFragment3()
    }

    private val viewModel: AppViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegFragmentH3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = activity as RegActivity

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_reg_highlight, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_reg_skip -> {
                        MyUtil.dialogCloseTypeView(requireActivity(),"가입완료페이지","굿")
                        true
                    }
                    android.R.id.home -> {
                        mActivity!!.onBackPressed()
                        //mActivity.goFragment(InfoFragment1(),null)
                        true
                    }
                    else -> false
                }

            }
        }, viewLifecycleOwner, Lifecycle.State.CREATED)
        var bookInfo: List<Book>? = null
        viewModel.getAll().observe(viewLifecycleOwner, Observer { user ->
            user?.let { bookInfo = user }
            if (bookInfo!!.isEmpty()) {
                binding.highImage.visibility = View.VISIBLE
                binding.recyclerViewHighlight.visibility = View.GONE
                binding.tvHighlightAddConfirm.isEnabled = false
            } else {
                binding.highImage.visibility = View.GONE
                binding.recyclerViewHighlight.visibility = View.VISIBLE
                binding.recyclerViewHighlight.layoutManager = LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                val mAdapter = RecyclerHighlightAdapter(bookInfo!!)
                binding.recyclerViewHighlight.adapter = mAdapter
                binding.tvHighlightAddConfirm.isEnabled = true

                mAdapter.itemClick = object : RecyclerHighlightAdapter.ItemClick {
                    override fun onClick(view: View, position: Int) {
                        dialogModifyHighlight(bookInfo!![position])
                    }
                }
            }


        })

        binding.btnHighlightAdd.setOnClickListener {

            // mActivity.goSearchFragment()
            mActivity!!.goFragment(SearchFragment(), null)
            // dialogModifyHighlight()

        }

        binding.tvHighlightAddConfirm.setOnClickListener {
            mActivity!!.goMain()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun dialogModifyHighlight(inBook: Book) {
        //val view = View.inflate(this@ActivityAdminMain, R.layout.dialog_add_customer, null)

        val binding = FragmentHModifyBinding.inflate(requireActivity().layoutInflater)
        val builder = AlertDialog.Builder(requireActivity(), R.style.popCasterDlgTheme)
        builder.setView(binding.root)
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.window!!.attributes.windowAnimations = R.style.AnimationPopupStyle
        alertDialog.setCancelable(true)
        Glide
            .with(binding.hAddImgBookCover.context)
            .load(inBook.img)
            .into(binding.hAddImgBookCover)
        binding.hAddTvBookTitle.text = inBook.name
        binding.hAddTvBookInfo.text = inBook.bookInfo

        val stringsTagImg = resources.getStringArray(R.array.tag_make_img_name)
        val images = resources.obtainTypedArray(R.array.tag_images)

        keyboardVisibilityUtils = KeyboardVisibilityUtils(alertDialog.window!!,
            onShowKeyboard = { keyboardHeight ->
                    binding.scrollHighlight.run {
                        smoothScrollTo(scrollX, scrollY + keyboardHeight)
                    } //실행할 코드

            }, onHideKeyboard = {

            })

        viewModel.getAllResultTag().observe(viewLifecycleOwner, Observer { tag ->
            // Update the cached copy of the users in the adapter.
            tag?.let {
                if (tag.isEmpty()) {

                } else {
                    var  thisChip :Chip
                    for (i in tag.indices) {
                        binding.chipsHighLightTag.addView(Chip(requireActivity()).apply {
                            isCheckable = true
                            text = "${tag[i].tag}"
                            chipMinHeight = dpToPx(requireActivity(), 32f)
                            setTextAppearance(R.style.label_2)
                            chipIconSize = dpToPx(requireActivity(), 18f)
                            iconEndPadding = dpToPx(requireActivity(), -4f)
                            chipStartPadding = dpToPx(requireActivity(), 12f)
                            chipEndPadding = dpToPx(requireActivity(), 12f)
                            isCheckedIconVisible = false
                            setChipBackgroundColorResource(R.color.chip_bg_h2)
/*                            setOnCloseIconClickListener {
                                binding.chipGroupTag.removeView(it)
                            }*/

                            setOnCheckedChangeListener { v, b ->
                                binding.horizontalScrollView.visibility = View.GONE
                                binding.horizontalScrollView.animation = animation
                                binding.tvBtnAddHighlight.isEnabled = true
                                binding.chipsHighLightTagCheck.addView(Chip(requireActivity()).apply {
                                    isCheckable = true
                                    isChecked = true
                                    text = v.text
                                    chipMinHeight = dpToPx(requireActivity(), 32f)
                                    setTextAppearance(R.style.label_2)
                                    chipIconSize = dpToPx(requireActivity(), 18f)
                                    iconEndPadding = dpToPx(requireActivity(), -4f)
                                    chipStartPadding = dpToPx(requireActivity(), 12f)
                                    chipEndPadding = dpToPx(requireActivity(), 12f)
                                    isCheckedIconVisible = false
                                    isCloseIconVisible = true
                                    setChipBackgroundColorResource(R.color.chip_bg_h2)

                                    for (j in tag.indices) {
                                        if (tag[j].tag == v.text) {
                                            for (k in stringsTagImg.indices) {
                                                if (stringsTagImg[k] == tag[j].tagImg) {
                                                    chipIcon = ContextCompat.getDrawable(
                                                        requireActivity(),
                                                        images.getResourceId(k, -1)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    setOnCloseIconClickListener {
                                        binding.chipsHighLightTagCheck.removeAllViews()
                                        binding.tvBtnAddHighlight.isEnabled = false
                                        binding.horizontalScrollView.visibility = View.VISIBLE
                                        binding.horizontalScrollView.animation = animation
                                    }

                                })
                            }

                            if ("$text@" == inBook.tag) {
                                thisChip = this
                                thisChip.isChecked = true
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

        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.scrollHighlight.setOnTouchListener { _, _ ->
            imm.hideSoftInputFromWindow(binding.editInfoComent.windowToken, 0)
        }
        if (inBook.lookFirst != inBook.lookLast ){
            binding.hAddTvRange.text = "${inBook.lookFirst} ~ ${inBook.lookLast}"
        }else{
            binding.hAddTvRange.text = "${inBook.lookFirst}"
        }

        binding.editInfoComent.setText("${inBook.comment}")
        binding.tilInfoComent.setOnClickListener {
            binding.scrollHighlight.fullScroll(ScrollView.FOCUS_DOWN)
        }

        binding.btnDateRange.setOnClickListener {
            dialogDate(binding.hAddTvRange)
        }


        binding.tvBookHDelete.setOnClickListener {
            dialogPopUpDelete(alertDialog, inBook)
        }

        binding.imgClose.setOnClickListener {
            alertDialog.dismiss()
        }
        binding.tvBtnAddHighlight.setOnClickListener {

            var tagString = ""
            var tagImgString = ""
            CoroutineScope(Dispatchers.Main).launch {

                val ids = binding.chipsHighLightTagCheck.checkedChipIds
                if (ids.size == 0) {
                    Toast.makeText(requireActivity(), "태그를 선택해주세요.", Toast.LENGTH_LONG).show()
                } else {
                    mActivity!!.progressView(15000)
                    try {

                        for (id in ids) {
                            val chip: Chip = binding.chipsHighLightTagCheck.findViewById(id)
                            val checkArray = viewModel.getAllResultTag().value
                            for (j in checkArray!!.indices) {

                                if (checkArray[j].tag == chip.text) {

                                    tagString += "${chip.text}@"
                                    tagImgString += "${checkArray[j].tagImg}@"
                                }
                            }

                        }

                        //123으로 저장된곳 협의후 수정
                        val arr: List<String> = if (binding.hAddTvRange.text.contains("~")) {
                            binding.hAddTvRange.text.split("~")
                        } else {
                            val str = "${binding.hAddTvRange.text}~${binding.hAddTvRange.text}"
                            str.split("~")
                        }

                        NotionAPI.updateBook(
                            inBook.page_id,
                            inBook.bookStatus.toString(),
                            inBook.bookPage.toString(),
                            inBook.lookPage.toString(),
                            arr[0],
                            arr[1],
                            inBook.img.toString(),
                            tagString,
                            tagImgString,
                            inBook.highlight.toString(),
                            binding.editInfoComent.text.toString()

                        )
                        //TODO 추후 룸 DB 연결부분 협의후 수정
                        viewModel.insert(
                            Book(
                                Struct.loginId,
                                inBook.page_id,
                                inBook.name.toString(),
                                inBook.author.toString(),
                                inBook.isbn,
                                inBook.bookStatus,
                                inBook.bookPage,
                                inBook.lookPage,
                                arr[0],
                                arr[1],
                                inBook.img,
                                tagString,
                                tagImgString,
                                inBook.highlight,
                                binding.editInfoComent.text.toString(),
                                inBook.bookInfo
                            )
                        )
                    } catch (e: Exception) {
                        Log.e("HighLightFragment", "$e")
                        Toast.makeText(requireActivity(), "하이라이트 수정 API 오류.", Toast.LENGTH_LONG)
                            .show()
                    } finally {
                        mActivity!!.progressDismiss()
                        //mActivity.goH3Fragment()
                        alertDialog.dismiss()
                    }
                }
            }
        }
        alertDialog.show()

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
                "${binding.rangePickerYear.value}. ${binding.rangePickerMonth.value}. ${binding.rangePickerDay.value}"
            binding.toggleTo.text =
                "${binding.rangePickerYear.value}. ${binding.rangePickerMonth.value}. ${binding.rangePickerDay.value}"
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

            if (binding.btnMonth.isChecked) {
                inTextView.text = binding.tvMonthPicker.text
            } else if (binding.btnYear.isChecked) {
                inTextView.text = binding.tvYearPicker.text
            } else if (binding.btnRange.isChecked) {
                inTextView.text = "${binding.toggleFrom.text}~${binding.toggleTo.text}"
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
        val formatter = SimpleDateFormat("yyyy. MM. dd")
        val fromDate = formatter.parse(inBinding.toggleFrom.text.toString())

        if (inBinding.toggleFrom.isChecked) {
            inBinding.toggleFrom.text =
                "${inBinding.rangePickerYear.value}. ${inBinding.rangePickerMonth.value}. ${inBinding.rangePickerDay.value}"
        } else if (inBinding.toggleTo.isChecked) {
            val toDate =
                formatter.parse("${inBinding.rangePickerYear.value}. ${inBinding.rangePickerMonth.value}. ${inBinding.rangePickerDay.value}")
            val compare = toDate.compareTo(fromDate)
            if (compare < 0) {
                Toast.makeText(requireActivity(), "FROM이 TO보다 큽니다.", Toast.LENGTH_SHORT).show()
            } else {
                inBinding.toggleTo.text =
                    "${inBinding.rangePickerYear.value}. ${inBinding.rangePickerMonth.value}. ${inBinding.rangePickerDay.value}"
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        keyboardVisibilityUtils?.detachKeyboardListeners()
        _binding = null
    }

    fun dpToPx(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        )
    }

    private fun dialogPopUpDelete(inDialog: AlertDialog, inBook: Book) {

        val bindingPopup = DialogPopupDeleteBinding.inflate(requireActivity().layoutInflater)

        val dialog = Dialog(requireActivity())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(bindingPopup.root)
        dialog.setCancelable(true)


        bindingPopup.btnClose.setOnClickListener {
            inDialog.dismiss()
            dialog.dismiss()
        }
        bindingPopup.btnIng.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                NotionAPI.deletePage(inBook.page_id)
            }

            viewModel.deleteBook(inBook)
            inDialog.dismiss()
            dialog.dismiss()
        }
        dialog.show()
    }

}