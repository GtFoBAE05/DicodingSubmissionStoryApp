package com.example.storyapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.ui.login.MainActivity
import com.example.storyapp.utils.Resource
import org.koin.android.ext.android.inject


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel: RegisterViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupAnimation()

        setLoading(false)

        nameValidator()
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
        val registerTv =
            ObjectAnimator.ofFloat(binding.loginPageTv, View.ALPHA, 1f).setDuration(500)
        val name =
            ObjectAnimator.ofFloat(binding.nameTextInputLayout, View.ALPHA, 1f).setDuration(500)
        val email =
            ObjectAnimator.ofFloat(binding.emailTextInputLayout, View.ALPHA, 1f).setDuration(500)
        val password =
            ObjectAnimator.ofFloat(binding.passwordTextInputLayout, View.ALPHA, 1f).setDuration(500)
        val loginBtn = ObjectAnimator.ofFloat(binding.registerBtn, View.ALPHA, 1f).setDuration(500)
        val toRegisterPageBtn =
            ObjectAnimator.ofFloat(binding.navigateToLoginPageBtn, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                imageView,
                registerTv,
                name,
                email,
                password,
                loginBtn,
                toRegisterPageBtn
            )
            start()
        }

    }

    private fun setupObserver() {
        registerViewModel.registerResponse.observe(this) {
            it.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is Resource.Loading -> setLoading(true)

                    is Resource.Error -> {
                        setLoading(false)
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Success -> {
                        setLoading(false)
                        Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun nameValidator() {
        binding.edRegisterName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                validateInputField()
            }

        })
    }

    private fun emailValidator() {
        binding.edRegisterEmail.addTextChangedListener(object : TextWatcher {
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
        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                validateInputField()
            }

        })
    }


    fun validateInputField() {
        val nameET = binding.edRegisterName
        val emailET = binding.edRegisterEmail
        val passwordET = binding.edRegisterPassword
        binding.registerBtn.isEnabled =
            nameET.text.toString().isNotEmpty() && emailET.text.toString()
                .isNotEmpty() && passwordET.text.toString()
                .isNotEmpty() && nameET.error == null && emailET.error == null && passwordET.error == null
    }

    private fun setLoading(bool: Boolean) {
        binding.progressBar.visibility = if (bool) View.VISIBLE else View.GONE
    }

    private fun setupButton() {
        binding.registerBtn.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            registerViewModel.register(name, email, password)
        }


        binding.navigateToLoginPageBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }


}