package com.hamdymohamed.gasapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class cc : AppCompatActivity() {
    lateinit var ccNum: EditText
    lateinit var gasType: EditText
    lateinit var errorText: TextView

//    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            errorText.setText("")
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cc)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        ccNum = findViewById(R.id.ccNum)
        gasType = findViewById(R.id.gasType)
        errorText = findViewById(R.id.errorText)
    }

    fun Next(view: View) {
        if(ccNum.text.isNullOrEmpty() || gasType.text.isNullOrEmpty()){
            errorText.setText("You must enter cc and gasoline type numbers")
            return
        }
        if(ccNum.text.toString().toInt()<50 || ccNum.text.toString().toInt()>7000){
            errorText.setText("Invalid cc")
            return
        }
        val gasNum = gasType.text.toString().toInt()
        if(!(gasNum == 80 || gasNum == 92 || gasNum == 95)){
            errorText.setText("Invalid gasoline type")
            return
        }
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("cc", ccNum.text.toString())
        intent.putExtra("gasType", gasType.text.toString())
        errorText.setText("")
        //launcher.launch(intent)
        startActivity(intent)
    }
}