package com.example.smartmatch.ui.findC


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.smartmatch.R
import com.example.smartmatch.base.activity.BaseActivity
import com.example.smartmatch.base.kxt.toast
import com.example.smartmatch.databinding.ActivityFindcBinding
import com.example.smartmatch.databinding.ActivityMainBinding
import com.example.smartmatch.ui.construction.areadefine.AreaDefineFragment
import com.example.smartmatch.ui.findC.step_one.FindCStepOneFragment
import com.example.smartmatch.ui.viewModel1.FindCStepOneViewModel
import com.example.smartmatch.ui.viewModel1.FindCViewModel


class FindCActivity : BaseActivity<ActivityFindcBinding>() {

    private lateinit var findCStepOneFragment: FindCStepOneFragment
    private lateinit var activityFindcBinding: ActivityFindcBinding
    private val mfindcViewModel:  FindCViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FindCViewModel::class.java]
    }
    override fun ActivityFindcBinding.initBindingView() {
        binding.findcviewModel = mfindcViewModel
       initActivity()
        initView()
        //Connector.getDatabase()
    }
        fun initActivity() {
        //init(R.layout.activity_findc, FindCViewModel::class.java, this)
        activityFindcBinding=DataBindingUtil.setContentView(this,R.layout.activity_findc)
        binding.findcviewModel=ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(FindCViewModel::class.java)
        activityFindcBinding.lifecycleOwner=this

    }

     fun initView() {
        findCStepOneFragment = FindCStepOneFragment()
        replaceFragment(findCStepOneFragment)
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.findCFrameLayout, fragment)
        transaction.addToBackStack(null) // 碎片模拟返回栈
        transaction.commitAllowingStateLoss()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        findCStepOneFragment.initViewParams()
    }

}