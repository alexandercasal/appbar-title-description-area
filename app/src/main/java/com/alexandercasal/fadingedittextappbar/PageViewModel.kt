package com.alexandercasal.fadingedittextappbar

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class PageViewModel : ViewModel() {

    val title = MutableLiveData<String>()

    init {
        title.value = ""
    }
}