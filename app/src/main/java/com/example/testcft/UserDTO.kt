package com.example.testcft

import android.graphics.Picture
import org.json.JSONObject

data class UserDTO(
    val gender: GenderDTO,
    val name: NameDTO,
    val location : AddressDTO,
//    val cell: CellDTO
    val picture: PictureDTO

) {
    enum class GenderDTO {
        MALE,
        FEMALE
    }

    data class NameDTO(
        val title: String,
        val first: String,
        val last: String
    ) {
        companion object {
            fun fromJSON(json: JSONObject): NameDTO = NameDTO(
                title = json.getString("title"),
                first = json.getString("first"),
                last = json.getString("last")
            )
        }
    }

    data class AddressDTO(
        val name: String,
        val number: String,
        val city: String,
        val state: String,
        val country: String
    ) {
        companion object {
            fun fromJSON(json: JSONObject): AddressDTO = AddressDTO(
                name = json.getJSONObject("street").getString("name"),
                number = json.getJSONObject("street").getString("number"),
                city = json.getString("city"),
                state = json.getString("state"),
                country = json.getString("country")
            )
        }
    }

//    data class CellDTO(
//        val cell: String
//    ) {
//        companion object {
//            fun fromJSON(json: JSONObject): CellDTO = CellDTO(
//                cell = json.getString("cell")
//            )
//        }
//    }


    data class PictureDTO(
        val photo: String
    ) {
        companion object{
            fun fromJSON(json: JSONObject): PictureDTO = PictureDTO(
                photo = json.getJSONObject("picture").getString("medium")
            )
        }
    }


    companion object {
        fun fromJSON(json: JSONObject) : UserDTO = UserDTO(
            gender = parseGender(json.getString("gender")),
            name = NameDTO.fromJSON(json.getJSONObject("name")),
            location = AddressDTO.fromJSON(json.getJSONObject("location")),
//            cell = CellDTO.fromJSON(json.getJSONObject("cell"))
            picture = PictureDTO.fromJSON(json.getJSONObject("picture")),
        )

        private fun parseGender(value: String) : GenderDTO = when (value) {
            "male" -> GenderDTO.MALE
            "female" -> GenderDTO.FEMALE
            else -> throw Exception("Can't parse gender: $value")
        }
    }
}

