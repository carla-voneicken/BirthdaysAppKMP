package de.carlavoneicken.birthdaysapp.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import de.carlavoneicken.birthdaysapp.MainActivity
import de.carlavoneicken.birthdaysapp.R
import de.carlavoneicken.birthdaysapp.data.database.BirthdayEntity

private const val BIRTHDAY_CHANNEL_ID = "birthday_reminders"
private const val BIRTHDAY_CHANNEL_NAME = "Birthday reminders"
private const val BIRTHDAY_CHANNEL_DESC = "Notifications for upcoming birthdays"

fun showBirthdayNotification(context: Context, birthday: BirthdayEntity) {
    // make sure channel exists (Android 8+)
    createBirthdayChannelIfNeeded(context)

    // build tap action (open your main/detail screen)
    // -> replace MainActivity::class.java with your actual entry activity
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        putExtra("birthdayId", birthday.id)
    }

    val pendingIntent = PendingIntent.getActivity(
        context,
        birthday.id.toInt(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // build the notification
    val title = "Birthday reminder"
    val text = "It's almost ${birthday.name}'s birthday!"

    val notification = NotificationCompat.Builder(context, BIRTHDAY_CHANNEL_ID)
        .setSmallIcon(R.drawable.bg_cake)
        .setContentTitle(title)
        .setContentText(text)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    // show notification -> use birthday.id as notification ID so each person has their own
    NotificationManagerCompat.from(context)
        .notify(birthday.id.toInt(), notification)
}

private fun createBirthdayChannelIfNeeded(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            BIRTHDAY_CHANNEL_ID,
            BIRTHDAY_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = BIRTHDAY_CHANNEL_DESC
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}