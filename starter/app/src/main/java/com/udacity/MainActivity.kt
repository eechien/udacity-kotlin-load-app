package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ContentMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var glideDownloadID: Long = 0
    private var projectDownloadID: Long = 0
    private var retrofitDownloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name)
        )
        val binding: ContentMainBinding = DataBindingUtil.setContentView(this, R.layout.content_main)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            download(when(binding.radioChoices.checkedRadioButtonId) {
                R.id.glide_radio -> getString(R.string.glide_url)
                R.id.project_radio -> getString(R.string.project_url)
                else -> getString(R.string.retrofit_url)
            })
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val projectDownload: String
            projectDownload = when (id) {
                glideDownloadID -> application.getString(R.string.glide_short_name)
                projectDownloadID -> application.getString(R.string.project_short_name)
                else -> application.getString(R.string.retrofit_short_name)
            }
            context?.let {
                notificationManager.sendNotification(
                    projectDownload,
                    it
                )
            }

        }
    }

    private fun download(projectUrl: String) {
        val downloadUrl = String.format("%s/archive/master.zip", projectUrl)
        val request =
            DownloadManager.Request(Uri.parse(downloadUrl))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        when(projectUrl) {
            application.getString(R.string.glide_url) -> glideDownloadID = downloadManager.enqueue(request)
            application.getString(R.string.project_url) -> projectDownloadID = downloadManager.enqueue(request)
            else -> retrofitDownloadID = downloadManager.enqueue(request)
        }
    }

    fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_LOW
            )
                .apply {
                    setShowBadge(false)
                }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.WHITE
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download complete"

            notificationManager.createNotificationChannel(notificationChannel)
        }

    }
}
