package com.example.autoservice.ui.support

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.autoservice.R

class SupportChatActivityFull : AppCompatActivity() {

    private lateinit var scrollViewChat: ScrollView
    private lateinit var llChatContainer: LinearLayout
    private lateinit var etMessage: EditText
    private lateinit var ivSend: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_chat_full)
        
        Toast.makeText(this, "Полнофункциональный чат открыт", Toast.LENGTH_SHORT).show()

        initViews()
        loadPredefinedMessages()
        setupClickListeners()
    }

    private fun initViews() {
        scrollViewChat = findViewById(R.id.scrollViewChat)
        llChatContainer = findViewById(R.id.llChatContainer)
        etMessage = findViewById(R.id.etMessage)
        ivSend = findViewById(R.id.ivSend)
    }

    private fun loadPredefinedMessages() {
        // Только одно приветственное сообщение от поддержки
        addMessageToChat("СП", "Здравствуйте! Я ваш персональный менеджер. Чем могу помочь?", "14:30", false)
    }

    private fun setupClickListeners() {
        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }

        ivSend.setOnClickListener {
            sendMessage()
        }

        etMessage.setOnEditorActionListener { _, actionId, event ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
                sendMessage()
                true
            } else {
                false
            }
        }
    }

    private fun sendMessage() {
        val messageText = etMessage.text.toString().trim()
        
        if (messageText.isEmpty()) {
            return
        }

        // Добавляем сообщение пользователя
        val currentTime = getCurrentTime()
        addMessageToChat("Вы", messageText, currentTime, true)
        
        // Очищаем поле ввода
        etMessage.text.clear()
        
        // Прокрутка вниз
        scrollViewChat.post {
            scrollViewChat.fullScroll(ScrollView.FOCUS_DOWN)
        }
        
        // Имитация ответа поддержки через 1 секунду
        ivSend.postDelayed({
            addMessageToChat("СП", "Спасибо за ваше сообщение! Наш специалист обработает ваш запрос в ближайшее время.", getCurrentTime(), false)
            
            // Прокрутка вниз после ответа
            scrollViewChat.post {
                scrollViewChat.fullScroll(ScrollView.FOCUS_DOWN)
            }
        }, 1000)
    }

    private fun addMessageToChat(senderName: String, message: String, time: String, isFromUser: Boolean) {
        val messageView = layoutInflater.inflate(R.layout.item_chat_message, llChatContainer, false)
        
        messageView.findViewById<TextView>(R.id.tvSenderName).text = senderName
        messageView.findViewById<TextView>(R.id.tvMessage).text = message
        messageView.findViewById<TextView>(R.id.tvTime).text = time
        
        // Выравнивание сообщений
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        
        if (isFromUser) {
            layoutParams.setMargins(80, 0, 0, 12)
            // Выравниваем контейнер по правому краю
            messageView.findViewById<LinearLayout>(R.id.llMessageContainer).gravity = android.view.Gravity.END
        } else {
            layoutParams.setMargins(0, 0, 80, 12)
            // Выравниваем контейнер по левому краю
            messageView.findViewById<LinearLayout>(R.id.llMessageContainer).gravity = android.view.Gravity.START
        }
        
        messageView.layoutParams = layoutParams
        llChatContainer.addView(messageView)
    }

    private fun getCurrentTime(): String {
        val now = java.util.Calendar.getInstance()
        val hour = now.get(java.util.Calendar.HOUR_OF_DAY)
        val minute = now.get(java.util.Calendar.MINUTE)
        return String.format("%02d:%02d", hour, minute)
    }
}
