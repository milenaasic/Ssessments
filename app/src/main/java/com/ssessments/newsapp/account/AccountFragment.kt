package com.ssessments.newsapp.account

import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.ssessments.newsapp.R
import com.ssessments.newsapp.database.NewsDatabase
import com.ssessments.newsapp.databinding.AccountFragmentBinding
import com.ssessments.newsapp.news_list_home.MainFragmentViewModel
import com.ssessments.newsapp.news_list_home.MainFragmentViewModelFactory


private const val MYTAG="AccountFragment"
class AccountFragment : Fragment() {


    private lateinit var viewModel: AccountViewModel
    private lateinit var binding:AccountFragmentBinding
    private lateinit var inputMethodManager:InputMethodManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        inputMethodManager= requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

    }

    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding=DataBindingUtil.inflate(inflater,R.layout.account_fragment,container, false)


        val application= requireActivity().application
        val datasource= NewsDatabase.getInstance(application).newsDatabaseDao
        viewModel = ViewModelProviders.of(this,AccountViewModelFactory(datasource,application))
            .get(AccountViewModel::class.java)

        viewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        Log.i(MYTAG,"on create view")



        binding.editEmailImage.setOnClickListener {
            binding.accountemailtextView.requestFocus()
            inputMethodManager.showSoftInput(binding.accountemailtextView,SHOW_IMPLICIT)
        }

        binding.editPhoneImage.setOnClickListener {
            binding.accountphoneValuetextView.requestFocus()
            inputMethodManager.showSoftInput(binding.accountphoneValuetextView,SHOW_IMPLICIT)

        }

        binding.accountemailtextView.setOnEditorActionListener { v, actionId, event ->
            when (actionId){
                EditorInfo.IME_ACTION_DONE-> {

                    inputMethodManager.hideSoftInputFromWindow(binding.accountemailtextView.windowToken, 0)
                    binding.accountemailtextView.clearFocus()
                    true
                }

                else->false
            }
        }

        binding.accountphoneValuetextView.setOnEditorActionListener { v, actionId, event ->
            when (actionId){
                EditorInfo.IME_ACTION_DONE-> {

                    inputMethodManager.hideSoftInputFromWindow(binding.phonetextView.windowToken, 0)
                    binding.accountphoneValuetextView.clearFocus()
                    true
                }

                else->false
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.myUser.observe(this, Observer{ userData->
            Log.i(MYTAG,"user data je $userData")
            if(userData!=null){
            binding.userRegistrationData=userData
            binding.executePendingBindings()
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.account_fragment_menu,menu)
        menu.removeItem(R.id.filter_menu_item)
        menu.removeItem(R.id.action_search)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.save_account_changes->{
                viewModel.userChangedHisRegData(binding.accountemailtextView.text.toString(),binding.accountphoneValuetextView.text.toString())
                //findNavController().navigateUp()
                return true
            }
            else-> return false
        }
    }



    override fun onStop() {
        super.onStop()
        if(inputMethodManager.isActive) inputMethodManager.hideSoftInputFromWindow(binding.accountConstraintLayout.windowToken, 0)
        binding.accountConstraintLayout.requestFocus()
    }

}



