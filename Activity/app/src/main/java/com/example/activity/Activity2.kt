package com.example.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Activity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvReceived = findViewById<TextView>(R.id.tvReceived)
        val etReply = findViewById<EditText>(R.id.etReply)
        val btnReply = findViewById<Button>(R.id.btnReply)

        // 接收第一頁傳來的資料並直接顯示
        val receivedData = intent.getStringExtra("send_data")
        tvReceived.text = receivedData

        btnReply.setOnClickListener {
            val replyText = etReply.text.toString()
            
            val resultIntent = Intent().apply {
                putExtra("reply_data", replyText)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}