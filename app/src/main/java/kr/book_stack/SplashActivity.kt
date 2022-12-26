package kr.book_stack

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import kr.book_stack.adapter.LoginViewPagerAdapter
import kr.book_stack.databinding.ActivityLoginPageBinding
import kr.book_stack.databinding.ActivitySplashBinding


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPagerLogin.adapter = LoginViewPagerAdapter(getPageBigList(),getPageSmallList()) // 어댑터 생성
        binding.viewPagerLogin.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로
        binding.springDotsIndicator.attachTo(binding.viewPagerLogin)


        val pref = getSharedPreferences("isFirst", MODE_PRIVATE)
        val first = pref.getBoolean("isFirst", false)
        if (!first) {

            val editor = pref.edit()
            editor.putBoolean("isFirst", true)
            editor.apply()
            //앱 최초 실행시 하고 싶은 작업
        } else {
            Log.d("Is first Time?", "not first")
        }

        binding.tvSplashStart.setOnClickListener {
            //val intent = Intent(this,RegActivity::class.java)
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }


    }

    private fun getPageBigList(): ArrayList<String> {
        return arrayListOf<String>("독서로 한단계 성장하기", "분야별 독서 관리",
            "독서 포트폴리오 공유")
    }
    private fun getPageSmallList(): ArrayList<String> {
        return arrayListOf<String>("나의 독서 커리어 관리, 북스택", "독서 태그로 관심 분야를 알릴 수 있어요.",
            "링크로 간편하게 나의 역량을 알려보세요.")
    }
}