package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.udacity.databinding.ContentDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelNotification()

        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val binding: ContentDetailBinding = DataBindingUtil.setContentView(
            this, R.layout.content_detail
        )
        binding.fileName = intent.getStringExtra("fileName")
        binding.status = intent.getStringExtra("status")
        binding.leaveDetailPageButton.setOnClickListener {
            binding.detailActivityContent.transitionToEnd()
            val contentIntent = Intent(applicationContext, MainActivity::class.java)
            navigateUpTo(contentIntent)
        }

    }

}
