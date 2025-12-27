package com.example.autoservice

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.autoservice.data.repository.AuthRepository
import com.example.autoservice.ui.main.MainScreenActivity

class MainActivity : AppCompatActivity() {

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etLogin = findViewById<EditText>(R.id.etLogin)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvError = findViewById<TextView>(R.id.tvError)

        btnLogin.setOnClickListener {
            val login = etLogin.text.toString().trim()
            val password = etPassword.text.toString().trim()

            when {
                login.isEmpty() || password.isEmpty() -> {
                    tvError.text = "Пожалуйста, заполните все поля"
                    tvError.visibility = View.VISIBLE
                }

                authRepository.login(login, password) -> {
                    tvError.visibility = View.GONE
                    Toast.makeText(this, "Успешный вход", Toast.LENGTH_SHORT).show()

                    startActivity(
                        Intent(this, MainScreenActivity::class.java)
                    )
                }

                else -> {
                    tvError.text = "Неверный логин или пароль"
                    tvError.visibility = View.VISIBLE
                }
            }
        }
    }
}
