package com.ssessments.newsapp.myfirebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ssessments.newsapp.MainActivity
import com.ssessments.newsapp.R
import com.ssessments.newsapp.database.NewsDatabase
import com.ssessments.newsapp.database.UserData

import kotlinx.coroutines.*

const val EXTRA_NEWSID="extra_news_id"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val CHANNEL_ID="My_News_Notifications_Channel"

    var serviceJob = Job()
    val serviceScope = CoroutineScope(serviceJob + Dispatchers.Main )

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        //tip poruke
        private const val MESSAGE_TYPE_KEY="type"
        private const val MESSAGE_TYPE_VALUE_NEWS="news"

        private const val NEWS_TITLE="title"
        private const val NEWS_BODY="body"
        private const val NEWS_ID="news_id"


    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {

            // Handle message within 10 seconds
            if (remoteMessage.data.get(MESSAGE_TYPE_KEY).equals(MESSAGE_TYPE_VALUE_NEWS)) {
                val receivedTitle = remoteMessage.data.get(NEWS_TITLE)
                val receivedBody = remoteMessage.data.get(NEWS_BODY)
                val receivedNewsId = remoteMessage.data.get(NEWS_ID)
                if (receivedTitle != null && receivedBody != null && receivedNewsId != null) sendNotification(
                    receivedTitle,
                    receivedBody,
                    receivedNewsId
                )

            }

            // Check if message contains a notification payload.
            remoteMessage.notification?.let {
                Log.d(TAG, "Message Notification Body: ${it.body}")
            }

        }
    }



    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(firebasetoken: String) {

        serviceScope.launch {
            withContext(Dispatchers.IO) {
                val datasource = NewsDatabase.getInstance(application).newsDatabaseDao
                val user = datasource.getUserNoLiveData()

                if (user != null) {
                    datasource.updateUser(UserData(username=user.username,password = user.password,token=user.token,firebaseID =firebasetoken))

                }

            }
        }

    }

    private fun sendNotification(messageTitle:String,messageBody: String,messageNewsId:String) {

        val intent=Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(EXTRA_NEWSID,messageNewsId)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.channel_name)
            val mChannel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            mChannel.description = getString(R.string.channel_description)

            notificationManager.createNotificationChannel(mChannel)
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_ssessments)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        notificationManager.notify(10, notificationBuilder.build())
    }


    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()

    }

}
