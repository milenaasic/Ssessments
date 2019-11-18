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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//parse i format Date u MySQL
val dateFormatMySQL: SimpleDateFormat= SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
val dateFormatNoHours: SimpleDateFormat = SimpleDateFormat("dd MMM yyyy")
val dateFormatWithHours:SimpleDateFormat= SimpleDateFormat("dd MMM yyyy, HH:mm")
val ssessmentsDateFormat:SimpleDateFormat= SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")


fun convertMutableListToSinglePreferencesArray(entries:MutableMap<String,*>):Array<NetworkSinglePreference>{

    var list= mutableListOf<NetworkSinglePreference>()


    for (index in 0..Markets.values().size - 2) {
        val s: String = Markets.values()[index + 1].toString().toLowerCase()
        if (entries.containsKey(s)) {
            list.add(NetworkSinglePreference(s,entries.get(s) as Boolean))
        }
    }

    for (index in 0..Products.values().size - 2) {
        val s: String = Products.values()[index + 1].toString().toLowerCase()
        if (entries.containsKey(s)) {
            list.add(NetworkSinglePreference(s,entries.get(s) as Boolean))
        }
    }

    for (index in 0..Ssessments.values().size - 2) {
        val s: String = Ssessments.values()[index + 1].toString().toLowerCase()
        if (entries.containsKey(s)) {
            list.add(NetworkSinglePreference(s,entries.get(s) as Boolean))
        }
    }

    Log.i("CONVERSIONS"," lista je $list")

    return list.toTypedArray()

                    /*                sea = entries.get("sea") as Boolean,
                                    china=entries.get("china") as Boolean,
                                    indonesia = entries.get("indonesia") as Boolean,
                                    malaysia = entries.get("malaysia") as Boolean,
                                    vietnam = entries.get("vietnam") as Boolean,
                                    india = entries.get("india") as Boolean,
                                    pe = entries.get("pe") as Boolean,
                                    pp=entries.get("pp") as Boolean,
                                    pvc = entries.get("pvc") as Boolean,
                                    pet = entries.get("pet") as Boolean,
                                    styrenics = entries.get("styrenics") as Boolean,
                                    daily = entries.get("daily") as Boolean,
                                    weekly = entries.get("weekly") as Boolean,
                                    monthly = entries.get("monthly") as Boolean,
                                    quarterly = entries.get("quarterly") as Boolean,
                                    news = entries.get("news") as Boolean,
                                    price = entries.get("price") as Boolean,
                                    stats = entries.get("stats") as Boolean,
                                    plant=entries.get("plant") as Boolean
                                    )*/


}


fun dateStringFormatSQlToReadableWithHours(stringDate:String):String{

    val calendar: Calendar = Calendar.getInstance()
    Log.i("ConversionDateExc ulaz ",stringDate )
    try {
        val date: Date = dateFormatMySQL.parse(stringDate)
            calendar.time = date
        Log.i("ConversionDate cal.tim","${calendar.time}" )
    } catch (e: Exception) {
        Log.w("ConversionDateException", e.message)
    }
    Log.i("ConversionDate frm tim","${dateFormatWithHours.format(calendar.time)}")
    return dateFormatWithHours.format(calendar.time)
}


fun dateStringFormatISO8601oReadableWithHours(stringDate:String):String{

    val calendar: Calendar = Calendar.getInstance()
    Log.i("ConversionDateExc ulaz ",stringDate )
    try {
        val date: Date? = ssessmentsDateFormat.parse(stringDate)
        calendar.time = date
        Log.i("ConversionDate cal.tim","${calendar.time}" )
    } catch (e: Exception) {
        Log.w("ConversionDateException", e.message)
    }
    Log.i("ConversionDate frm tim","${dateFormatWithHours.format(calendar.time)}")
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
                                dateFrom = item.dateFrom,
                                dateTo = dateToValue)
}

fun getCurentDateTime(): String {
    val cal=Calendar.getInstance().time
    return dateFormatMySQL.format(cal)
}

fun convertFilterItemFromDatabaseToNetworkNewsFilterObject(token:String,item:FilterItem):NetworkNewsFilterObject{
    return NetworkNewsFilterObject( token=token,
                                    markets = convertStringWithCommasToRealArray(item.market),
                                    products = convertStringWithCommasToRealArray(item.product),
                                    ssessments = convertStringWithCommasToRealArray(item.ssessment),
                                    language = item.language,
                                    dateFrom = item.dateFrom,
                                    dateTo = item.dateTo)
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
           list.add(next())
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

