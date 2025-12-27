package com.example.autoservice.ui.support

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.autoservice.R

class SupportChatActivitySimple : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_chat_simple)
        
        Toast.makeText(this, "Простой чат поддержки открыт", Toast.LENGTH_SHORT).show()

        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }
    }
}
