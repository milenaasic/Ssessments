package com.ssessments.news_list_home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssessments.data.NewsItem
import com.ssessments.data.UserAccessRights
import com.ssessments.data.getNewsItemArray

class NewsListHome_ViewModel: ViewModel() {

    //val repository:Repository by lazy {  }

    private val _newsList=MutableLiveData<List<NewsItem>>()
    val newsList:LiveData<List<NewsItem>>
        get()=_newsList

    private val _showNoAccessRightsSnackbar=MutableLiveData<Boolean>()
    val showNoAccessRightsSnackbar:LiveData<Boolean>
        get()=_showNoAccessRightsSnackbar

    private val _newsToBeOpenedID=MutableLiveData<Long>()
    val newsToBeOpenedID:LiveData<Long>
        get() = _newsToBeOpenedID


    init{
        _newsList.setValue(getNewsItemArray())
        _showNoAccessRightsSnackbar.value=false
        _newsToBeOpenedID.value=-1L
    }

    //otvara detail news fragment
    fun fetchNewsWithID(newsID:Long) {

        if (isNewsAccessibleByThisUser(UserAccessRights.PREMIUM_USER)) {
            //otvori detail news fragment
            _newsToBeOpenedID.value=newsID

        }else _showNoAccessRightsSnackbar.value=true
    }

    fun noAccessRightsSnackbarShown(){
        // _showNoAccessRightsSnackbar.value=false
    }

    fun isNewsAccessibleByThisUser( userType:UserAccessRights):Boolean{
            return true
    }

    fun resetNewsID(){
    _newsToBeOpenedID.value=-1L
    }


}