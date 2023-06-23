package com.example.progettowebemobile.principale

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskViewModel: ViewModel() {
    var camere = MutableLiveData<String>()
    var adulti = MutableLiveData<String>()
    var bambini = MutableLiveData<String>()
}