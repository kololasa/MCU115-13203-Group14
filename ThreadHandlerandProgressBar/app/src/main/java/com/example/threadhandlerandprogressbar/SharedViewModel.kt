package com.example.threadhandlerandprogressbar

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())
    private var workerThread: Thread? = null
    
    // Status states: IDLE, PREPARING, WORKING, FINISHED, CANCELLED
    private val _status = MutableLiveData<String>("IDLE")
    val status: LiveData<String> = _status
    
    private val _progress = MutableLiveData<Int>(0)
    val progress: LiveData<Int> = _progress
    
    fun startWork() {
        if (workerThread?.isAlive == true) return
        
        _status.value = "PREPARING"
        _progress.value = 0
        
        workerThread = Thread {
            // 模擬準備時間 
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                handler.post { _status.value = "CANCELLED" }
                return@Thread
            }
            
            handler.post { _status.value = "WORKING" }
            
            // 模擬工作進度 1 到 100
            for (i in 1..100) {
                if (Thread.currentThread().isInterrupted) {
                    handler.post { _status.value = "CANCELLED" }
                    return@Thread
                }
                try {
                    Thread.sleep(50) // 總共大約 5 秒完成 100%
                } catch (e: InterruptedException) {
                    handler.post { _status.value = "CANCELLED" }
                    return@Thread
                }
                handler.post { _progress.value = i }
            }
            
            handler.post { _status.value = "FINISHED" }
        }
        workerThread?.start()
    }
    
    fun cancelWork() {
        workerThread?.interrupt()
    }
}
