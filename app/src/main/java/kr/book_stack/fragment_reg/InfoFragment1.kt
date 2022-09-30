package kr.book_stack.fragment_reg

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.book_stack.AppViewModel
import kr.book_stack.RegActivity
import kr.book_stack.Struct
import kr.book_stack.appDB.data.Book
import kr.book_stack.appDB.data.User
import kr.book_stack.databinding.RegFragmentInfo1Binding
import kr.book_stack.notion.NotionAPI
import java.lang.NumberFormatException


class InfoFragment1 : Fragment() {
    private var _binding: RegFragmentInfo1Binding? = null;
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by viewModels()
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
        binding.editInfoName.setText(name)
        binding.editInfoName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                binding.tvInfoConfirm.isEnabled =
                    !(s.toString().length > 10 || s.toString().isEmpty())
            }

        })

        binding.tvInfoConfirm.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                mActivity.progressView(15000)
                try {
                    val userData = NotionAPI.queryUserID("id", id.toString())
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
                        mActivity.goFragment(TagFragment2(),null)
                    }
                }catch (e : Exception){
                    Log.e("InfoFragment1", "$e")
                    Toast.makeText(requireActivity(), "가입 API 오류.", Toast.LENGTH_LONG).show()
                }finally {
                    mActivity.progressDismiss()
            }

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}