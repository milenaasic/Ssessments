package com.ssessments.utilities

enum class Markets(val value:String){
    ALL_MARKETS("All Markets"),SEA("SEA"),CHINA("China"),INDONESIA("Indonesia"),MALAYSIA("Malaysia"),VIETNAM("Vietnam"),INDIA("India")
}

enum class Products(val value: String){
    ALL_PRODUCTS("All Products"),PE("PE"),PP("PP"),PVC("PVC"),PET("PET"),STYRENICS("Styrenics")
}

enum class Ssessments(val value: String){
    ALL_SERVICES("All Services"),DAILY("Daily"),WEEKLY("Weekly"),MONTHLY("Monthly"),QUARTERLY("Quarterly"),NEWS("News"),
    PRICE("Price"),STATS("Stats"),PLANT("Plant")
}

enum class Language(val value:String){
    ENGLISH("English")
}

enum class RegistrationFields(val value: String){
    FIRSTNAME("firstname"),LASTNAME("lastname"),EMAIL("email"), MOBILEPHONE("mobilephone"),COMPANYNAME("companyname"),COUNTRY("country"),
    USERNAME("username"),PASSWORD("password")}