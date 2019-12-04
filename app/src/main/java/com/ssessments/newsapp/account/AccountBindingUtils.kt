package com.ssessments.newsapp.account

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ssessments.newsapp.account.RegistrationData
import com.ssessments.newsapp.database.UserData
import com.ssessments.newsapp.utilities.EMPTY_COUNTRY

@BindingAdapter("setnameandsurname")
fun setNameandSurname(view:TextView,item: UserData?){
    item?.let{
        view.text="${it.firstName}, ${it.lastName}"
    }
}

@BindingAdapter("companyandcountry")
fun setCompanyAndCountry(view:TextView,item: UserData?){
    Log.i("account adapter"," user data je $item")
    item?.let{
        if(item.country.equals(EMPTY_COUNTRY))view.text=it.company
        else view.text="${it.company}, ${it.country}"
    }
}
