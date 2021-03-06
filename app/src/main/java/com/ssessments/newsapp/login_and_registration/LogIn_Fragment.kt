package com.ssessments.newsapp.login_and_registration

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import com.ssessments.newsapp.R

import com.ssessments.newsapp.database.NewsDatabase
import com.ssessments.newsapp.databinding.FragmentLogInBinding
import com.ssessments.newsapp.utilities.EMPTY_FIREBASEID

private const val TAG_LOGIN="MY_LOGIN_FRAGMENT"
const val LOGGED_IN_USER_GOTO_MAIN_ACTIVITY="LOGGED_IN_USER_GOTO_MAIN_ACTIVITY"

class LogIn_Fragment : Fragment() {

    private lateinit var binding:FragmentLogInBinding
    private lateinit var viewModel:LogIn_ViewModel
    var myalertDialog:AlertDialog?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_log_in,container,false)

        val application= requireNotNull(this.activity).application
        val datasource= NewsDatabase.getInstance(application).newsDatabaseDao
        viewModel = ViewModelProviders.of(this, LogInViewModelFactory(datasource,application))
            .get(LogIn_ViewModel::class.java)

        val navController=this.findNavController()

        //da se ne otvoti prvi edittext sam od sebe
        binding.rootConstLayout.requestFocus()

        binding.passwordEditText.setOnEditorActionListener { v, actionId, event ->
            when (actionId){
                EditorInfo.IME_ACTION_DONE-> {

                    val inputMethodManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(binding.passwordEditText.windowToken, 0)
                    binding.passwordEditText.clearFocus()
                    true
                }

                else->false
            }
        }


        //SIGN IN
        binding.signInButton.setOnClickListener{
            if(!isUsernameValid(binding.usernameEditText.text.toString())){
                binding.usernameEditText.setError(resources.getString(R.string.invalid_username))
            }else if(!isPasswordValid(binding.passwordEditText.text.toString())){
                binding.passwordEditText.setError(resources.getString(R.string.invalid_password))
            }else {
                viewModel.signInButtonPressed(binding.usernameEditText.text.toString(), binding.passwordEditText.text.toString())
            }
        }

        //FORGOT PASSWORD
        binding.forgotpaswordtextView.setOnClickListener {

        //otvori dijalog da korisnik upise e-mail
            val alertDialog= AlertDialog.Builder(requireActivity(),R.style.MyAlertDialogThemeForLogIn)

            val myinflater = this.layoutInflater
            val dialogView = myinflater.inflate(R.layout.forgot_password_dialog_layout, null)

             myalertDialog=alertDialog.setView(dialogView).setCancelable(true).setTitle("Enter your contact e-mail address.").create()

            alertDialog.setPositiveButton("SEND", DialogInterface.OnClickListener{ dialog, id ->

                val emailView: EditText?=dialogView.findViewById(R.id.enterEmaileditText)
                val layout:ConstraintLayout=requireActivity().findViewById(R.id.login_activity_root_layout)

                if(emailView!=null && layout!=null){
                    if(emailView.text.isNullOrEmpty()|| !emailView.text.contains("@")){
                        Snackbar.make(layout, R.string.enter_email_text, Snackbar.LENGTH_LONG).show()}
                    else{
                        viewModel.sendForgotPasswordEmail(emailView.text.toString())
                        }
                }
            })

            alertDialog.show()

         }



        viewModel.showProgressBar.observe(this, Observer {
                when(it){
                true-> progressBarVisible(true)
                false->progressBarVisible(false)}

         })

        viewModel.showToastNOInternet.observe(this,Observer{
            if(it){
            Snackbar.make(binding.rootConstLayout,R.string.nointernet,Snackbar.LENGTH_LONG).show()
            viewModel.toastNoInternetIsShown()
            }
        })

        viewModel.showToastUserLoggedIN.observe(this,Observer{
            if(it){
                Toast.makeText(requireActivity(),R.string.loggedin,Toast.LENGTH_LONG).show()
                viewModel.toastLoggedInUserIsShown()
            }
        })

        viewModel.showToastWrongPasswordOrUsername.observe(this,Observer{
            if(it){
                Snackbar.make(binding.rootConstLayout,R.string.WrongPasswordorUsername,Snackbar.LENGTH_LONG).show()
                viewModel.toastWrongPasswordOrUsernameIsShown()
            }
        })

        viewModel.showToastSomethingWentWrong.observe(this,Observer{
            if(it){
                Snackbar.make(binding.rootConstLayout,R.string.network_error,Snackbar.LENGTH_LONG).show()
                viewModel.toastSomethingWentWrongIsShown()
            }

        })

        viewModel.showToastForgotPasswordHandeled.observe(this,Observer{
            if(it){
                Snackbar.make(binding.rootConstLayout,R.string.forgot_password_handled,Snackbar.LENGTH_LONG).show()
                viewModel.toastForgotPaswordHandledIsShown()
            }
        })

        viewModel.sendUserToHomeFragment.observe(this,Observer{
            if(it){
                viewModel.userSentToHomeFragment()
                activity?.finish()
            }
        })

        return binding.root

    }

    /*private fun getFirebaseID(): String {
        Log.i(TAG_LOGIN,"myUser je trenutno ${viewModel.loggedInUser.value}")
        val myfirebaseId=viewModel.loggedInUser.value?.firebaseID
        Log.i(TAG_LOGIN,"myToken je trenutno $myfirebaseId")
        if(myfirebaseId!=null){
            if(myfirebaseId.equals(EMPTY_FIREBASEID))return getFirebaseIDFromFirebaseServer()
            else return myfirebaseId
        }else return EMPTY_FIREBASEID



    }*/

    /*private fun getFirebaseIDFromFirebaseServer():String {

        var myToken="pokupi token sa servera"
        Log.i(TAG_LOGIN,"myToken je trenutno $myToken")

        //ov oided u background i ne znam kada ce se zavrsiti
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                Log.i(TAG_LOGIN,"u on complete listener")
                if (!task.isSuccessful) {
                    Log.w(TAG_LOGIN, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token
                Log.i(TAG_LOGIN,"token sa servera je  $token")
                myToken=token?:"token sa firebase servera je null"

            })

        Log.i(TAG_LOGIN,"myToken koji ide  u return je $myToken")
        return myToken
    }*/



    private fun isUsernameValid(username:String):Boolean{
       return username.isNotBlank()
    }


    private fun progressBarVisible(show: Boolean){
       if(show) {
           binding.apply {
               rootConstLayout.alpha = 0.3F
               myprogressBar.visibility = View.VISIBLE
           }
       }

       if(!show){
           binding.apply {
               rootConstLayout.alpha = 1F
               myprogressBar.visibility = View.GONE
           }
       }

    }



    override fun onDestroy() {
        super.onDestroy()
            if(myalertDialog!=null) {
            myalertDialog?.dismiss()}

    }

}

    fun isPasswordValid(password:String):Boolean{
        //return password.length > 3 && password.isNotBlank() && password.isNotEmpty()
        return true
    }

