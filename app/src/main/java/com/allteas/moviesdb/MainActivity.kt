package com.allteas.moviesdb

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.allteas.moviesdb.databinding.ActivityMainBinding
import com.allteas.moviesdb.model.MovieDBClient
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val moviesAdapter = MoviesAdapter(emptyList()) {
            Toast.makeText(this@MainActivity, it.title, Toast.LENGTH_SHORT).show()

        }
        binding.recycler.adapter = moviesAdapter



        thread {
            val apiKey = this.getString(R.string.api_key)
            val popularMovies = MovieDBClient.service.listPopularMovies(apiKey)
            val body = popularMovies.execute().body()

            runOnUiThread{
                if(body != null) {
                    moviesAdapter.movies = body.results
                    moviesAdapter.notifyDataSetChanged()
                }
            }

        }
    }
}