package com.example.kvest2.data.model

import androidx.lifecycle.MutableLiveData
import com.example.kvest2.data.entity.User

object AppUserSingleton {
    var user = MutableLiveData<User?>()
}