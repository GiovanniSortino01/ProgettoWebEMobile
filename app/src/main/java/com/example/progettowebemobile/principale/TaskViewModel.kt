package com.example.progettowebemobile.principale

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskViewModel: ViewModel() {
    var categoria1 = MutableLiveData<String>()
    var categoria2 = MutableLiveData<String>()
    var categoria3 = MutableLiveData<String>()
}