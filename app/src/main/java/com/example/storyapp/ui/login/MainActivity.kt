package com.example.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.home.HomeActivity
import com.example.storyapp.ui.register.RegisterActivity
import com.example.storyapp.utils.Resource
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val loginViewModel: LoginViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupAnimation()

        setLoading(false)

        emailValidator()
        passwordValidator()
        setupButton()
        validateInputField()
        setupObserver()

    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 5000
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }.start()

        val imageView = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(500)
        val loginTv = ObjectAnimator.ofFloat(binding.loginPageTv, View.ALPHA, 1f).setDuration(500)
        val email =
            ObjectAnimator.ofFloat(binding.emailTextInputLayout, View.ALPHA, 1f).setDuration(500)
        val password =
            ObjectAnimator.ofFloat(binding.passwordTextInputLayout, View.ALPHA, 1f).setDuration(500)
        val loginBtn = ObjectAnimator.ofFloat(binding.loginBtn, View.ALPHA, 1f).setDuration(500)
        val toRegisterPageBtn =
            ObjectAnimator.ofFloat(binding.navigateToRegisterPageBtn, View.ALPHA, 1f)
                .setDuration(500)

        AnimatorSet().apply {
            playSequentially(imageView, loginTv, email, password, loginBtn, toRegisterPageBtn)
            start()
        }

    }

    private fun setupObserver() {
        loginViewModel.userLogin.observe(this) {
            it.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is Resource.Loading -> setLoading(true)

                    is Resource.Error -> {
                        setLoading(false)
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Success -> {

                        loginViewModel.saveId(result.data.loginResult.userId)
                        loginViewModel.saveName(result.data.loginResult.name)
                        loginViewModel.saveToken(result.data.loginResult.token)

                        val intent = Intent(this, HomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }


    private fun emailValidator() {
        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                validateInputField()
            }

        })
    }

    private fun passwordValidator() {
        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                validateInputField()
            }

        })
    }


    private fun setLoading(bool: Boolean) {
        binding.loginProgressBar.visibility = if (bool) View.VISIBLE else View.GONE
    }

    fun validateInputField() {
        val emailET = binding.edLoginEmail
        val passwordET = binding.edLoginPassword
        binding.loginBtn.isEnabled =
            emailET.text.toString().isNotEmpty() && passwordET.text.toString()
                .isNotEmpty() && emailET.error == null && passwordET.error == null
    }

    private fun setupButton() {
        binding.loginBtn.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            loginViewModel.login(email, password)
        }

        binding.navigateToRegisterPageBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

}