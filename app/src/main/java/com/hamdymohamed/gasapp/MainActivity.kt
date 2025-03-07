package com.hamdymohamed.gasapp

import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mumayank.com.airlocationlibrary.AirLocation

class MainActivity : AppCompatActivity(), AirLocation.Callback {
    lateinit var startLoc: EditText
    lateinit var secLoc: EditText
    lateinit var thrLoc: EditText
    lateinit var fouLoc: EditText
    lateinit var checkReturn: CheckBox
    lateinit var calcButton: Button
    lateinit var airLoc: AirLocation
    lateinit var showButton: Button
    lateinit var errorTxt: TextView
    lateinit var resultText: TextView
    var cc: Int? = null
    var gasType: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        startLoc = findViewById(R.id.startLoc)
        secLoc = findViewById(R.id.secLoc)
        thrLoc = findViewById(R.id.thrLoc)
        fouLoc = findViewById(R.id.fouLoc)
        checkReturn = findViewById(R.id.checkReturn)
        calcButton = findViewById(R.id.calcButton)
        showButton = findViewById(R.id.showButton)
        errorTxt = findViewById(R.id.errorTxt)
        resultText = findViewById(R.id.resultText)
        cc = intent.getStringExtra("cc").toString().toInt()
        gasType = intent.getStringExtra("gasType").toString().toInt()
        startLoc.isEnabled = false
        secLoc.isEnabled = false
        thrLoc.isEnabled = false
        fouLoc.isEnabled = false
        checkReturn.isEnabled = false
        calcButton.isEnabled = false
        showButton.isEnabled = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        airLoc.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        airLoc.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun Calculate(view: View) {
        errorTxt.text=""
        var result = ""
        var totalDistance: Float = 0.0F
        var smthngWrong: Boolean =false
        val dist1 : Float
        val dist2 : Float
        val dist3 : Float
        val dist4 : Float
        val add1 = "${startLoc.text} Egypt"
        val add2 = "${secLoc.text} Egypt"
        val add3 = "${thrLoc.text} Egypt"
        val add4 = "${fouLoc.text} Egypt"
        if(add1.isNullOrEmpty() || add1 == " Egypt"){
            errorTxt.append("Add a valid starting location. ")
            resultText.text =""
            return
        }
        if(add2.isNullOrEmpty() || add2 == " Egypt"){
            errorTxt.append("Add a valid second location. ")
            resultText.text =""
            return
        }
        if(add3.isNullOrEmpty() || add3 == " Egypt"){
            errorTxt.append("Add a valid third location. ")
            resultText.text =""
            return
        }
        if(add4.isNullOrEmpty() || add4 == " Egypt"){
            errorTxt.append("Add a valid fourth location. ")
            resultText.text =""
            return
        }
        val geoCoder = Geocoder(this)
        val addList1 = geoCoder.getFromLocationName(add1, 1)
        if(!addList1.isNullOrEmpty()){
            val loc1 = Location("")
            loc1.latitude = addList1[0].latitude
            loc1.longitude = addList1[0].longitude

            val addList2 = geoCoder.getFromLocationName(add2, 1)
            if(!addList2.isNullOrEmpty()){
                val loc2 = Location("")
                loc2.latitude = addList2[0].latitude
                loc2.longitude = addList2[0].longitude
                dist1 = loc1.distanceTo(loc2)/1000
                totalDistance+=dist1
                result+="Distance between $add1 to $add2 =$dist1 km"

                val addList3 = geoCoder.getFromLocationName(add3, 1)
                if(!addList3.isNullOrEmpty()){
                    val loc3 = Location("")
                    loc3.latitude = addList3[0].latitude
                    loc3.longitude = addList3[0].longitude
                    dist2 = loc2.distanceTo(loc3)/1000
                    totalDistance+=dist2
                    result+="\nDistance between $add2 to $add3 =$dist2 km"

                    val addList4 = geoCoder.getFromLocationName(add4, 1)
                    if (!addList4.isNullOrEmpty()){
                        val loc4 = Location("")
                        loc4.latitude = addList4[0].latitude
                        loc4.longitude = addList4[0].longitude
                        dist3 = loc3.distanceTo(loc4)/ 1000
                        totalDistance+=dist3
                        result+= "\nDistance between $add3 to $add4 =$dist3 km"
                        if(checkReturn.isChecked){
                            dist4 = loc4.distanceTo(loc1) / 1000
                            totalDistance+=dist4
                            result+= "\nDistance between $add4 to $add1 =$dist4 km"
                        }
                    }else{
                        result = "The fourth location is not valid"
                        smthngWrong = true
                    }

                }else{
                    result = "The third location is not valid"
                    smthngWrong = true
                }

            }else{
                result = "The second location is not valid"
                smthngWrong = true
            }

        }else{
            result = "The first location is not valid"
            smthngWrong = true
        }

        if(!smthngWrong){
            val cost: Float
            if(gasType == 80){
                cost = 13.75F
            }
            else if(gasType == 92){
                cost = 15.25F
            }
            else{
                cost = 17F
            }
            resultText.text = result
            resultText.append("\nApproximate total distance = ${totalDistance}")
            if(cc != null && gasType != null){
                if(cc!! in 50 ..500){
                    resultText.append("\nApproximate gasoline needed: ${(2*totalDistance)/100} - ${(4*totalDistance)/100} L")
                    resultText.append("\nApproximate fuel cost needed: ${(2*totalDistance*cost)/100} - ${(4*totalDistance*cost)/100} LE")
                }
                else if(cc!! in 501 .. 1500){
                    resultText.append("\nApproximate gasoline needed: ${(4*totalDistance)/100} - ${(8*totalDistance)/100} L")
                    resultText.append("\nApproximate fuel cost needed: ${(4*totalDistance*cost)/100} - ${(8*totalDistance*cost)/100} LE")
                }
                else if(cc!! in 1501 .. 2000){
                    resultText.append("\nApproximate gasoline needed: ${(8*totalDistance)/100} - ${(10*totalDistance)/100} L")
                    resultText.append("\nApproximate fuel cost needed: ${(8*totalDistance*cost)/100} - ${(10*totalDistance*cost)/100} LE")
                }
                else if(cc!! in 2001 .. 4000){
                    resultText.append("\nApproximate gasoline needed: ${(10*totalDistance)/100} - ${(14*totalDistance)/100} L")
                    resultText.append("\nApproximate fuel cost needed: ${(10*totalDistance*cost)/100} - ${(14*totalDistance*cost)/100} LE")
                }
                else if(cc!! in 4001 .. 6000){
                    resultText.append("\nApproximate gasoline needed: ${(14*totalDistance)/100} - ${(19*totalDistance)/100} L")
                    resultText.append("\nApproximate fuel cost needed: ${(14*totalDistance*cost)/100} - ${(19*totalDistance*cost)/100} LE")
                }
                else if(cc!! in 6001 .. 7000){
                    resultText.append("\nApproximate gasoline needed: ${(19*totalDistance)/100} - ${(25*totalDistance)/100} L")
                    resultText.append("\nApproximate fuel cost needed: ${(19*totalDistance*cost)/100} - ${(25*totalDistance*cost)/100} LE")
                }

            }

        }else{
            resultText.text = result
        }
    }

    override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {
        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(locations: ArrayList<Location>) {
        val lat=locations[0].latitude
        val lon=locations[0].longitude

        val geocoder= Geocoder(this)
        val addressList=geocoder.getFromLocation(lat,lon,1)
        if(!addressList.isNullOrEmpty()){
            startLoc.append("\n${addressList[0].getAddressLine(0)}")
        }
    }

    fun Show(view: View) {
        airLoc = AirLocation(this, this, true)
        airLoc.start()
        startLoc.isEnabled = true
        secLoc.isEnabled = true
        thrLoc.isEnabled = true
        fouLoc.isEnabled = true
        checkReturn.isEnabled = true
        calcButton.isEnabled = true
        showButton.isEnabled = false
    }
}