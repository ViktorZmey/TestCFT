package com.example.testcft.adapter

data class UserDetailsItem(
    val type: ContentType,
    val showAccessory: Boolean = false,
    val tapAction: (UserDetailsItem.() -> Unit)? = null
) {
    sealed class ContentType {
        data class Photo(val url: String) : ContentType()
        data class Gender(val gender: String) : ContentType()
        data class FirstName(val firstName: String) : ContentType()
        data class LastName(val lastName: String) : ContentType()
        data class Address(val address: String) : ContentType()
        data class Location(val location: String) : ContentType()
        data class Email(val email: String) : ContentType()
        data class DobDate(val dobDate: String) : ContentType()
        data class DobAge(val dobAge: String) : ContentType()
        data class Phone(val phone: String) : ContentType()
        data class Cell(val cell: String) : ContentType()
        data class Nat(val nat: String) : ContentType()
    }
}