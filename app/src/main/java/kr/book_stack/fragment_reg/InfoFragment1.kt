package kr.book_stack.fragment_reg

import KeyboardVisibilityUtils
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import kr.book_stack.AppViewModel
import kr.book_stack.R
import kr.book_stack.RegActivity
import kr.book_stack.Struct
import kr.book_stack.appDB.data.Book
import kr.book_stack.appDB.data.User
import kr.book_stack.databinding.RegFragmentInfo1Binding
import kr.book_stack.notion.NotionAPI
import org.jraf.klibnotion.model.exceptions.NotionClientRequestException
import java.lang.NumberFormatException


class InfoFragment1 : Fragment() {
    private lateinit var callback: OnBackPressedCallback

    private var _binding: RegFragmentInfo1Binding? = null;
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by viewModels()
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils
    fun newInstance(): InfoFragment1 {
        return InfoFragment1()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegFragmentInfo1Binding.inflate(inflater, container, false)

        return binding.root

    }

    interface OnRegInfoListener {
        fun onFragmentAgreeOkClick()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString("user_id")
        val name = arguments?.getString("user_name")
        val profile = arguments?.getString("user_profile")
        val mActivity = activity as RegActivity
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {

                        mActivity.regFinish()
                        true
                    }

                    else -> false
                }

            }
        }, viewLifecycleOwner, Lifecycle.State.CREATED)
        binding.editInfoName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {


                binding.tvInfoConfirm.isEnabled = s.toString().isNotEmpty()
            }

        })

        if (profile == "null") {
            Glide
                .with(binding.circleImageView.context)
                .load(R.drawable.login_1)
                .circleCrop()
                .into(binding.circleImageView)
        } else {
            Glide
                .with(binding.circleImageView.context)
                .load(profile)
                .circleCrop()
                .into(binding.circleImageView)

        }

        var firstClick = false
        keyboardVisibilityUtils = KeyboardVisibilityUtils(requireActivity().window,
            onShowKeyboard = {
                binding.layConfirm.setPadding(0, 0, 0, 0)
                binding.tvInfoConfirm.setBackgroundResource(R.drawable.enable_btn_full)
                binding.editInfoName.hint = "이름을 입력해주세요."
                binding.tilInfoName.isCounterEnabled = true
                binding.tilInfoName.isEndIconVisible = true
                if (!firstClick){
                    firstClick=true
                    binding.tilInfoName.requestFocus()
                    binding.tilInfoName.editText!!.setText(name)
                    var len = 0
                    len = if (name!!.length>8){
                        8
                    }else{
                        name.length
                    }
                    binding.tilInfoName.editText!!.setSelection(len)
                }

                binding.editInfoName.isFocusable = true
                binding.editInfoName.setTextColor(ContextCompat.getColor(requireActivity(),R.color.gray15))
            }, onHideKeyboard = {
                binding.layConfirm.setPadding(16, 16, 16, 16)
                binding.tvInfoConfirm.setBackgroundResource(R.drawable.enable_btn)
                binding.editInfoName.hint = ""
                binding.tilInfoName.isCounterEnabled = false
                binding.tilInfoName.isEndIconVisible = false
                binding.editInfoName.clearFocus()

                binding.editInfoName.setTextColor(ContextCompat.getColor(requireActivity(),R.color.gray60))
            })
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

       // binding.tilInfoName.requestFocus()
        binding.tilInfoName.editText!!.setText(name)
       // binding.tilInfoName.clearFocus()

        binding.editInfoName.setOnFocusChangeListener { _, p1 ->
            if (p1) {
                imm.showSoftInput(binding.editInfoName, 0)
            }
        }
        binding.layBackgroundInfo1.setOnClickListener {
            imm.hideSoftInputFromWindow(binding.editInfoName.windowToken, 0)
        }


        binding.tvInfoConfirm.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {
                mActivity.progressView(15000)
                val userData = NotionAPI.queryUserID("id", id.toString())
                try {

                    if (userData == null) {
                        return@launch
                    }
                    val testt = 1
                    if (userData.id == "empty") {
                        val userDbPageId = NotionAPI.createUserDB(
                            id.toString(),
                            binding.editInfoName.text.toString(),
                            profile.toString()
                        )

                        val bookDbPageId = NotionAPI.createBookDatabase(id.toString())
                        val tagDbPageId = NotionAPI.createTagDatabase(id.toString())
                        NotionAPI.updateUserPageId(userDbPageId, bookDbPageId, tagDbPageId)
                        viewModel.insertUser(
                            User(
                                id.toString(),
                                binding.editInfoName.text.toString(),
                                profile.toString(),
                                userDbPageId,
                                bookDbPageId,
                                tagDbPageId
                            )
                        )
                        Struct.loginId = id.toString()
                        mActivity.goFragment(TagFragment2(), null)
                        //mActivity.goTagFragment()
                    } else {
                        Struct.loginId = id.toString()
                        viewModel.insertUser(
                            User(
                                userData.id.toString(),
                                userData.name.toString(),
                                userData.profileImg.toString(),
                                userData.user_page_id,
                                userData.book_page_id,
                                userData.tag_page_id
                            )
                        )
                        Toast.makeText(requireActivity(), "QA1. 가입완료상태.", Toast.LENGTH_SHORT).show()
                        mActivity.goFragment(TagFragment2(),null)
                    }
                } catch (e: Exception) {

                    Log.e("InfoFragment1", "$e")
                    Toast.makeText(requireActivity(), "가입 API 오류.", Toast.LENGTH_LONG).show()
                } finally {
                    mActivity.progressDismiss()
                }

            }

        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        keyboardVisibilityUtils.detachKeyboardListeners()
    }
}