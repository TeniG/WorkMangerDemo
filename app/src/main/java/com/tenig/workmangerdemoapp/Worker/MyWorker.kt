package com.tenig.workmangerdemoapp.Worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tenig.workmangerdemoapp.R
import java.util.*

class MyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    private val context = context
    var outputData : Data = Data.EMPTY


    companion object {
        const val IS_NEXT_ROW_PRESENT : String = "isNextRowPresent"
        val TASK_DESC = "task_desc"
    }

    override fun doWork(): Result {

        //getting the input data
        //getting the input data
        val taskDesc = inputData.getString(TASK_DESC)
        val currentTime = Calendar.getInstance().time



        displayNotification("My Worker", "$taskDesc $currentTime")
        val outputDataStr: String
        outputDataStr =
            if (taskDesc === "The task data passed from MainActivity ") { //setting output data
                "The conclusion of the task from first request"
            } else {
                "The conclusion of the task from second request"
            }

        //setting output data
        //setting output data
        val outputData = Data.Builder()
            .putString(TASK_DESC, outputDataStr)
            .build()



        return Result.success(outputData)
    }


    private fun displayNotification(title: String, task: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        val notification =
            NotificationCompat.Builder(applicationContext, "simplifiedcoding")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.mipmap.ic_launcher)
        notificationManager.notify(1, notification.build())
    }


}