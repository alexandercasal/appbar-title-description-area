package com.alexandercasal.fadingedittextappbar

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import com.alexandercasal.fadingedittextappbar.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.et_title
import kotlinx.android.synthetic.main.activity_main.toolbar_main
import kotlinx.android.synthetic.main.activity_main.toolbar_title

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: PageViewModel
    private lateinit var bindings: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAppBar()
        initViewModel()
        initDataBindings()
    }

    private fun initAppBar() {
        setSupportActionBar(toolbar_main)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(PageViewModel::class.java)
    }

    private fun initDataBindings() {
        bindings = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bindings.apply {
            viewModel = this@MainActivity.viewModel
            titleTextWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    viewModel?.title?.value = s.toString()
                }
            }
            setLifecycleOwner(this@MainActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}
