package kr.book_stack

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import kr.book_stack.databinding.ActivityRegBinding
import kr.book_stack.databinding.DialogTagMakeBinding
import kr.book_stack.databinding.LayoutProgressBarTransBinding
import kr.book_stack.fragment.HighLightFragment
import kr.book_stack.fragment.SearchFragment
import kr.book_stack.fragment_reg.HFragment3
import kr.book_stack.fragment_reg.InfoFragment1
import kr.book_stack.fragment_reg.TagFragment2
import kr.book_stack.fragment_reg.TagMakeFragment2

class RegActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegBinding
    private var progressDialogView: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarReg)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.svg_back)

        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")
        val profile = intent.getStringExtra("profile")

        //goSearchFragment()
        val bundle = Bundle()
        bundle.putString("user_id", id)
        bundle.putString("user_name", name)
        bundle.putString("user_profile", profile)
        //goTagFragment()
        goFragment(InfoFragment1(), bundle)

        overridePendingTransition(
            com.google.android.material.R.anim.design_bottom_sheet_slide_in,
            com.google.android.material.R.anim.design_bottom_sheet_slide_in
        )
    }


    fun goTagFragment() {
        val fragment: TagFragment2 = TagFragment2().newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout_registration, fragment)
            .commit()
    }

    fun goTagMakeFragment() {
        val fragment: TagMakeFragment2 = TagMakeFragment2().newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout_registration, fragment)
            .commit()
    }

    fun goH3Fragment() {
        val fragment: HFragment3 = HFragment3().newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout_registration, fragment)
            .commit()
    }

    @SuppressLint("PrivateResource")
    fun goSearchFragment() {
        val fragment: SearchFragment = SearchFragment().newInstance()
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                com.google.android.material.R.anim.design_bottom_sheet_slide_in,
                com.google.android.material.R.anim.design_bottom_sheet_slide_in
            )
            .addToBackStack(null)
            .replace(R.id.frameLayout_registration, fragment)
            .commit()
    }

    fun goHighlightFragment() {
        val fragment: HighLightFragment = HighLightFragment().newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout_registration, fragment)
            .commit()
    }

    @SuppressLint("PrivateResource")
    fun goFragment(inFragment: Fragment, inBundle: Bundle?) {
        inFragment.arguments = inBundle
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                com.google.android.material.R.anim.design_bottom_sheet_slide_in,
                com.google.android.material.R.anim.design_bottom_sheet_slide_out
            )
            .addToBackStack(null)
            .replace(R.id.frameLayout_registration, inFragment)
            .commit()
    }

    fun goMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun progressView(inDelay: Long) {
        val builder = AlertDialog.Builder(this)
        val dialogView = LayoutProgressBarTransBinding.inflate(this.layoutInflater)
        builder.setView(dialogView.root)
        builder.setCancelable(false)


        progressDialogView = builder.create()
        progressDialogView!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialogView!!.show()
    }

    fun progressDismiss() {
        progressDialogView?.let {
            if (it.isShowing) {
                it.dismiss()
            }
            progressDialogView = null
        }
    }

    fun regFinish() {
        finish()
    }
}