package com.practice.jetpack.androidtask.ui.login

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.practice.jetpack.androidtask.R
import com.practice.jetpack.androidtask.uitlity.SignInTextWatcher
import com.practice.jetpack.androidtask.uitlity.ValidEmailTextWatcher
import com.practice.jetpack.androidtask.uitlity.ValidPasswordTextWatcher
import kotlinx.android.synthetic.main.login_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(), SignInTextWatcher.OnTextChangedListener {

    lateinit var validPasswordTextWatcher: ValidPasswordTextWatcher
    lateinit var validEmailTextWatcher: ValidEmailTextWatcher
    val SUCCESS = 200
    val NETWORK_ERROR = 400
    val WRONG_CREDENTIALS = 401

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        redrawImageView()

        // checking input values of username edittext
        validEmailTextWatcher = ValidEmailTextWatcher( username, user_username,
            getString(R.string.emailNoValidHint), getString(R.string.emailNoValidHint), this )

        username.addTextChangedListener(validEmailTextWatcher)
        username.setOnFocusChangeListener(validEmailTextWatcher)

        // checking input values of password edittext
        validPasswordTextWatcher = ValidPasswordTextWatcher( context!!, password, user_password,
            getString(R.string.passwordHintSignUp), getString(R.string.passwordHintSignUp), this )

        password.addTextChangedListener(validPasswordTextWatcher)
        password.setOnFocusChangeListener(validPasswordTextWatcher)

        button_login.setOnClickListener(View.OnClickListener { view ->
            view.hideKeyboard()
            loginViewModel.login(username.text.toString(), password.text.toString()).observe( viewLifecycleOwner, Observer { code ->
                var msg : String = "Success"
                when(code){
                    SUCCESS -> Navigation.findNavController(view)
                        .navigate(R.id.action_loginFragment_pop_including_mainFragment)
                    NETWORK_ERROR -> msg = getString(R.string.loginCommunicationError)
                    WRONG_CREDENTIALS -> msg = getString(R.string.loginCredentialError)
                }

                val snack = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                snack.show()
            })
        })

    }

    override fun beforeTextChanged(input: TextInputEditText?) {

    }

    override fun onTextChanged(input: TextInputEditText?) {

    }

    override fun afterTextChanged(input: TextInputEditText?) {
        invalidateLoginButton()
    }

    fun redrawImageView(){
        val bitmap: Bitmap = Bitmap.createBitmap(700, 1000, Bitmap.Config.ARGB_8888)
        val canvas: Canvas = Canvas(bitmap)

        var left = 100
        var top = 100
        var right = 600
        var bottom = 800

        // draw oval shape to canvas
        var shapeDrawable: ShapeDrawable = ShapeDrawable(OvalShape())
        shapeDrawable.setBounds( left, top, right, bottom)
        shapeDrawable.getPaint().setColor(Color.parseColor("#3700B3"))
        shapeDrawable.draw(canvas)

        // now bitmap holds the updated pixels

        // set bitmap as background to ImageView
        imageview_login.background = BitmapDrawable(getResources(), bitmap)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    // set Login button state
    fun invalidateLoginButton(){
        button_login.isEnabled = validEmailTextWatcher.hasValidInfo() && validPasswordTextWatcher.hasValidInfo()
    }

}
