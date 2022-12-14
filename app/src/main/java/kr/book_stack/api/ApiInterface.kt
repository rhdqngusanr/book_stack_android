package kr.book_stack.api

import com.google.gson.GsonBuilder



import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
//import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.*


interface ApiInterface {
    @GET("/v1/search/book.json")
    fun getBookInfo(
        @Query("query") query: String
    ): Call<ApiData.BookInfoAll>

    @GET("/ttb/api/ItemSearch.aspx")
    fun getAlaBookInfo(
        @Query("ttbkey") ttbkey: String,
        @Query("query") query: String,
        @Query("Version") Version: String,
        @Query("Cover") Cover: String
    ): Call<ApiData.BookAlaInfo>

    @GET("/ttb/api/ItemLookUp.aspx")
    fun getAlaDetailBookInfo(
        @Query("ttbkey") ttbkey: String,
        @Query("ItemId") ItemId: String,
        @Query("Version") Version: String
    ): Call<ApiData.BookAlaDetailInfo>


    @Headers("Content-Type: application/json")
    @POST("/v1/pages")
    fun postNotionCreatePage(
        @Header("Authorization") token: String,
        @Header("Notion-Version") version: String,
        @Body body :String
    ): Call<ApiData.NotionCreateJson>
    companion object {
        var BASE_URL = "https://openapi.naver.com"
        var BASE_URL2 = "http://www.aladin.co.kr"
        var BASE_URL3 = "https://api.notion.com"
        var token = ""
        fun create() : ApiInterface {

            val gson = GsonBuilder()
                .setLenient()
                .create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor) //토큰 x
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL2)
                .client(client)

                //.addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()


            return retrofit.create(ApiInterface::class.java)
        }
        fun createNotion() : ApiInterface {

            val gson = GsonBuilder()
                .setLenient()
                .create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor) //토큰 x
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL3)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiInterface::class.java)
        }
        fun createNaverApi() : ApiInterface {

            val gson = GsonBuilder()
                .setLenient()
                .create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(NaverOpenApi()) //저장했던 Token 사용
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(ApiInterface::class.java)
        }

        private class NaverOpenApi : Interceptor {

            override fun intercept(chain: Interceptor.Chain): Response {
                val newRequest = chain.request().newBuilder()
                    .addHeader("X-Naver-Client-Id", "Cy_vVnm7vhzwWp1LL8wh")
                    .addHeader("X-Naver-Client-Secret", "dYH0BlOh9G")
                    .build()


                return chain.proceed(newRequest)
            }
        }
    }


}