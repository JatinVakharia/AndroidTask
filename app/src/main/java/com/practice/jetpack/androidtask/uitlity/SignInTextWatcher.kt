package com.practice.jetpack.androidtask.uitlity

import android.graphics.Color
import android.graphics.PorterDuff
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnFocusChangeListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.practice.jetpack.androidtask.R

abstract class SignInTextWatcher
/**
 * Validate text input fields, display hints and change the inputLayout line to highlight the error
 * @param inputEditText
 * @param textInputLayout
 * @param errorHint
 * @param validHint
 * @param onTextChangedListener
 */(
    protected var input: TextInputEditText,
    protected var inputLayout: TextInputLayout?,
    private val errorHint: String,
    private val validHint: String,
    private val onTextChangedListener: OnTextChangedListener?
) : TextWatcher, OnFocusChangeListener {

    interface OnTextChangedListener {
        fun beforeTextChanged(input: TextInputEditText?)
        fun onTextChanged(input: TextInputEditText?)
        fun afterTextChanged(input: TextInputEditText?)
    }

    abstract fun hasValidInfo(): Boolean
    override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {
        onTextChangedListener?.beforeTextChanged(input)
    }

    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        if (input.hasFocus()) {
            if (!hasValidInfo()) {
                inputLayout!!.setErrorTextAppearance(R.style.layout_error_appearance)
                inputLayout!!.error = errorHint
                inputLayout!!.isErrorEnabled = true
                setRedLineColor(true)
            } else {
                inputLayout!!.error = validHint
                inputLayout!!.setErrorTextAppearance(R.style.no_error_appearance)
                inputLayout!!.isErrorEnabled = true
                setRedLineColor(false)
            }
        }
        onTextChangedListener?.onTextChanged(input)
    }

    override fun afterTextChanged(s: Editable) {
        onTextChangedListener?.afterTextChanged(input)
    }

    override fun onFocusChange(
        view: View,
        isFocused: Boolean
    ) {
        if (inputLayout == null) {
            return
        }
        if (input.text.toString().trim { it <= ' ' }.isEmpty()) {
            inputLayout!!.setErrorTextAppearance(R.style.no_error_appearance)
            inputLayout!!.error = validHint
            inputLayout!!.isErrorEnabled = true
            setRedLineColor(false)
        }
        if (!isFocused && hasValidInfo()) {
            inputLayout!!.isErrorEnabled = false
            setRedLineColor(false)
        } else if (!isFocused && !hasValidInfo()) { // if focus is just moved and it does not have valid input show error
            inputLayout!!.setErrorTextAppearance(R.style.layout_error_appearance)
            inputLayout!!.error = errorHint
            inputLayout!!.isErrorEnabled = true
            setRedLineColor(true)
        } else {
            inputLayout!!.isErrorEnabled = true
            setRedLineColor(true)
        }
    }

    private fun setRedLineColor(isRed: Boolean) {
        val drawable = input.background
        if (isRed) {
            drawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP)
        } else {
            drawable.setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP)
        }
        input.setBackgroundDrawable(drawable)
    }

    fun showHint() {
        inputLayout!!.error = validHint
        inputLayout!!.setErrorTextAppearance(R.style.no_error_appearance)
        inputLayout!!.isErrorEnabled = true
        setRedLineColor(false)
    }

}