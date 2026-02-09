package de.carlavoneicken.birthdaysapp.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import de.carlavoneicken.birthdaysapp.MainActivity
import de.carlavoneicken.birthdaysapp.R
import de.carlavoneicken.birthdaysapp.data.database.BirthdayEntity


private const val BIRTHDAY_CHANNEL_ID = "birthday_reminders"
private const val BIRTHDAY_CHANNEL_NAME = "Birthday reminders"
private const val BIRTHDAY_CHANNEL_DESC = "Notifications for upcoming birthdays"

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun showBirthdayNotification(context: Context, birthday: BirthdayEntity, reminderText: String) {
    // make sure channel exists (Android 8+)
    createBirthdayChannelIfNeeded(context)

    /*
    Create an Intent for tapping the notification
    Intents: "something I want Android to do" e.g. start this activity, start this service, open this app screen, open this URL, etc.
    -> in this case: launch MainActivity and tell it which birthday to open

    The flags control HOW the activity is launched
    -> FLAG_ACTIVITY_NEW_TASK: start this activity fresh, in a new task (app stack), instead of pushing it on top of whatever is currently open
    -> FLAG_ACTIVITY_CLEAR_TASK: before launching, clear any existing Activities in this task
    Together they clear any existing app UI and start MainActivity brand new
     */
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        // intent passes along which birthday was tapped
        putExtra("birthdayId", birthday.id)
    }

    /*
    Wrap Intent into PendingIntent
    PendingIntent: permission we give to Android to execute the intent on our behalf in the future
    -> notifications are managed by the system, not the app, so when the user taps the notification, the app may not be running
    -> system uses the PendingIntent to launch the activity on our behalf
    "Here Android, I authorize you to run this intent later when the notification is tapped."

    PendingIntent.getActivity() -> when triggered, launch an Activity
    birthday.id.toInt() -> request code, using unique ID per birthday means each notification will have its own tap action
    Flags:
    -> FLAG_UPDATE_CURRENT: if a PendingIntent with this requestCode already exists, update it with this new Intent
    -> FLAG_IMMUTABLE: once created, Android cannot modify the Intent (required for security on Android 12+)
     */
    val pendingIntent = PendingIntent.getActivity(
        context,
        birthday.id.toInt(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // build the notification
    val notification = NotificationCompat.Builder(context, BIRTHDAY_CHANNEL_ID)
        .setSmallIcon(R.drawable.bg_cake)
        .setContentTitle("Birthday reminder")
        .setContentText(reminderText)
        .setContentIntent(pendingIntent)
        // automatically remove notification once tapped
        .setAutoCancel(true)
        // priority influences whether notification pops up immediately, makes sound, where it appears, etc. (ignored in Android 8+)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    // show notification -> use birthday.id as notification ID so each person has their own
    NotificationManagerCompat.from(context)
        .notify(birthday.id.toInt(), notification)
}

/*
A channel is a named category for notifications, controlled entirely by the user.
It has an ID, a human-readable name, an importance level (sound/no sound, etc.), its own settings in Android's system UI

-> Every notification (in Android 8+) must belong to a channel
-> They must be created ONCE
-> Users can modify channel settings any time
 */
private fun createBirthdayChannelIfNeeded(context: Context) {
    // only if Android 8+ (below no channels exist)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // create a channel with the specified id, name importance and description
        val channel = NotificationChannel(
            BIRTHDAY_CHANNEL_ID,
            BIRTHDAY_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = BIRTHDAY_CHANNEL_DESC
        }

        // get the Notificationmanager -> system service that manages notifications and channels
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // register channel with the system -> if the channel already exists, Android ignores the call
        manager.createNotificationChannel(channel)
    }
}