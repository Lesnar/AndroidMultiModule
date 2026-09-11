package com.jemis.multi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jemis.business_a.BusinessAActivity as BusinessAMainActivity
import com.jemis.business_b.MainActivity as BusinessBMainActivity
import com.jemis.multi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonBusinessA.setOnClickListener {
            startActivity(Intent(this, BusinessAMainActivity::class.java))
        }
        binding.buttonBusinessB.setOnClickListener {
            startActivity(Intent(this, BusinessBMainActivity::class.java))
        }
    }
}