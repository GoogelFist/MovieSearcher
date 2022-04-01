package com.github.googelfist.moviesearcher.data

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.github.googelfist.moviesearcher.R
import com.github.googelfist.moviesearcher.component
import com.github.googelfist.moviesearcher.domain.Repository
import kotlinx.coroutines.delay
import javax.inject.Inject

class RefreshMainDataWork(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    @Inject
    lateinit var repository: Repository

    init {
        context.component.inject(this)
    }

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as
            NotificationManager

    override suspend fun doWork(): Result {
        return try {
            setForeground(createForegroundInfo())
            val pageCount = repository.loadPageCount()
            for (page in 1..pageCount) {
                repository.refreshLocalData(page)
                delay(LOAD_DELAY)
            }
            Result.success()
        } catch (error: RemoteLoadError) {
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    private fun createForegroundInfo(): ForegroundInfo {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel()
            } else {
                EMPTY_STRING
            }

        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(id)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(NOTIFICATION_TITLE)
            .setTicker(NOTIFICATION_TITLE)
            .setContentText(CONTENT_TEXT)
            .setSmallIcon(R.drawable.ic_baseline_update_24)
            .setOngoing(true)
            .setCategory(Notification.CATEGORY_SERVICE)
            .addAction(android.R.drawable.ic_delete, CANCEL_ACTION_TITLE, intent)
            .build()

        return ForegroundInfo(NOTIFICATION_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE
        )
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        notificationManager.createNotificationChannel(channel)
        return CHANNEL_ID
    }

    companion object {
        private const val NOTIFICATION_ID = 101
        private const val NOTIFICATION_TITLE = "Update movies data"

        private const val LOAD_DELAY = 500L

        private const val CANCEL_ACTION_TITLE = "Cancel"

        private const val CHANNEL_ID = "my_service"
        private const val CHANNEL_NAME = "Update movies service"

        private const val CONTENT_TEXT = "Starting update"

        private const val EMPTY_STRING = ""
    }
}