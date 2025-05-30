package com.example.mymemo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.widget.EditText
import android.content.SharedPreferences
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun submitRegister(view: View) {
        val usernameText: EditText = findViewById(R.id.registerUsernameText)
        val passwordText: EditText = findViewById(R.id.registerPasswordText)
        val confirmText: EditText = findViewById(R.id.confirmPasswordText)
        val username: String = usernameText.text.toString()
        val password: String = passwordText.text.toString()
        val confirmPassword: String = confirmText.text.toString()
        if (password != confirmPassword) {
            Toast.makeText(this, "两次密码输入不一致，请重新输入", Toast.LENGTH_SHORT).show()
            return
        }
//        var prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val prefs: SharedPreferences = getSharedPreferences("user_information", MODE_PRIVATE)
        val isExist: Boolean = prefs.contains(username)
        if (isExist) {
            Toast.makeText(this, "用户已存在，请重新登录", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            val editor: SharedPreferences.Editor = prefs.edit()
            editor.putString(username, password)
            editor.apply()
            Toast.makeText(this, "注册成功，请继续登录", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}