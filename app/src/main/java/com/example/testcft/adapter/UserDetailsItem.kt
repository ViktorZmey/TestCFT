package com.example.testcft.adapter

data class UserDetailsItem(
    val type: ContentType
) {
    sealed class ContentType {
        data class Photo(val url: String) : ContentType()
        data class FirstName(val firstName: String) : ContentType()
        data class LastName(val lastName: String) : ContentType()
        data class AddressName(val addressName: String) : ContentType()
        data class AddressNumber(val addressNumber: String) : ContentType()
        data class AddressCity(val addressCity: String) : ContentType()
        data class AddressState(val addressState: String) : ContentType()
        data class AddressCountry(val addressCountry: String) : ContentType()
        data class Email(val email: String) : ContentType()
        data class DobDate(val dobDate: String) : ContentType()
        data class DobAge(val dobAge: String) : ContentType()
        data class Phone(val phone: String) : ContentType()
        data class Cell(val cell: String) : ContentType()
        data class Nat(val nat: String) : ContentType()
    }
}