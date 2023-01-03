package kr.book_stack

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import kr.book_stack.appDB.data.Tag

class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_API)
    }
}