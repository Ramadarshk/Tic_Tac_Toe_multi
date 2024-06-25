package com.example.mute

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class viewable():ViewModel() {
    private var _board1 = mutableStateListOf<String>("","","","","","","","","")
    private var _board = mutableStateListOf<String>("","","","","","","","","")
    private var _value = mutableStateOf(" ")
    private var _count= mutableIntStateOf(0)
   private var _accesser= mutableStateListOf<Boolean>(true,true,true,true,true,true,true,true,true)
    //private var
    var value = _value
    var board:MutableList<String> = _board
    var accesser:MutableList<Boolean> = _accesser
    var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("board")

    fun setVisible(index:Int){
        if (win().equals(" ")){
        board[index]=change(value.value)
        accesser[index]=false
        _count.intValue++
        database.child("board1").setValue(boardinfo(board,_count.intValue,_value.value,_accesser))
        colck()}
    }
     data class boardinfo(var board:MutableList<String>,var count:Int,var value:String,var _accesser:MutableList<Boolean>){
         constructor():this(mutableListOf<String>("","","","","","","","",""),0," " ,mutableListOf<Boolean>(true,true,true,true,true,true,true,true,true))
     }
    fun colck(){
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //val value = snapshot.getValue(String::class.java)
                if (snapshot.exists()){
                    val value = snapshot.child("board1").getValue(boardinfo::class.java)
                    changeBoard(value!!.board)
                    _count.intValue=value.count
                    _value.value=value.value
                    _accesser.clear().also {
                        _accesser.addAll(value._accesser)
                    }
                    accesser=_accesser
                    valueOn.value=when(value.value){
                        "X"->"O"
                        else->"X"
                    }
                }
                Log.d("TAG", "Value changed $board")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }


        })
    }


    private fun changeBoard(board: MutableList<String>){
        for (i in board.indices){
            _board[i]=board[i]
        }
    }

    private var valueOn = mutableStateOf("X")
    fun change1() {
        val x= when(valueOn.value){
            "X"->"O"
            else->"X"
        }

        valueOn.value =x
    }

    fun win(): String {
        val value = value.value
        for (i in (0..8 step 3)){
            if(board[i]==value && board[i+1]==value && board[i+2]==value)
                return value
        }
        for (i in (0..2)) {
            if (board[i] == value && board[i + 3] == value && board[i + 6] == value)
                return value
        }
        if (board[0] == value && board[4] == value && board[8] == value)
            return value
        if (board[2] == value && board[4] == value && board[6] == value)
            return value
        return " "
    }

    private fun change(value: String): String {
        val x= when(value){
            "X"->"O"
            else->"X"
        }
        this.value.value =x
        return x
    }
    fun setClear() {
        valueOn.value="X"
        database.child("board1").setValue(boardinfo())
        //board= mutableStateListOf<String>("","","","","","","","","")
        _count.intValue=0
        _value.value=" "
        value.value=" "
        _board.clear().also {
            _board.addAll(_board1)
        }
        _accesser.clear().also {
            _accesser.addAll(mutableListOf<Boolean>(true,true,true,true,true,true,true,true,true))
        }
        //colck()
    }

    fun get(): String {
        var a=""
        for (i in _board){
            a+=i
        }
        return a
    }

    fun title(): String {
        val a=win()
        return if (a!=" "){
            _accesser.clear().also {
                _accesser.addAll(mutableListOf<Boolean>(false,false,false,false,false,false,false,false,false))
            }
            boardinfo(board,_count.intValue,valueOn.value,_accesser)
            "$a Has Won"}
        else if(_count.intValue==9){
            //setClear()
            "Draw Game"
        }
        else{
            "${valueOn.value} Turn"
        }

    }


}