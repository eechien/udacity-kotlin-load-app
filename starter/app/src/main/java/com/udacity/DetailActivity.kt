package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.udacity.databinding.ContentDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val binding: ContentDetailBinding = DataBindingUtil.setContentView(
            this, R.layout.content_detail
        )
        binding.fileName = intent.getStringExtra("fileName")
        binding.status = intent.getStringExtra("status")
        binding.leaveDetailPageButton.setOnClickListener {
            val contentIntent = Intent(applicationContext, MainActivity::class.java)
            navigateUpTo(contentIntent)
        }

    }

}
