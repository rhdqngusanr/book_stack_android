package kr.book_stack

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kakao.sdk.common.util.Utility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.book_stack.databinding.ActivityMainBinding
import kr.book_stack.fragment.MainFragment
import kr.book_stack.fragment.SearchFragment
import kr.book_stack.fragment.SettingFragment
import org.jraf.klibnotion.client.Authentication


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mAdapter: RecyclerViewAdapter? = null
    private val authentication = Authentication()
    private var searchFragment: SearchFragment = SearchFragment().newInstance()
    private var settingFragment: SettingFragment = SettingFragment().newInstance()
    private var mainFragment: MainFragment = MainFragment().newInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authentication.accessToken = Struct.token

        Log.d("test MainActivity", "keyhash : ${Utility.getKeyHash(this)}")

        CoroutineScope(Dispatchers.Main).launch {
/*            val data = NotionAPI.queryBookDatabaseFilters(Struct.dbIdBook,"email","test2")
            //Log.i("CoroutineScope", "NotionAPI query Book : $data")
            //createPageInDatabase(Struct.dbIdBook)
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("DB가져오기")
            if(data.size > 0){
                builder.setMessage("$data")
            }else{
                builder.setMessage("데이타 없음")
            }

            builder.setCancelable(false)
            builder.setPositiveButton("닫기") { _, _ ->
            }

            val alertDialog = builder.create()
            alertDialog.show()*/
            //NotionAPI.createPageInPageWithContent()
        }


        //notionCreateJson()
        binding.bnvMain.setOnItemSelectedListener { item ->
            changeFragment(
                when (item.itemId) {
                    R.id.nevi_home -> {
                        searchFragment
                        // Respond to navigation item 1 click
                    }
                    R.id.nevi_setting -> {
                        settingFragment
                        // Respond to navigation item 2 click
                    }
                    else -> {
                        mainFragment
                    }
                }
            )
            true
        }
        binding.bnvMain.selectedItemId = R.id.nevi_home


    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.linearLayout, fragment)
            .commit()
    }

}




