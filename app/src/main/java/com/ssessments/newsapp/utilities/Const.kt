package com.ssessments.newsapp.utilities

import com.ssessments.newsapp.network.NetworkNewsItem
import com.ssessments.newsapp.database.NewsItem

//intent za sign in or sign up
const val START_LOG_REGISTRATION_ACTIVITY_MESSAGE="Sign_in_OR_Sign_up"
const val SIGN_IN_MENU_ITEM=0
const val SIGN_UP_MENU_ITEM=1

//user data values
const val EMPTY_TOKEN="empty_token"
const val EMPTY_USERNAME="empty_username"
const val EMPTY_PASSWORD="empty_password"
const val EMPTY_FIREBASEID="empty_firebase_id"
const val EMPTY_FIRST_NAME="empty_first_name"
const val EMPTY_LAST_NAME="empty_last_name"
const val EMPTY_ACCESS_TYPE="empty_access_type"
const val EMPTY_EMAIL="empty_email"
const val EMPTY_PHONE="empty_phone"
const val EMPTY_COMPANY="empty_company"
const val EMPTY_COUNTRY="empty_country"


//za main fragment i mainfragment viewmodel
const val INITIALIZED_FROM_SWIPE_REFRESH=true

//Seessments LinkedIn Url
const val URL_SSESSMENTS_LINKEDIN_PAGE="https://www.linkedin.com/company/13417069/admin/"
const val URL_SSESSMENTS_HOME="www.ssessments.com"
const val URL_SSESSMENTS_FACEBOOK="https://www.facebook.com/ssessments/"

//filteri default vrednosti za datum
const val NO_DATE_SELECTED_VALUE="1900-01-01 00:00:00"
const val DATE_SELECT_TEXT="Select"

//No RESULT Network News Liat
const val NO_RESULT="no_result"
val NO_RESULT_NETWORK_NEWS_LIST= listOf<NetworkNewsItem>(NetworkNewsItem(-1, NO_RESULT, arrayOf(NO_RESULT), NO_RESULT, NO_RESULT))
val EMPTY_LIST= listOf<NewsItem>()

const val HTTP_AUTH_FAILED="401"

//Notifications
const val NOTHING_SELECTED_IN_NOTIFICATIONS="None"


