package kr.book_stack.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kr.book_stack.*
import kr.book_stack.adapter.RecyclerViewAdapter
import kr.book_stack.api.ApiData
import kr.book_stack.api.ApiInterface
import kr.book_stack.databinding.DialogAddSearchBinding
import kr.book_stack.databinding.FragmentSearchBinding
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

                            if (responseData.body()!!.totalResults == 0){
                                binding.layNoSearch.visibility = View.VISIBLE
                                binding.recyclerView.visibility = View.GONE
                            }else{
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
        val binding = DialogAddSearchBinding.inflate(requireActivity().layoutInflater)
        val builder = AlertDialog.Builder(requireActivity(), R.style.popCasterDlgTheme)
        builder.setView(binding.root)
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.window!!.attributes.windowAnimations = R.style.AnimationPopupStyle
        alertDialog.setCancelable(true)

        alertDialog.show()

        Glide
            .with(binding.imgBookCover.context)
            .load(Item.cover)
            .into(binding.imgBookCover)


        val formatter = SimpleDateFormat("yyyy-mm-dd", Locale.US)
        val date = formatter.parse(Item.pubdate)
        val formatter2 = SimpleDateFormat("yyyy년",Locale.getDefault())
        val str: String = formatter2.format(date)
        val description = Item.description
        val bookInfoStr =  "${Item.author} · ${Item.publisher} · $str"

        binding.tvBookName.text = Item.title
        binding.tvBookAuthor.text = Item.author
        binding.tvBookPublisher.text = Item.publisher


        binding.tvBookDescription.text = description

        binding.imgClose.setOnClickListener {
            MyUtil.dialogCloseTypeView(requireActivity(),"종료","종료함?")
        }
        binding.tvBookInfoClose.setOnClickListener {
            MyUtil.dialogCloseTypeView(requireActivity(),"종료","종료함?")
        }
        binding.btnSearchAdd.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("bookInfo", bookInfoStr)
            bundle.putString("bookName",binding.tvBookName.text.toString())
            bundle.putString("bookDes",binding.tvBookDescription.text.toString())
            bundle.putString("bookCover",Item.cover)
            bundle.putSerializable("bookStatus", bookInfo!!)
            mActivity?.goFragment(HighLightFragment(),bundle)

            Handler(Looper.getMainLooper()).postDelayed({
                alertDialog.dismiss()
            }, 500)

        }


    }

}