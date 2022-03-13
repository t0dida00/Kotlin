package com.example.finalproject
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class MessageViewModel :ViewModel() {
    var msgs= mutableStateOf(listOf<String>())
    init{
        Firebase.firestore
            .collection("messages")
            .orderBy("created_at")
            .addSnapshotListener{value,error ->
                if(error != null)
                {

                }
                else if (value != null && !value.isEmpty)
                {
                    val msg = mutableListOf<String>()
                    for (d in value.documents)
                    {
                        msg.add(d.get("username").toString()+ ": " + d.get("msg").toString())
                    }
                    msgs.value = msg
                }
            }
    }

    fun addMessange (username: String, msg: String){
            val firestore = Firebase.firestore

            val name= username.split("@")

            val msgs=Message(msg,name[0], Calendar.getInstance().time)
      //print(Calendar.getInstance().time)
        firestore.collection("messages").add(msgs)

    }

}