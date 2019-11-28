package com.ssessments.newsapp.account

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ssessments.newsapp.account.RegistrationData

@BindingAdapter("setnameandsurname")
fun setNameandSurname(view:TextView,item: RegistrationData){
    item?.let{
        view.text="${it.name}, ${it.surname}"
    }
}

@BindingAdapter("companyandcountry")
fun setCompanyAndCountry(view:TextView,item: RegistrationData){
    item?.let{
        if(item.country.isNullOrBlank())view.text="${it.company}"
        else view.text="${it.company}, ${it.country}"
    }
}
