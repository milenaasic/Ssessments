package com.ssessments.newsapp.myfirebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ssessments.newsapp.MainActivity
import com.ssessments.newsapp.R
import com.ssessments.newsapp.database.NewsDatabase
import com.ssessments.newsapp.network.NetworkNotificatiosObject
import com.ssessments.newsapp.network.NetworkUserData
import com.ssessments.newsapp.network.NewsApi
import com.ssessments.newsapp.utilities.EMPTY_TOKEN
import com.ssessments.newsapp.utilities.Markets
import com.ssessments.newsapp.utilities.Products
import com.ssessments.newsapp.utilities.Ssessments
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
        private const val MESSAGE_TYPE_VALUE_NOTIFICATIONS="notifications"
        private const val MESSAGE_TYPE_VALUE_NEWS="news"
        //json nazivi iz notifikacije za novu vest
        private const val NEWS_TITLE="title"
        private const val NEWS_BODY="body"
        private const val NEWS_ID="news_id"
        //json za notifikacije

    }
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            // Handle message within 10 seconds

            if (remoteMessage.data.get(MESSAGE_TYPE_KEY).equals(MESSAGE_TYPE_VALUE_NOTIFICATIONS)) {
                Log.d(TAG, "Message type: " + remoteMessage.data.get(MESSAGE_TYPE_KEY))
                setNotifications(remoteMessage)
            }

            if (remoteMessage.data.get(MESSAGE_TYPE_KEY).equals(MESSAGE_TYPE_VALUE_NEWS)) {
                Log.d(TAG, "Message type: " + remoteMessage.data.get(MESSAGE_TYPE_KEY))
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

            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
        }
    }
    // [END receive_message]

    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String) {

        serviceScope.launch {

            val datasource= NewsDatabase.getInstance(application).newsDatabaseDao
            val user=datasource.getUserNoLiveData()
            Log.i(TAG,"user je $user")
            if(user!=null){
                if(user.token!= EMPTY_TOKEN){
                    var sendIdDeferred=NewsApi.retrofitService.postUserLogIn(NetworkUserData(user.username,user.password))

                    try {
                        val result=sendIdDeferred.await()

                    }catch (e:Exception){

                        Log.i(TAG,"greska prilikom slanja ${e.message}")
                    }
                }
            }

         }

    }

    private fun sendNotification(messageTitle:String,messageBody: String,messageNewsId:String) {
        Log.d(TAG, "usao u send notifications: ${messageTitle}")
       // val taskStackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        //taskStackBuilder.addParentStack(NotificationNewsActivity::class.java)
       // val intent = Intent(this, NotificationNewsActivity::class.java)
        val intent=Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(EXTRA_NEWSID,messageNewsId)
        //taskStackBuilder.addNextIntent(intent)

        //val pendingIntent: PendingIntent? =taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_ONE_SHOT)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.channel_name)
            val mChannel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            mChannel.description = getString(R.string.channel_description)
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
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


    fun setNotifications(remoteMessage: RemoteMessage) {
        Log.d(TAG, "set notifications metod: " + remoteMessage.data)

                val defSharedPref = PreferenceManager.getDefaultSharedPreferences(getApplication())
                val editor: SharedPreferences.Editor = defSharedPref.edit()

                for (index in 0..Markets.values().size - 2) {
                    val s: String = Markets.values()[index + 1].toString().toLowerCase()
                    if (remoteMessage.data.containsKey(s)) {
                        editor.putBoolean(s, remoteMessage.data[s]!!.toBoolean())
                    }
                }

                for (index in 0..Products.values().size - 2) {
                    val s: String = Products.values()[index + 1].toString().toLowerCase()
                    if (remoteMessage.data.containsKey(s)) {
                        editor.putBoolean(s, remoteMessage.data[s]!!.toBoolean())
                    }
                }

                for (index in 0..Ssessments.values().size - 2) {
                    val s: String = Ssessments.values()[index + 1].toString().toLowerCase()
                    if (remoteMessage.data.containsKey(s)) {
                        editor.putBoolean(s, remoteMessage.data[s]!!.toBoolean())
                    }
                }

                editor.apply()

    }


    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

}
