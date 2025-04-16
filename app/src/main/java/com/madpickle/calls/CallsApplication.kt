package com.madpickle.calls

import android.app.Application
import com.madpickle.calls.domain.CallsLogContentProvider
import com.madpickle.calls.domain.ContactsContentProvider
import com.madpickle.calls.utils.isAllGranted

class CallsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if(this.isAllGranted()) {
            CallsLogContentProvider.getCalls(this.contentResolver)
            ContactsContentProvider.getContacts(this.contentResolver)
        }
    }
}