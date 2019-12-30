package com.ssessments.newsapp.utilities

import android.text.TextUtils
import android.util.Log
import com.ssessments.newsapp.database.NewsItem
import com.ssessments.newsapp.network.NetworkNewsItem
import com.ssessments.newsapp.database.CurrentFilter
import com.ssessments.newsapp.database.FilterItem
import com.ssessments.newsapp.database.PredefinedFilter
import com.ssessments.newsapp.network.NetworkNewsFilterObject
import com.ssessments.newsapp.network.NetworkSinglePreference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//parse i format Date u MySQL
val dateFormatMySQL: SimpleDateFormat= SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
val dateFormatNoHours: SimpleDateFormat = SimpleDateFormat("dd MMM yyyy")
val dateFormatWithHours:SimpleDateFormat= SimpleDateFormat("dd MMM yyyy, HH:mm")
val ssessmentsDateFormat:SimpleDateFormat= SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")


private const val MYTAG="MY_converstions"
fun convertMutableListToSinglePreferencesArray(entries:Map<String,*>):Array<NetworkSinglePreference>{

    var list= mutableListOf<NetworkSinglePreference>()
    Log.i (MYTAG,"usao u list ti single pref array")

    for(value in enumValues<AsiaPacificMarkets>()){
        val s: String =value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US)
        Log.i (MYTAG,"key za asia pacific je ${s}")
        if (entries.containsKey(s)) {
            Log.i (MYTAG,"u entries postoji key za asia pacific je ${s}")
            list.add(NetworkSinglePreference(value.value,entries.get(s) as Boolean))
            Log.i (MYTAG,"dodato u listu ${value.value}")
        }
    }

    for(value in enumValues<AfricaMarkets>()){
        val s: String =value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US)
        if (entries.containsKey(s)) {
            list.add(NetworkSinglePreference(value.value,entries.get(s) as Boolean))
            Log.i (MYTAG,"dodato u listu ${value.value}")
        }
    }

    for(value in enumValues<AmericasMarkets>()){
        val s: String =value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US)
        if (entries.containsKey(s)) {
            list.add(NetworkSinglePreference(value.value,entries.get(s) as Boolean))
            Log.i (MYTAG,"dodato u listu ${value.value}")
        }
    }

    for(value in enumValues<EuropeMarkets>()){
        val s: String =value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US)
        if (entries.containsKey(s)) {
            list.add(NetworkSinglePreference(value.value,entries.get(s) as Boolean))
            Log.i (MYTAG,"dodato u listu ${value.value}")
        }
    }

    for(value in enumValues<ICSMarkets>()){
        val s: String =value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US)
        if (entries.containsKey(s)) {
            list.add(NetworkSinglePreference(value.value,entries.get(s) as Boolean))
            Log.i (MYTAG,"dodato u listu ${value.value}")
        }
    }

    for(value in enumValues<MiddleEastMarkets>()){
        val s: String =value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US)
        if (entries.containsKey(s)) {
            list.add(NetworkSinglePreference(value.value,entries.get(s) as Boolean))
            Log.i (MYTAG,"dodato u listu ${value.value}")
        }
    }

    for(value in enumValues<RussiaMarkets>()){
        val s: String =value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US)
        if (entries.containsKey(s)) {
            list.add(NetworkSinglePreference(value.value,entries.get(s) as Boolean))
            Log.i (MYTAG,"dodato u listu ${value.value}")
        }
    }

    for(value in enumValues<WorldMarkets>()){
        val s: String =value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US)
        if (entries.containsKey(s)) {
            list.add(NetworkSinglePreference(value.value,entries.get(s) as Boolean))
            Log.i (MYTAG,"dodato u listu ${value.value}")
        }
    }

    for(value in enumValues<Plastics>()){
        val s: String =value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US)
        if (entries.containsKey(s)) {
            list.add(NetworkSinglePreference(value.value,entries.get(s) as Boolean))
            Log.i (MYTAG,"dodato u listu ${value.value}")
        }
    }

    for(value in enumValues<Chemicals>()){
        val s: String =value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US)
        if (entries.containsKey(s)) {
            list.add(NetworkSinglePreference(value.value,entries.get(s) as Boolean))
            Log.i (MYTAG,"dodato u listu ${value.value}")
        }
    }

    for(value in enumValues<EnergyFeedstocks>()){
        val s: String =value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US)
        if (entries.containsKey(s)) {
            list.add(NetworkSinglePreference(value.value,entries.get(s) as Boolean))
            Log.i (MYTAG,"dodato u listu ${value.value}")
        }
    }

    for(value in enumValues<Services>()){
        val s: String =value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US)
        if (entries.containsKey(s)) {
            list.add(NetworkSinglePreference(value.value,entries.get(s) as Boolean))
            Log.i (MYTAG,"dodato u listu ${value.value}")
        }
    }


    Log.i(MYTAG, "lista je $list")
    return list.toTypedArray()

}

fun makeKeyListOfEnums():ArrayList<String>{

    var list= mutableListOf<String>()


    for(value in enumValues<AsiaPacificMarkets>()){
        if(value.equals(AsiaPacificMarkets.ALL_ASIA_PACIFIC))continue
        list.add(value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US))

    }

    for(value in enumValues<AfricaMarkets>()){

        list.add(value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US))
    }

    for(value in enumValues<AmericasMarkets>()){
        if(value.equals(AmericasMarkets.ALL_AMERICAS))continue
        list.add(value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US))
    }

    for(value in enumValues<EuropeMarkets>()){
        if(value.equals(EuropeMarkets.ALL_EUROPE))continue
        list.add(value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US))
    }

    for(value in enumValues<ICSMarkets>()){
        if(value.equals(ICSMarkets.ALL_ICS))continue
        list.add(value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US))
    }

    for(value in enumValues<MiddleEastMarkets>()){
        if(value.equals(MiddleEastMarkets.ALL_MIDDLE_EAST))continue
        list.add(value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US))
    }

    for(value in enumValues<RussiaMarkets>()){
        list.add(value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US))
    }

    for(value in enumValues<WorldMarkets>()){
        list.add(value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US))
    }

    for(value in enumValues<Plastics>()){
        if(value.equals(Plastics.ALL_PLASTICS))continue
        list.add(value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US))
    }

    for(value in enumValues<Chemicals>()){
        if(value.equals(Chemicals.ALL_CHEMICALS))continue
        list.add(value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US))
    }

    for(value in enumValues<EnergyFeedstocks>()){
        if(value.equals(EnergyFeedstocks.ALL_ENERGY))continue
        list.add(value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US))
    }

    for(value in enumValues<Services>()){
        if(value.equals(Services.ALL_SERVICES))continue
        list.add(value.toString().trim().replace(" ", "_").replace("/", "_").toLowerCase(Locale.US))
    }

    return list as ArrayList<String>
}

fun dateStringFormatISO8601oReadableWithHours(stringDate:String):String{

    val calendar: Calendar = Calendar.getInstance()
    try {
        val date: Date? = ssessmentsDateFormat.parse(stringDate)
        calendar.time = date
    } catch (e: Exception) {
        Log.w("ConversionDateException", e.message)
    }
    return dateFormatWithHours.format(calendar.time)
}


fun convertNetworkToDatabaseNewsItem(networkNewsList:List<NetworkNewsItem>):List<NewsItem>{
    var resultList= ArrayList<NewsItem>()

    for(item in networkNewsList){
        resultList.add(NewsItem(0L,item.newsID,item.title,TextUtils.join(",",item.tags),item.date_time,item.access))

    }
    return resultList
}

fun convertCurrentFilterToNetworkNewsFilterObject(token:String,item:CurrentFilter):NetworkNewsFilterObject{

var dateToValue=item.dateTo
if(dateToValue.equals(NO_DATE_SELECTED_VALUE)) dateToValue=getCurentDateTime()
return NetworkNewsFilterObject( token=token,
                                markets = convertStringWithCommasToRealArray(item.market),
                                products = convertStringWithCommasToRealArray(item.product),
                                ssessments = convertStringWithCommasToRealArray(item.ssessment),
                                language = item.language,
                                dateFrom = item.dateFrom,
                                dateTo = dateToValue)
}

fun getCurentDateTime(): String {
    val cal=Calendar.getInstance().time
    return dateFormatMySQL.format(cal)
}



fun convertFilterItemFromDatabaseToCurrentFilter(item:FilterItem):CurrentFilter{
    return CurrentFilter(
        market = item.market,
        product = item.product,
        ssessment = item.ssessment,
       language =  item.language,
        dateFrom = item.dateFrom,
        dateTo = item.dateTo
    )
}

fun convertPredefinedFilterToCurrentFilter(item:PredefinedFilter):CurrentFilter{
    return CurrentFilter(
        market = item.market,
        product = item.product,
        ssessment = item.ssessment,
        language = item.language,
        dateTo = item.dateFrom,
        dateFrom = item.dateTo)

}

fun convertPredefinedFilterToNetworkNewsFilterObject(token:String,item:PredefinedFilter):NetworkNewsFilterObject{

    return NetworkNewsFilterObject(
        token=token,
        markets = convertStringWithCommasToRealArray(item.market),
        products = convertStringWithCommasToRealArray(item.product),
        ssessments = convertStringWithCommasToRealArray(item.ssessment),
        language = item.language,
        dateTo = item.dateFrom,
        dateFrom = item.dateTo)

}

fun convertStringWithCommasToArray(s: String):ArrayList<String>{

    val list=ArrayList<String>()
    TextUtils.SimpleStringSplitter(',').apply {
           setString(s)
           while (hasNext()){
           val string:String=next()
           list.add(string.trim())
           }
    }
    return list
}

fun convertArrayListToStringWithCommas(list:ArrayList<String>):String{
    return TextUtils.join(",",list)
}

fun convertStringWithCommasToRealArray(s: String):Array<String>{
    val list= convertStringWithCommasToArray(s)
    return list.toTypedArray()
}

