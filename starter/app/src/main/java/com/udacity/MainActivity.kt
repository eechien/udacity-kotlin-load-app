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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
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
            custom_button.buttonState = ButtonState.Clicked
            when(binding.radioChoices.checkedRadioButtonId) {
                R.id.glide_radio -> download(Project.getGlide())
                R.id.project_radio -> download(Project.getLoadApp())
                R.id.retrofit_radio -> download(Project.getRetrofit())
                else -> Toast.makeText(applicationContext, "Please select the file to download", Toast.LENGTH_LONG).show()
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent!!.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val query = DownloadManager.Query()
                .setFilterById(id)
            val cursor = downloadManager.query(query)
            var downloadStatus = ""
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(
                    cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                )
                when (status) {
                    DownloadManager.STATUS_SUCCESSFUL -> downloadStatus = "Success"
                    DownloadManager.STATUS_FAILED -> downloadStatus = "Fail"
                }
                val project = when (id) {
                    glideDownloadID -> Project.getGlide()
                    projectDownloadID -> Project.getLoadApp()
                    else -> Project.getRetrofit()
                }
                custom_button.buttonState = ButtonState.Completed
                context?.let {
                    notificationManager.sendNotification(
                        project,
                        downloadStatus,
                        it
                    )
                }
            }
        }
    }

    private fun download(project: Project) {
        val downloadUrl = String.format("%s/archive/master.zip", project.url)
        val request =
            DownloadManager.Request(Uri.parse(downloadUrl))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        when(project.url) { // FIXME how to do less duplication?
            application.getString(R.string.glide_url) -> {
                glideDownloadID = downloadManager.enqueue(request)
            }
            application.getString(R.string.project_url) -> {
                projectDownloadID = downloadManager.enqueue(request)
            }
            else -> {
                retrofitDownloadID = downloadManager.enqueue(request)
            }
        }
        custom_button.buttonState = ButtonState.Loading
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
