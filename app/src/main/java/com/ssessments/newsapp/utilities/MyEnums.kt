package com.ssessments.newsapp.utilities

enum class Markets(val value:String){
    ALL_MARKETS("All Markets"),SEA("SEA"),CHINA("China"),INDONESIA("Indonesia"),MALAYSIA("Malaysia"),VIETNAM("Vietnam"),INDIA("India")
}

enum class AllMarkets(val value:String){
    ALL_MARKETS("All Markets")
}

enum class AsiaPacificMarkets(val value:String){
    ALL("Asia Pacific"),AUSTRALIA("Australia"),INDONESIA("Indonesia"),COREA("Corea"),PHILIPPINES("Philippines"),SEA("SEA"),THAILAND("Thailand"),
    CHINA("China"),JAPAN("Japan"),MALAYSIA("Malaysia"),NEA("NEA"),SINGAPORE("Singapore"),TAIWAN("Taiwan"),VIETNAM("Vietnam")
}

enum class AfricaMarkets(val value:String){
    ALL("Africa")
}

enum class ICSMarkets(val value:String){
    ALL("ICS"),INDIA("India"),BANGLADESH("Bangladesh"),Pakistan("Pakistan")
}

enum class AmericasMarkets(val value:String){
    ALL("Americas"),BRAZIL("Brazil"),CANADA("Canada"),LATIN_AMERICA("Latin America"),MEXICO("Mexico"),US("US")
}

enum class EuropeMarkets(val value:String){
    ALL("Europe"),CENTRAL_AND_EAST("Central and East"),FRANCE("France"),GERMANY("Germany"),ITALY("Italy"),WEST_EUROPE("West Europe")
}

enum class MiddleEastMarkets(val value:String){
    ALL("Middle East"),EGYPT("Egypt"),IRAQ("Iraq"),SAUDI_ARABIA("Saudi Arabia"),UAE("UAE"),IRAN("Iran"),QATAR("Qatar"),TURKEY("Turkey")
}

enum class RussiaMarkets(val value:String){
    RUSSIA_AND_CIS("Russia and CIS")
}

enum class WorldMarkets(val value:String){
    WORLD("World")
}

enum class Products(val value: String){
    ALL_PRODUCTS("All Products"),PE("PE"),PP("PP"),PVC("PVC"),PET("PET"),STYRENICS("Styrenics")
}

enum class AllProducts(val value:String){
    ALL_PRODUCTS("All Products")
}

enum class Plastics (val value: String){
    ALL("All Plastics"),PE("PE"),PP("PP"),PVC("PVC"),PET("PET"),STYRENICS("Styrenics"),PLASTICIZERS("Plasticizers")
}

enum class Chemicals (val value: String){
    ALL("All Chemicals"),EDC("EDC"),ETHYLENE("Ethylene"),ETHYLENEBENZENE("Ethylenebenzene"),BENZENE("Benzene"),BUTADIENE("Butadiene"),CAUSTIC_SODA("Caustic Soda"),
    CHLORINE("Chlorine"),CHLOR_ALKALI("Chlor Alkali"),MONOETHYLENE_GLYCOL("Monoethylene Glycol"),PARAXYLENE("Paraxylene"),PROPYLENE("Propylene"),
    PTA_DMT("PTA/DMT"),STYRENE("Styrene"),VINYLS("Vinyls")
}

enum class EnergyFeedstocks (val value: String){
    ALL("All Energy/Feedstocks"),CRUDE_OIL("Crude Oil"),COAL("Coal"),GAS("Gas"),BIORENEWABLES("Bio/Renewables"),METHANOL("Methanol"),
    NAPHTHA("Naphtha")
}

enum class Ssessments(val value: String){
    ALL_SERVICES("All Services"),DAILY("Daily"),WEEKLY("Weekly"),MONTHLY("Monthly"),QUARTERLY("Quarterly"),NEWS("News"),
    PRICE("Price"),STATS("Stats"),PLANT("Plant")
}

enum class Services(val value: String){
    ALL_SERVICES("All Services"),DAILY("Daily"),WEEKLY("Weekly"),MONTHLY("Monthly"),QUARTERLY("Quarterly"),NEWS("News"),ANALYSIS("Analysis"),
    PRICE("Price"),STATS("Stats"),PLANT("Plant"),TALK("Talk"),INVENTORY("Inventory"),ONELINER("OneLiner")
}


enum class Language(val value:String){
    ENGLISH("English")
}

enum class RegistrationFields(val value: String){
    FIRSTNAME("firstname"),LASTNAME("lastname"),EMAIL("email"), MOBILEPHONE("mobilephone"),COMPANYNAME("companyname"),COUNTRY("country"),
    USERNAME("username"),PASSWORD("password")}