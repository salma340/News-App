package com.example.newsapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.newsapp.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadNews()
        binding.swipeRefresh.setOnRefreshListener { loadNews() }
    }
    private fun loadNews()
    {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit

            .Builder()
            .baseUrl("https://newsapi.org")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val c = retrofit.create(NewsCallable::class.java)
        c.getNews().enqueue(object : Callback<News>{
            override fun onResponse(
                p0: Call<News?>,
                p1: Response<News?>
            ) {
                val news=  p1.body()
                val articles=news?.articles
                Log.d("trace","Articles: $articles")
                if (articles != null) {
                    val adapter = NewsAdapter(this@MainActivity, ArrayList(articles))
                    binding.newsList.adapter = adapter
                    binding.progress.visibility = View.GONE
                }
                binding.swipeRefresh.isRefreshing=false
                binding.progress.isVisible=false

            }

            override fun onFailure(
                p0: Call<News?>,
                p1: Throwable
            ) {
                Log.d("trace","Error: ${p1.message}")
                binding.swipeRefresh.isRefreshing=false
                binding.progress.isVisible=false

            }

        } )

    }
}