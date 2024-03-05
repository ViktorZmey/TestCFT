package com.example.testcft

import org.json.JSONObject

data class UserDTO(
    val gender: GenderDTO,
    val name: NameDTO
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

    companion object {
        fun fromJSON(json: JSONObject) : UserDTO = UserDTO(
            gender = parseGender(json.getString("gender")),
            name = NameDTO.fromJSON(json.getJSONObject("name"))
        )

        private fun parseGender(value: String) : GenderDTO = when (value) {
            "male" -> GenderDTO.MALE
            "female" -> GenderDTO.FEMALE
            else -> throw Exception("Can't parse gender: $value")
        }
    }
}

