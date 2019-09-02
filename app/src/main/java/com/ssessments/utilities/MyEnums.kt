package com.ssessments.utilities

enum class Markets(val value:String){
    ALL("all"),CHINA("china"),JAPAN("japan"),SEA("sea"),NEA("nea")
}

enum class Products(val value: String){
    ALL("all"),PE("pe"),PP("pp"),PVC("pvc"),PET("pet")
}

enum class Ssessments(val value: String){
    ALL("all"),DAILY("daily"),WEEKLY("weekly"),MONTHLY("monthly"),ALERT("alert"),NEWS("news")
}

enum class Language(val value:String){
    ENGLISH("english"),MALAYSIA("malaysia")
}

enum class RegistrationFields(val value: String){
    FIRSTNAME("firstname"),LASTNAME("lastname"),EMAIL("email"), MOBILEPHONE("mobilephone"),COMPANYNAME("companyname"),COUNTRY("country"),
    USERNAME("username"),PASSWORD("password")}