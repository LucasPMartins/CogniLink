package com.example.cognilink.ui.feature

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    // Common State
    var isSignUpMode by mutableStateOf(false)
        private set

    // Sign In State
    var signInEmail by mutableStateOf("")
        private set
    var signInPassword by mutableStateOf("")
        private set

    // Sign Up State
    var signUpName by mutableStateOf("")
        private set
    var signUpEmail by mutableStateOf("")
        private set
    var signUpPassword by mutableStateOf("")
        private set
    var signUpConfirmPassword by mutableStateOf("")
        private set
    var isTermsAccepted by mutableStateOf(false)
        private set

    fun onModeChange(isSignUp: Boolean) {
        isSignUpMode = isSignUp
    }

    // Sign In Events
    fun onSignInEmailChange(newValue: String) {
        signInEmail = newValue
    }

    fun onSignInPasswordChange(newValue: String) {
        signInPassword = newValue
    }

    fun onSignInClick() {
        // TODO: Implement Login Logic
    }

    // Sign Up Events
    fun onSignUpNameChange(newValue: String) {
        signUpName = newValue
    }

    fun onSignUpEmailChange(newValue: String) {
        signUpEmail = newValue
    }

    fun onSignUpPasswordChange(newValue: String) {
        signUpPassword = newValue
    }

    fun onSignUpConfirmPasswordChange(newValue: String) {
        signUpConfirmPassword = newValue
    }

    fun onTermsAcceptedChange(newValue: Boolean) {
        isTermsAccepted = newValue
    }

    fun onSignUpClick() {
        // TODO: Implement Registration Logic
    }
}
