package com.example.autoservice.ui.support

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.autoservice.R
import com.example.autoservice.data.model.ChatMessage

class SupportChatActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var ivSend: ImageView

    private val messages = mutableListOf<ChatMessage>()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_chat)

        initViews()
        setupRecyclerView()
        loadPredefinedMessages()
        setupClickListeners()
    }

    private fun initViews() {
        rvChat = findViewById(R.id.rvChat)
        etMessage = findViewById(R.id.etMessage)
        ivSend = findViewById(R.id.ivSend)
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messages)
        rvChat.layoutManager = LinearLayoutManager(this)
        rvChat.adapter = chatAdapter
    }

    private fun loadPredefinedMessages() {
        messages.clear()
        
        // Предзагруженные сообщения как на скриншоте
        messages.add(ChatMessage(
            id = 1,
            senderName = "СП",
            message = "Здравствуйте! Я ваш персональный менеджер. Чем могу помочь?",
            time = "14:30",
            isFromUser = false
        ))
        
        messages.add(ChatMessage(
            id = 2,
            senderName = "Вы",
            message = "Как ваши дела?",
            time = "13:07",
            isFromUser = true
        ))
        
        messages.add(ChatMessage(
            id = 3,
            senderName = "СП",
            message = "Спасибо за ваше сообщение! Наш специалист обработает ваш запрос в ближайшее время.",
            time = "13:07",
            isFromUser = false
        ))
        
        chatAdapter.notifyDataSetChanged()
        scrollToBottom()
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
        val userMessage = ChatMessage(
            id = messages.size + 1,
            senderName = "Вы",
            message = messageText,
            time = currentTime,
            isFromUser = true
        )
        
        messages.add(userMessage)
        chatAdapter.notifyDataSetChanged()
        scrollToBottom()
        
        // Очищаем поле ввода
        etMessage.text.clear()
        
        // Имитация ответа поддержки через 1 секунду
        ivSend.postDelayed({
            val supportMessage = ChatMessage(
                id = messages.size + 1,
                senderName = "СП",
                message = "Спасибо за ваше сообщение! Наш специалист обработает ваш запрос в ближайшее время.",
                time = getCurrentTime(),
                isFromUser = false
            )
            
            messages.add(supportMessage)
            chatAdapter.notifyDataSetChanged()
            scrollToBottom()
        }, 1000)
    }

    private fun scrollToBottom() {
        if (messages.isNotEmpty()) {
            rvChat.post {
                rvChat.smoothScrollToPosition(messages.size - 1)
            }
        }
    }

    private fun getCurrentTime(): String {
        val now = java.util.Calendar.getInstance()
        val hour = now.get(java.util.Calendar.HOUR_OF_DAY)
        val minute = now.get(java.util.Calendar.MINUTE)
        return String.format("%02d:%02d", hour, minute)
    }
}

class ChatAdapter(private val messages: List<ChatMessage>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val tvSenderName: TextView = itemView.findViewById(R.id.tvSenderName)
        val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val llMessageContainer: LinearLayout = itemView.findViewById(R.id.llMessageContainer)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ChatViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        
        holder.tvSenderName.text = message.senderName
        holder.tvMessage.text = message.message
        holder.tvTime.text = message.time
        
        // Выравнивание сообщений
        val layoutParams = holder.llMessageContainer.layoutParams as RecyclerView.LayoutParams
        if (message.isFromUser) {
            layoutParams.marginStart = 80
            layoutParams.marginEnd = 0
            holder.tvMessage.setBackgroundResource(R.drawable.bg_card)
        } else {
            layoutParams.marginStart = 0
            layoutParams.marginEnd = 80
            holder.tvMessage.setBackgroundResource(R.drawable.bg_card)
        }
        
        holder.llMessageContainer.layoutParams = layoutParams
    }

    override fun getItemCount() = messages.size
}
