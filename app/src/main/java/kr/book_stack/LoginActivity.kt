package kr.book_stack

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import kr.book_stack.adapter.LoginViewPagerAdapter


import kr.book_stack.databinding.ActivityLoginPageBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("test", "1 = ${R.drawable.ic_baseline_home_32} , 2 = ${R.drawable.test_svg}")
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


        binding.viewPagerLogin.adapter = LoginViewPagerAdapter(getPageList()) // 어댑터 생성
        binding.viewPagerLogin.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로
        binding.springDotsIndicator.attachTo(binding.viewPagerLogin)



        Log.d("test MainActivity", "keyhash : ${Utility.getKeyHash(this)}")

       val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient : GoogleSignInClient = this.let { GoogleSignIn.getClient(it, gso) }

        binding.googleLoginBtn.setOnClickListener {
            signIn(mGoogleSignInClient)
        }


        KakaoSdk.init(this, BuildConfig.KAKAO_API)
        binding.btnKakao.setOnClickListener {
/*            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)*/
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                    kakaoLogin()
                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                        kakaoLogin()
                    }
                }
            } else {
                Log.e(TAG, "카카오계정으로 ELSE")
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    private fun getPageList(): ArrayList<String> {
        return arrayListOf<String>("hello!\nit’s a Splash page.1", "hello!\nit’s a Splash page.2", "hello!\nit’s a Splash page.3")
    }

    private fun signIn(inClient : GoogleSignInClient){
        val signInIntent = inClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.i("resultLauncher", "result $result")
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>){
        try {

            val account = completedTask.getResult(ApiException::class.java)

            Log.i("handleSignInResult", "test ${account.displayName}")
            val email = account?.email.toString()
            val familyName = account?.familyName.toString()
        } catch (e: ApiException){
            Log.w("failed", "signInResult:failed code=" + e.statusCode)
        }
    }


    private fun kakaoLogin(){
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패 $error")
            } else if (user != null) {
                Log.e(TAG, "사용자 정보 요청 성공 : ${user.id} ,${user.properties}")
                val intent = Intent(this,RegActivity::class.java)
                intent.putExtra("id","${user.id}")
                intent.putExtra("name",user.kakaoAccount?.profile?.nickname.toString())
                intent.putExtra("profile",user.kakaoAccount?.profile?.profileImageUrl)
                startActivity(intent)
            }
        }
    }
}