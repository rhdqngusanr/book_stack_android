package kr.book_stack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import kr.book_stack.adapter.LoginViewPagerAdapter
import kr.book_stack.adapter.TutorialViewPagerAdapter

import kr.book_stack.databinding.ActivityTutoBinding

class TutorialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTutoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarTutorial)

        binding.viewPagerTutorial.adapter = TutorialViewPagerAdapter(getPageList()) // 어댑터 생성
        binding.viewPagerTutorial.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로


        binding.btnTutorial.setOnClickListener {
            if (binding.viewPagerTutorial.currentItem == 0){
                binding.viewPagerTutorial.currentItem = 1
                binding.btnTutorial.text ="다음"
            }else if (binding.viewPagerTutorial.currentItem == 1){
                binding.viewPagerTutorial.currentItem = 2
                binding.btnTutorial.text ="시작하기"
            }
        }

    }

    private fun getPageList(): ArrayList<String> {
        return arrayListOf<String>("tuto1", "tuto2", "tuto3")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tuto, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_tuto_skip -> {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)

            }
        }
        return super.onOptionsItemSelected(item)
    }
}