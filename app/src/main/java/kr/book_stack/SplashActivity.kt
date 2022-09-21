package kr.book_stack

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

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
    }
}