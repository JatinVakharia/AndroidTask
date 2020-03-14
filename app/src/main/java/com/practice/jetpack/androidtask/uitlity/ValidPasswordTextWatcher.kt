package com.practice.jetpack.androidtask.uitlity

import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.practice.jetpack.androidtask.R

class ValidPasswordTextWatcher(
    private val context: Context,
    input: TextInputEditText?,
    inputLayout: TextInputLayout?,
    errorHint: String?,
    validHint: String?,
    onTextChangedListener: OnTextChangedListener?
) : SignInTextWatcher(input!!, inputLayout, errorHint!!, validHint!!, onTextChangedListener) {
    private var validField = false
    override fun onTextChanged(
        charSequence: CharSequence,
        count: Int,
        i1: Int,
        i2: Int
    ) {
        val password = charSequence.toString().trim { it <= ' ' }
        validField = checkPassword(password) == null
        super.onTextChanged(charSequence, count, i1, i2)
    }

    override fun hasValidInfo(): Boolean {
        return validField
    }

    private fun checkPassword(password: String): String? {
        var err: String? = null
        val password_lenght_error =
            context.resources.getString(R.string.password_lenght_error)
        val password_numbers_letters_error =
            context.resources.getString(R.string.password_numbers_letters_error)
        val password_special_chars_error =
            context.resources.getString(R.string.password_special_chars_error)
        val password_special_chars =
            context.resources.getString(R.string.password_special_chars)
        val password_accepted_chars_error =
            context.resources.getString(R.string.password_accepted_chars_error)
        val password_accepted_chars =
            context.resources.getString(R.string.password_accepted_chars)
        if (password.length < 8 || password.length > 25) {
            err = password_lenght_error
        } else if (!hasOneOf(password, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
            || !hasOneOf(password, "0123456789")
        ) {
            err = password_numbers_letters_error
        } else if (!hasOneOf(password, password_special_chars)) {
            err = password_special_chars_error + "\n" + password_special_chars
        } else if (!doesNotHasOutOf(password, password_accepted_chars)) {
            err = password_accepted_chars_error.replace("ONE_OF", password_accepted_chars)
        }
        return err
    }

    private fun hasOneOf(password: String, template: String): Boolean {
        val passwordCharsArr = password.toCharArray()
        for (oneChar in passwordCharsArr) if (template.indexOf(oneChar) != -1) return true
        return false
    }

    private fun doesNotHasOutOf(password: String, template: String): Boolean {
        val passwordCharsArr = password.toCharArray()
        for (oneChar in passwordCharsArr) if (template.indexOf(oneChar) == -1) return false
        return true
    }

}