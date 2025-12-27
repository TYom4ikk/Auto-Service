package com.example.autoservice.data.repository

import com.example.autoservice.data.model.User

class AuthRepository {

    private val adminUser = User(
        email = "admin@gmail.com",
        password = "admin123",
        city = "Екатеринбург",
        name = "Админ",
    )

    fun login(email: String, password: String): Boolean {
        return email == adminUser.email && password == adminUser.password
    }
    fun getCurrentUser(): User {
        return adminUser
    }
}