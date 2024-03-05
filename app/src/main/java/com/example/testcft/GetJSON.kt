package com.example.testcft

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

private const val BASE_URL = "https://randomuser.me/api/?results=10"

class GetJSON {

//    GlobalScope.launch {
//            println("MyPrint Start Loading")
//            val usersJSON = fetchUsers()
//            println("MyPrint fetched $usersJSON")
//            val usersJSONObject = JSONObject(usersJSON)
//            println("MyPrint $usersJSONObject")
//            (usersJSONObject["results"] as? JSONArray)?.let {
//                for (idx in 0 until it.length()) {
//                    val userJSON = it.getJSONObject(idx)
//                    try {
//                        val userDTO = parseUser(userJSON)
//                        println("MyPrint $userDTO")
//                    } catch (e: Exception) {
//                        println(e)
//                    }
//                }
//            }
//        }
//        println("MyPrint onCreate end")



//    }
//
//    private suspend fun fetchUsers(): String {
//        return withContext(Dispatchers.IO) {
//            URL(BASE_URL).readText()
//        }
//    }


}

//private suspend fun fetchUsers(): String {
//    return withContext(Dispatchers.IO) {
//        URL(BASE_URL).readText()
//    }
//}


//fun parseUser(jsonObj: JSONObject) : UserDTO = UserDTO(
//    name = parseName(jsonObj.getJSONObject("name"))
//)
//
//fun parseName(jsonObj: JSONObject) : UserDTO.Name = UserDTO.Name(
//    jsonObj.getString("title"),
//    jsonObj.getString("first"),
//    jsonObj.getString("last")
//)
//
//
//data class UserDTO(
//    val name: Name
//) {
//    data class Name(
//        val title: String,
//        val first: String,
//        val last: String
//    )
//}