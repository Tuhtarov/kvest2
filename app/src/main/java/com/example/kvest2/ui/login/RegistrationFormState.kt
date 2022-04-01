package com.example.kvest2.ui.login

/**
 * Data validation state of the login form.
 */
data class RegistrationFormState (val loginError: Int? = null,
                           val passwordError: Int? = null,
                           val nameError: Int? = null,
                           val phoneError: Int? = null,
                           val isDataValid: Boolean = false)

