package com.example.quiz2

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }


        val userLogin: EditText = findViewById(R.id.user_login_auth)
        val userPassword: EditText = findViewById(R.id.user_password_auth)
        val button: Button = findViewById(R.id.button_auth)
        val linkToReg: TextView = findViewById(R.id.link_to_reg)

        linkToReg.setOnClickListener(){
            val intent = Intent(this, RegActivity::class.java)
            startActivity(intent)
        }


        button.setOnClickListener(){
            val login = userLogin.text.toString().trim()
            val password = userPassword.text.toString().trim()

            if(login == "" || password == ""){
                Toast.makeText(this, "не все поля заполнены", Toast.LENGTH_LONG).show()
            } else{
                val db = DBhelper(this, null)

                val isAuth = db.getUser(login, password);

                if(isAuth){
                    val db = DBhelper(this, null)

                    val admin = db.isAdmin(login, password);

                    if(admin) {
                        val adminDialog = layoutInflater.inflate(R.layout.admin_log_dialog, null)
                        val myDialog = Dialog(this)
                        myDialog.setContentView(adminDialog)
                        myDialog.setCancelable(true)
                        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        myDialog.show()
                        val btnClose = adminDialog.findViewById<Button>(R.id.ok_back)
                        btnClose.setOnClickListener{
                            myDialog.dismiss()
                            val intent = Intent(this, MainForm::class.java)
                            intent.putExtra("admin", admin)
                            startActivity(intent)
                        }
                    } else{
                        Toast.makeText(this, "Пользователь $login авторизован", Toast.LENGTH_LONG).show()
                        userLogin.text.clear()
                        userPassword.text.clear()
                        val intent = Intent(this, MainForm::class.java)
                        startActivity(intent)
                    }
                } else{
                    Toast.makeText(this, "Проверьте вводимые данные", Toast.LENGTH_LONG).show()
                }
            }
        }


    }
}