package com.example.testcft

import org.json.JSONObject
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class UserDTO(
    var _id : Long = 0,
    val gender : GenderDTO,
    val name : NameDTO,
    val address : AddressDTO,
    val location: LocationDTO,
    val email : String,
    val dob : DobDTO,
    val phone : String,
    val cell : String,
    val picture : PictureDTO,
    val nat : String
) : Serializable {
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
        val streetName: String,
        val houseNumber: String,
        val city: String,
        val state: String,
        val country: String,
        val postcode: String
    ) {
        companion object {
            fun fromJSON(json: JSONObject): AddressDTO = AddressDTO(
                streetName = json.getJSONObject("street").getString("name"),
                houseNumber = json.getJSONObject("street").getString("number"),
                city = json.getString("city"),
                state = json.getString("state"),
                country = json.getString("country"),
                postcode = json.getString("postcode")
            )
        }
    }

    data class LocationDTO(
        val latitude: Double,
        val longitude: Double
    ) {
        companion object {
            fun fromJSON(json: JSONObject) = LocationDTO(
                latitude = json.getDouble("latitude"),
                longitude = json.getDouble("longitude")
            )
        }
    }

    data class DobDTO(
        val date: LocalDateTime,
        val age: String
    ) {
        companion object{
            fun fromJSON(json: JSONObject): DobDTO = DobDTO(
                date = LocalDateTime.parse(json.getString("date"), DateTimeFormatter.ISO_DATE_TIME),
                age = json.getString("age"),
            )
        }
    }

    data class PictureDTO(
        val large: String,
        val medium: String,
        val thumbnail: String
    ) {
        companion object{
            fun fromJSON(json: JSONObject): PictureDTO = PictureDTO(
                large = json.getString("large"),
                medium = json.getString("medium"),
                thumbnail = json.getString("thumbnail")
            )
        }
    }

    companion object {
        fun fromJSON(json: JSONObject) : UserDTO = UserDTO(
            gender = parseGender(json.getString("gender")),
            name = NameDTO.fromJSON(json.getJSONObject("name")),
            address = AddressDTO.fromJSON(json.getJSONObject("location")),
            location = LocationDTO.fromJSON(json.getJSONObject("location").getJSONObject("coordinates")),
            email = json.getString("email"),
            dob = DobDTO.fromJSON(json.getJSONObject("dob")),
            phone = json.getString("phone"),
            cell = json.getString("cell"),
            picture = PictureDTO.fromJSON(json.getJSONObject("picture")),
            nat = json.getString("nat")
        )

        private fun parseGender(value: String) : GenderDTO = when (value) {
            "male" -> GenderDTO.MALE
            "female" -> GenderDTO.FEMALE
            else -> throw Exception("Can't parse gender: $value")
        }
    }
}

