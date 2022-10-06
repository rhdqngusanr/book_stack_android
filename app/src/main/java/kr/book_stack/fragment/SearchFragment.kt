package kr.book_stack.fragment

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.core.text.parseAsHtml
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
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
        mActivity = activity as RegActivity
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    apiAlaBookSearch(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    apiAlaBookSearch(newText)
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
            Query
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
                            binding.recyclerView.layoutManager = LinearLayoutManager(
                                requireActivity(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                            mAdapter = RecyclerViewAdapter(items)
                            binding.recyclerView.adapter = mAdapter
                            binding.recyclerView.addItemDecoration(
                                DividerItemDecoration(
                                    requireActivity(),
                                    DividerItemDecoration.VERTICAL
                                )
                            )
                            mAdapter?.itemClick = object : RecyclerViewAdapter.ItemClick {
                                override fun onClick(view: View, position: Int) {
                                    apiAlaBookDetail(items[position].isbn,items[position])
                                }
                            }

                        }
                    }
                }
            }

            override fun onFailure(call: Call<ApiData.BookAlaInfo>, t: Throwable) {
                Log.i("test", "apiAlaBookSearch onFailure" + t)
            }


        })
    }

    private fun apiAlaBookDetail(ItemId: String, Item :ApiData.Item) {
        val apiInterface = ApiInterface.create().getAlaDetailBookInfo(
            Struct.ttbKey,
            ItemId
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

                            dialogAddHighlight(Item)
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
        alertDialog.setCancelable(true)
        alertDialog.show()

        Glide
            .with(binding.imgBookCover.context)
            .load(Item.cover)
            .into(binding.imgBookCover)


        val formatter = SimpleDateFormat("E, dd MMMM yyyy HH:mm:ss X", Locale.US)
        val date = formatter.parse(Item.pubdate)
        val formatter2 = SimpleDateFormat("yyyy년",Locale.getDefault())
        val str: String = formatter2.format(date)
        val description = Item.description.split("<br/>");
        binding.tvBookInfo.text = "${Item.author} · ${Item.publisher} · $str"
        binding.tvBookName.text = Item.title


        binding.tvBookDescription.text = description[1].parseAsHtml().toString()

        binding.imgClose.setOnClickListener {
            MyUtil.dialogCloseTypeView(requireActivity(),"종료","종료함?")
        }
        binding.btnSearchAdd.setOnClickListener {
            alertDialog.dismiss()
            val bundle = Bundle()
            bundle.putString("bookInfo", binding.tvBookInfo.text.toString())
            bundle.putString("bookName",binding.tvBookName.text.toString())
            bundle.putString("bookDes",binding.tvBookDescription.text.toString())
            bundle.putString("bookCover",Item.cover)
            mActivity?.goFragment(HighLightFragment(),bundle)
        }


    }

}