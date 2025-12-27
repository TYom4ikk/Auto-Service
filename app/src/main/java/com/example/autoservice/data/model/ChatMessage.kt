package com.example.autoservice.data.model

data class ChatMessage(
    val id: Int,
    val senderName: String,
    val message: String,
    val time: String,
    val isFromUser: Boolean
)
