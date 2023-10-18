package com.example.usercontactapp

class dataclass : ArrayList<dataclass.dataclassItem>(){
    data class dataclassItem(
        val page: Int,
        val results: List<Result>
    ) {
        data class Result(
            val userId: Int,
            val id: Int,
            val title: String,
            val body: String
        )
    }
}