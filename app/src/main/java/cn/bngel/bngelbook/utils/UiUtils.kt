package cn.bngel.bngelbook.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.bngel.bngelbook.R
import cn.bngel.bngelbook.activity.ActivityManager
import cn.bngel.bngelbook.activity.BaseActivity
import com.google.accompanist.glide.rememberGlidePainter
import androidx.core.app.ActivityCompat.startActivityForResult

import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import cn.bngel.bngelbook.BuildConfig
import cn.bngel.bngelbook.activity.MainActivity
import java.io.File
import java.util.*
import kotlin.reflect.KClass


/**
 * @author: bngel
 * @date: 21.11.15
 * @description:
 */

object UiUtils {

    private val billTypeMap = mapOf(
        "测试" to R.drawable.default_profile
    )

    fun getTypeImg(type: String) = billTypeMap[type]?:R.drawable.default_profile

    fun getTypes() = billTypeMap.keys

    fun installApk(file: File, context: Context = (ActivityManager.getCurActivity() as BaseActivity)) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags =
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            val uriForFile = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)
            intent.setDataAndType(uriForFile, "application/vnd.android.package-archive")
        }
        else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        }
        context.startActivity(intent)
    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "bngelbook_notification"
            val descriptionText = "bngelbook_push_notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("bngelbook", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createSimpleNotification(context: Context, title: String, content: String) {
        createNotificationChannel(context)
        val intent = Intent().apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context, "bngelbook")
            .setSmallIcon(R.drawable.bngelbook_ic_fore)
            .setColor(0xFF66CCFF.toInt())
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.bngelbook_ic))
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(context)) {
            notify(13, builder.build())
        }
    }
}