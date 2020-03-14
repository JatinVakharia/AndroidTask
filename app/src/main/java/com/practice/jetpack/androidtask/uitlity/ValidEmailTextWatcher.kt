package com.practice.jetpack.androidtask.uitlity

import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ValidEmailTextWatcher(
    private val inputEditText: TextInputEditText,
    inputLayout: TextInputLayout?,
    errorHint: String?,
    validHint: String?,
    onTextChangedListener: OnTextChangedListener?
) : SignInTextWatcher(inputEditText, inputLayout, errorHint!!, validHint!!, onTextChangedListener) {
    private var validField = false
    override fun onTextChanged(
        charSequence: CharSequence,
        count: Int,
        i1: Int,
        i2: Int
    ) { //        String email = charSequence.toString().trim();
        var email = charSequence.toString()
        if (email.contains(" ")) {
            email = email.replace(" ", "")
            inputEditText.setText(email)
            if (email.length > 1) inputEditText.setSelection(email.length)
        }
        //        email = email.replaceAll(" ", "");
        validField = if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            false
        } else {
            true
        }
        super.onTextChanged(charSequence, count, i1, i2)
    }

    override fun hasValidInfo(): Boolean {
        return validField
    }

}