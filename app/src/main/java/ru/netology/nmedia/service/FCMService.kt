package ru.netology.nmedia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AppAuth
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {
    private val content = "content"
    private val channelId = "remote"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val push = gson.fromJson(message.data[content], Push::class.java)
        val myId = AppAuth.getInstance().authStateFlow.value.id

        when (push.recipientId) {
            myId, null -> {
                sendNotification(push)
            }
            else -> AppAuth.getInstance().sendPushToken()
        }
    }

    override fun onNewToken(token: String) {
        AppAuth.getInstance().sendPushToken(token)
    }

    private fun sendNotification(push: Push) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_login,
                    push.content
                )
            )
            .setStyle(NotificationCompat.BigTextStyle().bigText(push.recipientId.toString()))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this).notify(Random.nextInt(100_000), notification)
    }
}

data class Push(
    val content: String,
    val recipientId: Long?
)

//Реализуйте на клиентской стороне при получении push-сообщения проверку recipientId (сервер будет присылать вам его в Push'е).
//
//Для этого сравнивайте полученный recipientId с тем, что хранится у вас в AppAuth, и выполняйте одно из следующих действий:
//
//если recipientId = тому, что в AppAuth, то всё ok, показываете Notification;
//если recipientId = 0 (и не равен вашему), значит сервер считает, что у вас анонимная аутентификация и вам нужно переотправить свой push token;
//если recipientId != 0 (и не равен вашему), значит сервер считает, что на вашем устройстве другая аутентификация и вам нужно переотправить свой push token;
//если recipientId = null, то это массовая рассылка, показываете Notification.
