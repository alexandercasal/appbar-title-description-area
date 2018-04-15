package com.alexandercasal.fadingedittextappbar

import android.databinding.BindingAdapter
import android.text.TextWatcher
import android.widget.EditText

object CustomBindings {

    @JvmStatic
    @BindingAdapter("onTextChanged")
    fun bindTextWatcher(view: EditText, watcher: TextWatcher) {
        view.addTextChangedListener(watcher)
    }
}