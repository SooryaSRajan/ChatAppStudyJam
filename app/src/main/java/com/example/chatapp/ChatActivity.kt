package com.example.chatapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.adapter.RecyclerAdapter
import com.example.chatapp.model.ChatModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {
    private lateinit var messageList: MutableList<ChatModel>
    private lateinit var adapter: RecyclerAdapter
    private lateinit var userKey: String
    private lateinit var userName: String
    lateinit var mRef: DatabaseReference
    private lateinit var eventListener: ValueEventListener;


    private val TAG: String = "Chat Activity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Log.d(TAG, "onCreate: ${intent.getStringExtra("NAME").toString()}")
        userName = intent.getStringExtra("NAME").toString()
        userKey = if (savedInstanceState != null) {
            savedInstanceState.getString("USER-KEY")!!
        } else {
            UUID.randomUUID().toString()
        }
        userKey += userName

        mRef = FirebaseDatabase.getInstance().reference.child("MESSAGE")

        messageList = mutableListOf()
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerAdapter(messageList, userKey)
        recyclerView.adapter = adapter

        eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messageList.clear()
                notifyNewItemAdded()
                for (i in dataSnapshot.children) {

                    val data = ChatModel(
                        name = i.child("name").value.toString(), time = i.child("time").value
                            .toString(), message = i.child("message").value
                            .toString(), userId = i.child("userId").value
                            .toString()
                    )

                    messageList.add(data)
                    notifyNewItemAdded()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        }

        mRef.addValueEventListener(eventListener)

    }

    override fun onStart() {
        super.onStart()
        sendButton.setOnClickListener {
            val message = messageField.text.toString().trim()
            sendMessage(message)
            messageField.text?.clear()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun notifyNewItemAdded() {
        if (messageList.isNotEmpty()) {
            startConversationMessage.visibility = View.GONE
            adapter.notifyItemInserted(messageList.size - 1)
            recyclerView.smoothScrollToPosition(adapter.itemCount - 1);
        } else {
            startConversationMessage.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
        }
    }


    private fun sendMessage(message: String) {
        if (message.isNotEmpty()) {
            val messageData =
                ChatModel(message = message, name = userName, userId = userKey, time = getDateTme())
            mRef.push().setValue(messageData)
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateTme(): String {
        val simpleDateFormat = SimpleDateFormat("HH:mm, dd.MM.yyyy")
        return simpleDateFormat.format(Date())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mRef.removeEventListener(eventListener)

    }
}