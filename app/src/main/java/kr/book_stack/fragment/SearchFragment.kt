package kr.book_stack.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kr.book_stack.*
import kr.book_stack.adapter.RecyclerViewAdapter
import kr.book_stack.api.ApiData
import kr.book_stack.api.ApiInterface
import kr.book_stack.databinding.DialogAddSearchBinding
import kr.book_stack.databinding.DialogPopupBinding
import kr.book_stack.databinding.FragmentSearchBinding
import kr.book_stack.fragment_reg.HFragment3
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null;
    private val binding get() = _binding!!
    private var mAdapter: RecyclerViewAdapter? = null
    var mActivity : RegActivity? = null
    var bookInfo : StructData.BookInfo? = null;

    fun newInstance() : SearchFragment {
        return SearchFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mActivity = activity as RegActivity
        binding.searchView.isIconifiedByDefault = false;
        binding.searchView.requestFocus();

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        mActivity!!.onBackPressed()
                        //mActivity.goFragment(InfoFragment1(),null)
                        true
                    }

                    else -> false
                }

            }
        }, viewLifecycleOwner, Lifecycle.State.CREATED)

        binding.searchView.findFocus().setOnFocusChangeListener { _, p1 ->
            if (p1){
                imm.showSoftInput(binding.searchView.findFocus(), 0)
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    apiAlaBookSearch(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    //apiAlaBookSearch(newText)
                }
                return false
            }

        })


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun apiAlaBookSearch(Query: String) {
        val apiInterface = ApiInterface.create().getAlaBookInfo(
            Struct.ttbKey,
            Query,
            "20131101",
            "Big"
        )

        apiInterface.enqueue(object : Callback<ApiData.BookAlaInfo> {
            override fun onResponse(
                call: Call<ApiData.BookAlaInfo>,
                responseData: Response<ApiData.BookAlaInfo>
            ) {
                Log.i("test", "createAladinApi onResponse code : ${responseData.code()}")
                responseData.let {

                    when (responseData.code()) {
                        200 -> {
                            val items = responseData.body()!!.item
/*
                            if (responseData.body()!!.totalResults == 0){
                                binding.layNoSearch.visibility = View.VISIBLE
                                binding.recyclerView.visibility = View.GONE
                            }else{*/
                                binding.layNoSearch.visibility = View.GONE
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.recyclerView.layoutManager = LinearLayoutManager(
                                    requireActivity(),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                                mAdapter = RecyclerViewAdapter(items)
                                binding.recyclerView.adapter = mAdapter


                                mAdapter?.itemClick = object : RecyclerViewAdapter.ItemClick {
                                    override fun onClick(view: View, position: Int) {
                                        apiAlaBookDetail(items[position]!!.isbn, items[position]!!)

                                    }
                                }



                        }
                    }
                }
            }

            override fun onFailure(call: Call<ApiData.BookAlaInfo>, t: Throwable) {
                Log.i("test", "apiAlaBookSearch onFailure$t")
                binding.layNoSearch.visibility = View.VISIBLE
                binding.tvNo.text = "‘$Query’에 대한 검색 결과가 없습니다."
                binding.recyclerView.visibility = View.GONE
            }


        })
    }

    private fun apiAlaBookDetail(ItemId: String, Item :ApiData.Item) {
        val apiInterface = ApiInterface.create().getAlaDetailBookInfo(
            Struct.ttbKey,
            ItemId,
            "20131101"
        )

        apiInterface.enqueue(object : Callback<ApiData.BookAlaDetailInfo> {
            override fun onResponse(
                call: Call<ApiData.BookAlaDetailInfo>,
                responseData: Response<ApiData.BookAlaDetailInfo>
            ) {
                Log.i("test", "createAladinApi onResponse code : ${responseData.code()}")
                responseData.let {

                    when (responseData.code()) {
                        200 -> {
                            val itemPage = responseData.body()!!.ItemDetail[0].BookInfo[0].itemPage
                            dialogAddHighlight(Item)
                            bookInfo = StructData.BookInfo(Item.isbn,"3",itemPage.toString(),"0","bb","bb")

                        }
                    }
                }
            }

            override fun onFailure(call: Call<ApiData.BookAlaDetailInfo>, t: Throwable) {
                Log.i("test", "apiAlaBookSearch onFailure" + t)
            }


        })
    }


    @SuppressLint("SetTextI18n")
    private fun dialogAddHighlight(Item :ApiData.Item) {
        //val view = View.inflate(this@ActivityAdminMain, R.layout.dialog_add_customer, null)
        val bindingDialog = DialogAddSearchBinding.inflate(requireActivity().layoutInflater)
        val builder = AlertDialog.Builder(requireActivity(), R.style.popCasterDlgTheme)
        builder.setView(bindingDialog.root)
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.window!!.attributes.windowAnimations = R.style.AnimationPopupStyle
        alertDialog.setCancelable(true)

        alertDialog.show()

        Glide
            .with(bindingDialog.imgBookCover.context)
            .load(Item.cover)
            .into(bindingDialog.imgBookCover)


        val formatter = SimpleDateFormat("yyyy-mm-dd", Locale.US)
        val date = formatter.parse(Item.pubdate)
        val formatter2 = SimpleDateFormat("yyyy년",Locale.getDefault())
        val str: String = formatter2.format(date)
        var description = ""
        description = if (Item.description == null){
            "책에 대한 정보가 없습니다"
        }else{
            Html.fromHtml(Item.description, Html.FROM_HTML_MODE_LEGACY).toString()
        }

        val bookInfoStr =  "${Item.author} · ${Item.publisher} · $str"

        bindingDialog.tvBookName.text = Item.title
        bindingDialog.tvBookAuthor.text = Item.author
        bindingDialog.tvBookPublisher.text = Item.publisher


        bindingDialog.tvBookDescription.text = description

        bindingDialog.imgClose.setOnClickListener {
            alertDialog.dismiss()
        }
        bindingDialog.tvBookInfoClose.setOnClickListener {
            dialogPopUp(mActivity!!)
        }
        bindingDialog.btnSearchAdd.setOnClickListener {


            val bundle = Bundle()
            bundle.putString("bookInfo", bookInfoStr)
            bundle.putString("bookName",bindingDialog.tvBookName.text.toString())
            bundle.putString("bookDes",bindingDialog.tvBookDescription.text.toString())
            bundle.putString("bookCover",Item.cover)
            bundle.putString("bookAuthor",Item.author)
            bundle.putSerializable("bookStatus", bookInfo!!)
            mActivity?.goFragment(HighLightFragment(),bundle)

            Handler(Looper.getMainLooper()).postDelayed({
                alertDialog.dismiss()
            }, 500)

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

}