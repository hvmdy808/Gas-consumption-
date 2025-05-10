package com.hamdymohamed.gasapp





import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class cc : AppCompatActivity() {
    lateinit var ccNum: EditText
    lateinit var gasType: EditText
    lateinit var errorText: TextView

    lateinit var file: SharedPreferences
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

        file = getSharedPreferences("data", MODE_PRIVATE)
        val savedCcNum = file.getString("ccNum", "")
        val savedGasType = file.getString("gasType", "")
        ccNum.setText(savedCcNum)
        gasType.setText(savedGasType)

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
        val editor = file.edit()
        editor.putString("ccNum", ccNum.text.toString())
        editor.putString("gasType", gasType.text.toString())
        editor.apply()
        errorText.setText("")
        //launcher.launch(intent)
        finish()
    }
    override fun onPause() {
        super.onPause()
        val editor = file.edit()
        editor.putString("ccNum", ccNum.text.toString())
        editor.putString("gasType", gasType.text.toString())
        editor.apply()
    }

}