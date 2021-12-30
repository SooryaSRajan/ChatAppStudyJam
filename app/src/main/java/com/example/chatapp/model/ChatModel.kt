package com.example.chatapp.model

import java.io.Serializable

data class ChatModel(
    var name: String,
    var message: String,
    var userId: String,
    var time: String
) : Serializable {

}