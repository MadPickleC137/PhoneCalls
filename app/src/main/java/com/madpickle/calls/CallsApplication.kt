package com.madpickle.calls

import android.app.Application
import com.madpickle.calls.domain.CallsLogContentProvider
import com.madpickle.calls.domain.LoadContactsUseCase
import com.madpickle.calls.utils.isAllGranted

class CallsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if(this.isAllGranted()) {
            CallsLogContentProvider.getCalls(this.contentResolver)
            LoadContactsUseCase.getContacts(this)
        }
    }
}