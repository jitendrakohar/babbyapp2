package com.example.babbyapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.yourapp.LoginManager

class splashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

       var loginStatus= LoginManager.getLoginStatus(this@splashActivity);
        if(loginStatus){
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 2000)
        }
        else {
            Handler().postDelayed({
                startActivity(Intent(this, loginLogout::class.java))
                finish()
            }, 2000)
        }
    }
}