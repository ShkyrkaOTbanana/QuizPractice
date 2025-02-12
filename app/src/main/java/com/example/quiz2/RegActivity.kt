    package com.example.quiz2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reg)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        fun isValidLogin(login: String): Boolean{
//            if (login.length < 4 || login.length > 16)
//                return false
//        }

        fun isValidPassword(password: String): Boolean {
            // Проверяем длину пароля
            if (password.length < 4 || password.length > 16) {
                return false
            }

            // Проверяем наличие запрещенных символов
            val forbiddenCharacters = setOf('*', '&', '{', '}', '|', '+')
            if (password.any { it in forbiddenCharacters }) {
                return false
            }

            // Проверяем наличие заглавных букв и цифр
            val hasUpperCase = password.any { it.isUpperCase() }
            val hasDigit = password.any { it.isDigit() }

            return hasUpperCase && hasDigit
        }

        val userLogin: EditText = findViewById(R.id.user_login)
        val userEmail: EditText = findViewById(R.id.user_email)
        val userPassword: EditText = findViewById(R.id.user_password)
        val button: Button = findViewById(R.id.button_reg)

        val linkToAuth: TextView = findViewById(R.id.link_to_auth)


        linkToAuth.setOnClickListener(){
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val password = userPassword.text.toString().trim()



            if(login == "" || email == "" || password == "")
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            else if (email.contains("@")){
                if (isValidPassword(password)) {
                    val user = User(login, email, password)

                    val db = DBhelper(this, null)
                    db.addUser(user)
                    Toast.makeText(this, "Пользователь $login добавлен", Toast.LENGTH_LONG).show()

                    userLogin.text.clear()
                    userEmail.text.clear()
                    userPassword.text.clear()

                    val intent = Intent(this, AuthActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "Пароль не соответсвует требованиям", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Неверный формат email", Toast.LENGTH_LONG).show()
            }
        }
    }
}