package com.mongoose.inputvalidation.ui.login

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class LoginViewModel : ViewModel() {

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

    fun start() {
        disposables.add(
            Observable.combineLatest(
                isNameValid(),
                isPasswordValid(),
                { a: Any, b: Any -> "biFun call $a, $b" }
            ).subscribe { v: String ->
                println("Combined latest: $v")
            }
        )
    }

    private val emailSubject: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    fun setEmailSubject(value: String) {
        if (emailSubject.value != value) { // Avoids infinite loops.
            emailSubject.onNext(value)
        }
    }

    private val passwordSubject: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    fun setPasswordSubject(value: String) {
        if (passwordSubject.value != value) { // Avoids infinite loops.
            passwordSubject.onNext(value)
        }
    }

    private fun isNameValid(): Observable<Boolean> {
        return emailSubject.map { str ->
            !TextUtils.isEmpty(str)
        }
    }

    private fun isPasswordValid(): Observable<Boolean> {
        return passwordSubject.map { str ->
            str.length > 5
        }
    }
}