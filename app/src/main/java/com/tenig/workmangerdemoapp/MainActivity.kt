package com.tenig.workmangerdemoapp

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.google.common.util.concurrent.ListenableFuture
import com.tenig.workmangerdemoapp.Worker.MyWorker
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    var constraints: Constraints? = null
    var oneTimeWorkRequest: OneTimeWorkRequest? = null
    var data: Data? = null
    var count = 0
    var workManager: WorkManager? = null
    var handler: Handler? = null
    var runnableCode: Runnable? = null
    var periodicWorkRequest: PeriodicWorkRequest? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workManager = WorkManager.getInstance(this)
        //creating constraints
        //creating constraints
        constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        data = Data.Builder()
            .putString(MyWorker.TASK_DESC, "The task data passed from MainActivity ")
            .build()


        oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInputData(data!!)
            .setConstraints(constraints!!)
            .build()

        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            MyWorker::class.java,
            5000,
            TimeUnit.MILLISECONDS
        )
            .setInputData(data!!)
            .setConstraints(constraints!!)
            .build()

    }


    override fun onResume() {
        super.onResume()
        createOneTimeRequest()
    }


    private fun createOneTimeRequest() {
        val currentTime = Calendar.getInstance().time
        Log.d("MainActivity", "currentTime=$currentTime")
        count++
        //This is the subclass of our WorkRequest
        val statuses: ListenableFuture<List<WorkInfo>> = workManager!!.getWorkInfosForUniqueWork("test")

        var workInfoList: List<WorkInfo>? = null
        try {
            workInfoList = statuses.get()
            for (workInfo in workInfoList)
            {

                val state = workInfo.state
                Log.d("MainActivity", "state = $state")

                if (state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED) {
                    Log.d("MainActivity", "state if = $state")
                } else {
                    Log.d("MainActivity", "state else = $state")
                }
            }
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        //        WorkManager.getInstance().enqueue(oneTimeWorkRequest);
        workManager?.enqueueUniqueWork("test", ExistingWorkPolicy.KEEP, oneTimeWorkRequest!!)
    }

}
