package kr.book_stack.fragment


import KeyboardVisibilityUtils
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import androidx.lifecycle.Observer
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.book_stack.*

import kr.book_stack.adapter.TagMakeViewPagerAdapter
import kr.book_stack.appDB.data.Book
import kr.book_stack.appDB.data.DefaultTag
import kr.book_stack.appDB.data.User
import kr.book_stack.databinding.DialogDatepickerBinding
import kr.book_stack.databinding.DialogPopupBinding
import kr.book_stack.databinding.DialogTagMakeBinding

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
        val animation: Animation = AlphaAnimation(0f, 1f)
        animation.duration = 1000
        val mActivity = activity as RegActivity

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_reg_close, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_close -> {

                        dialogPopUp(mActivity)
                        true
                    }
                    android.R.id.home -> {
                        mActivity.onBackPressed()
                        //mActivity.goFragment(InfoFragment1(),null)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.CREATED)



        val info = arguments?.getString("bookInfo")
        val title = arguments?.getString("bookName")
        val des = arguments?.getString("bookDes")
        val author = arguments?.getString("bookAuthor")
        val cover = arguments?.getString("bookCover")
        val status: StructData.BookInfo =
            arguments?.getSerializable("bookStatus") as StructData.BookInfo


        Glide
            .with(binding.hAddImgBookCover.context)
            .load(cover)
            .into(binding.hAddImgBookCover)
        binding.hAddTvBookTitle.text = title
        binding.hAddTvBookInfo.text = info

        val stringsTag = resources.getStringArray(R.array.tag_name)
        val stringsTagImg = resources.getStringArray(R.array.tag_make_img_name)
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


                            for (j in stringsTagImg.indices) {
                                if (stringsTagImg[j] == tag[i].tagImg) {
                                    chipIcon = ContextCompat.getDrawable(
                                        requireActivity(),
                                        images.getResourceId(j, -1)
                                    )
                                }
                            }
                            /*isCheckable = true
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
                                }*/
                        })
                    }
                }
            }
        })

        val strDateRange =
            "${cal.get(Calendar.YEAR)}. ${cal.get(Calendar.MONTH)}. ${cal.get(Calendar.DATE)}"
        binding.hAddTvRange.text = "$strDateRange ~ $strDateRange"

        binding.btnDateRange.setOnClickListener {
            dialogDate(binding.hAddTvRange)
        }
        binding.tilInfoComent.setOnClickListener {
            binding.scrollHighlight.fullScroll(ScrollView.FOCUS_UP)
        }

        var userInfo: User? = null
        viewModel.getUser(Struct.loginId).observe(viewLifecycleOwner, Observer { user ->
            user?.let { userInfo = user }

        })
        binding.tvBtnAddHighlight.setOnClickListener {

            var tagString = ""
            var tagImgString = ""
            CoroutineScope(Dispatchers.Main).launch {

                val ids = binding.chipsHighLightTagCheck.checkedChipIds
                if (ids.size == 0) {
                    Toast.makeText(requireActivity(), "태그를 선택해주세요.", Toast.LENGTH_LONG).show()
                } else {
                    mActivity.progressView(15000)
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
                        val bookDbPageId = NotionAPI.createBookPage(
                            Struct.loginId,
                            userInfo!!.bookPageId.toString(),
                            title.toString(),
                            author.toString(),
                            status.inIsbn,
                            status.inBookStatus,
                            status.inBookPage,
                            status.inLookPage,
                            arr[0],
                            arr[1],
                            cover.toString(),
                            tagString,
                            tagImgString,
                            "3",
                            binding.editInfoComent.text.toString(),
                            info.toString()

                        )
                        NotionAPI.updateBookPageId(bookDbPageId)
                        //TODO 추후 룸 DB 연결부분 협의후 수정
                        viewModel.insert(
                            Book(
                                Struct.loginId,
                                bookDbPageId,
                                title.toString(),
                                author.toString(),
                                status.inIsbn,
                                status.inBookStatus,
                                status.inBookPage,
                                status.inLookPage,
                                arr[0],
                                arr[1],
                                cover.toString(),
                                tagString,
                                tagImgString,
                                "3",
                                binding.editInfoComent.text.toString(),
                                info
                            )
                        )
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
            }, onHideKeyboard = {

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

    private fun dialogPopUp(inActivity: RegActivity) {

        val bindingPopup = DialogPopupBinding.inflate(requireActivity().layoutInflater)

        val dialog = Dialog(requireActivity())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(bindingPopup.root)
        dialog.setCancelable(true)


        bindingPopup.btnClose.setOnClickListener {
            inActivity.goFragment(HFragment3(), null)
            dialog.dismiss()
        }
        bindingPopup.btnIng.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        keyboardVisibilityUtils.detachKeyboardListeners()
        _binding = null
    }

    fun dpToPx(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        )
    }

    fun scrollDown() {
        binding.scrollHighlight.post(Runnable {
            binding.scrollHighlight.fullScroll(ScrollView.FOCUS_DOWN)
        })

    }

}