package com.hamdymohamed.gasapp



import android.content.Intent
import android.content.SharedPreferences
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
    lateinit var detailsButton: Button
    lateinit var checkBstRoute: CheckBox
    lateinit var bstRouteButton: Button
    var cc: Int? = null
    var gasType: Int? = null
    lateinit var file: SharedPreferences

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
        detailsButton = findViewById(R.id.detailsButton)
        bstRouteButton = findViewById(R.id.bstRouteButton)
        checkBstRoute = findViewById(R.id.checkBstRoute)

        val ccExtra = intent.getStringExtra("cc")
        val gasTypeExtra = intent.getStringExtra("gasType")

        if (ccExtra != null && gasTypeExtra != null) {
            cc = ccExtra.toInt()
            gasType = gasTypeExtra.toInt()
        } else {
            // Retrieve values from SharedPreferences as fallback
            file = getSharedPreferences("data", MODE_PRIVATE)
            val savedCc = file.getString("ccNum", null)
            val savedGasType = file.getString("gasType", null)

            if (savedCc != null && savedGasType != null) {
                cc = savedCc.toInt()
                gasType = savedGasType.toInt()
            } else {

                Toast.makeText(
                    this,
                    "No engine data available. Please enter your cc and gas type.",
                    Toast.LENGTH_LONG
                ).show()

            }
        }


        startLoc.isEnabled = false
        secLoc.isEnabled = false
        thrLoc.isEnabled = false
        fouLoc.isEnabled = false
        checkReturn.isEnabled = false
        calcButton.isEnabled = false
        showButton.isEnabled = true
        detailsButton.isEnabled = false
        checkBstRoute.isEnabled = false
        bstRouteButton.isEnabled = false
        bstRouteButton.isEnabled = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        airLoc.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        airLoc.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun Calculate(view: View) {
        errorTxt.text = ""
        var result = ""
        var totalDistance: Float = 0.0F
        var smthngWrong: Boolean = false
        val dist1: Float
        val dist2: Float
        val dist3: Float
        val dist4: Float
        val add1 = "${startLoc.text} Egypt"
        val add2 = "${secLoc.text} Egypt"
        var add3 = "${thrLoc.text} Egypt"
        var add4 = "${fouLoc.text} Egypt"
        val placesEntered = mutableListOf<String>()
        if (add1.isNullOrEmpty() || add1 == " Egypt") {
            errorTxt.append("Add a valid starting location. ")
            resultText.text = ""
            return
        } else {
            placesEntered.add(add1)
        }
        if (add2.isNullOrEmpty() || add2 == " Egypt") {
            errorTxt.append("Add a valid second location. ")
            resultText.text = ""
            return
        }
        if (placesEntered.contains(add2)) {
            errorTxt.append("Enter a different second location")
            return
        } else {
            placesEntered.add(add2)
        }

        if (!(add3.isNullOrEmpty() || add3 == " Egypt")) {
            if (placesEntered.contains(add3)) {
                errorTxt.append("Enter a different third location")
                return
            } else {
                placesEntered.add(add3)
            }
        }

        if (!(add4.isNullOrEmpty() || add4 == " Egypt")) {
            if (placesEntered.contains(add4)) {
                errorTxt.append("Enter a different fourth location")
                return
            } else {
                placesEntered.add(add4)
            }
        }
        if(add4 != " Egypt" && add3 == " Egypt"){
            add3 = add4
            add4 = ""
        }
        val geoCoder = Geocoder(this)
        val addList1 = geoCoder.getFromLocationName(add1, 1)
        if (!addList1.isNullOrEmpty()) {
            if(addList1[0].latitude == 26.820553 && addList1[0].longitude == 30.802498000000003){
                result = "The first location is not valid"
                errorTxt.text = result
                smthngWrong = false
                detailsButton.isEnabled = false
                return
            }
            val loc1 = Location("")
            loc1.latitude = addList1[0].latitude
            loc1.longitude = addList1[0].longitude
            println("${loc1.latitude} -- ${loc1.longitude}")

            val addList2 = geoCoder.getFromLocationName(add2, 1)
            if (!addList2.isNullOrEmpty()) {
                if(addList2[0].latitude == 26.820553 && addList2[0].longitude == 30.802498000000003){
                    result = "The second location is not valid"
                    errorTxt.text = result
                    smthngWrong = false
                    detailsButton.isEnabled = false
                    return
                }
                val loc2 = Location("")
                loc2.latitude = addList2[0].latitude
                loc2.longitude = addList2[0].longitude
                println("${loc2.latitude} -- ${loc2.longitude}")
                dist1 = loc1.distanceTo(loc2) / 1000
                totalDistance += dist1
                var lastLoc = loc2

                if (!(add3.isNullOrEmpty() || add3 == " Egypt")) {
                    val addList3 = geoCoder.getFromLocationName(add3, 1)
                    if (!addList3.isNullOrEmpty()) {
                        if(addList3[0].latitude == 26.820553 && addList3[0].longitude == 30.802498000000003){
                            result = "The third location is not valid"
                            errorTxt.text = result
                            smthngWrong = false
                            detailsButton.isEnabled = false
                            return
                        }
                        val loc3 = Location("")
                        loc3.latitude = addList3[0].latitude
                        loc3.longitude = addList3[0].longitude
                        dist2 = loc2.distanceTo(loc3) / 1000
                        totalDistance += dist2
                        lastLoc = loc3

                        if (!(add4.isNullOrEmpty() || add4 == " Egypt")) {
                            val addList4 = geoCoder.getFromLocationName(add4, 1)
                            if (!addList4.isNullOrEmpty()) {
                                if(addList4[0].latitude == 26.820553 && addList4[0].longitude == 30.802498000000003){
                                    result = "The fourth location is not valid"
                                    errorTxt.text = result
                                    smthngWrong = false
                                    detailsButton.isEnabled = false
                                    return
                                }
                                val loc4 = Location("")
                                loc4.latitude = addList4[0].latitude
                                loc4.longitude = addList4[0].longitude
                                dist3 = loc3.distanceTo(loc4) / 1000
                                totalDistance += dist3
                                lastLoc = loc4
                            } else {
                                result = "The fourth location is not valid"
                                smthngWrong = true
                            }
                        }
                    } else {
                        result = "The third location is not valid"
                        smthngWrong = true
                    }
                }
                if (checkReturn.isChecked) {
                    dist4 = lastLoc.distanceTo(loc1) / 1000
                    totalDistance += dist4
                }
            } else {
                result = "The second location is not valid"
                smthngWrong = true
            }
        } else {
            result = "The first location is not valid"
            smthngWrong = true
        }
        if (checkBstRoute.isChecked && !(add3.isNullOrEmpty() || add3 == " Egypt")) {
            println("fff")
            println(add3)
            bstRouteButton.isEnabled = true
        }else{
            println("ggg")
            bstRouteButton.isEnabled = false
        }

        if (!smthngWrong) {
            detailsButton.isEnabled = true
            val cost: Float = if (gasType == 80) {
                13.75F
            } else if (gasType == 92) {
                15.25F
            } else {
                17F
            }
            resultText.text = result
            resultText.append("\nApproximate total distance = ${totalDistance} Km")
            if (cc != null && gasType != null) {
                if (cc!! in 50..500) {
                    resultText.append("\nApproximate gasoline needed: ${(2 * totalDistance) / 100} - ${(4 * totalDistance) / 100} L")
                    resultText.append("\nApproximate fuel cost needed: ${(2 * totalDistance * cost) / 100} - ${(4 * totalDistance * cost) / 100} LE")
                } else if (cc!! in 501..1500) {
                    resultText.append("\nApproximate gasoline needed: ${(4 * totalDistance) / 100} - ${(8 * totalDistance) / 100} L")
                    resultText.append("\nApproximate fuel cost needed: ${(4 * totalDistance * cost) / 100} - ${(8 * totalDistance * cost) / 100} LE")
                } else if (cc!! in 1501..2000) {
                    resultText.append("\nApproximate gasoline needed: ${(8 * totalDistance) / 100} - ${(10 * totalDistance) / 100} L")
                    resultText.append("\nApproximate fuel cost needed: ${(8 * totalDistance * cost) / 100} - ${(10 * totalDistance * cost) / 100} LE")
                } else if (cc!! in 2001..4000) {
                    resultText.append("\nApproximate gasoline needed: ${(10 * totalDistance) / 100} - ${(14 * totalDistance) / 100} L")
                    resultText.append("\nApproximate fuel cost needed: ${(10 * totalDistance * cost) / 100} - ${(14 * totalDistance * cost) / 100} LE")
                } else if (cc!! in 4001..6000) {
                    resultText.append("\nApproximate gasoline needed: ${(14 * totalDistance) / 100} - ${(19 * totalDistance) / 100} L")
                    resultText.append("\nApproximate fuel cost needed: ${(14 * totalDistance * cost) / 100} - ${(19 * totalDistance * cost) / 100} LE")
                } else if (cc!! in 6001..7000) {
                    resultText.append("\nApproximate gasoline needed: ${(19 * totalDistance) / 100} - ${(25 * totalDistance) / 100} L")
                    resultText.append("\nApproximate fuel cost needed: ${(19 * totalDistance * cost) / 100} - ${(25 * totalDistance * cost) / 100} LE")
                }
            }
        } else {
            resultText.text = result
            detailsButton.isEnabled = false
        }
        detailsButton.isEnabled = true
    }

    override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {
        Toast.makeText(this, "failed", Toast.LENGTH_LONG).show()
    }

    override fun onSuccess(locations: ArrayList<Location>) {
        val lat = locations[0].latitude
        val lon = locations[0].longitude

        val geocoder = Geocoder(this)
        val addressList = geocoder.getFromLocation(lat, lon, 1)
        if (!addressList.isNullOrEmpty()) {
            startLoc.setText("${addressList[0].getAddressLine(0)}")
        }
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("data", MODE_PRIVATE)
        val ccNum = prefs.getString("ccNum", null)
        val gas = prefs.getString("gasType", null)
        cc = ccNum.toString().toInt()
        gasType = gas.toString().toInt()
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
        //showButton.isEnabled = false
        checkBstRoute.isEnabled = true
    }

    fun showDetails(view: View) {
        var result = ""
        var totalDistance: Float = 0.0F
        var smthngWrong: Boolean = false
        val dist1: Float
        val dist2: Float
        val dist3: Float
        val dist4: Float
        val add1 = "${startLoc.text} Egypt"
        val add2 = "${secLoc.text} Egypt"
        var add3 = "${thrLoc.text} Egypt"
        var add4 = "${fouLoc.text} Egypt"
        val placesEntered = mutableListOf("")
        if (add1.isNullOrEmpty() || add1 == " Egypt") {
            errorTxt.append("Add a valid starting location. ")
            resultText.text = ""
            return
        } else {
            placesEntered.add(add1)
        }
        if (add2.isNullOrEmpty() || add2 == " Egypt") {
            errorTxt.append("Add a valid second location. ")
            resultText.text = ""
            return
        }
        if (placesEntered.contains(add2)) {
            errorTxt.append("Enter a different second location")
            return
        } else {
            placesEntered.add(add2)
        }

        if (!(add3.isNullOrEmpty() || add3 == " Egypt")) {
            if (placesEntered.contains(add3)) {
                errorTxt.append("Enter a different third location")
                return
            } else {
                placesEntered.add(add3)
            }
        }

        if (!(add4.isNullOrEmpty() || add4 == " Egypt")) {
            if (placesEntered.contains(add4)) {
                errorTxt.append("Enter a different fourth location")
                return
            } else {
                placesEntered.add(add4)
            }
        }

        if(add4 != " Egypt" && add3 == " Egypt"){
            add3 = add4
            add4 = " Egypt"
        }

        val geoCoder = Geocoder(this)
        val addList1 = geoCoder.getFromLocationName(add1, 1)
        if (!addList1.isNullOrEmpty()) {
            val loc1 = Location("")
            loc1.latitude = addList1[0].latitude
            loc1.longitude = addList1[0].longitude

            val addList2 = geoCoder.getFromLocationName(add2, 1)
            if (!addList2.isNullOrEmpty()) {
                val loc2 = Location("")
                loc2.latitude = addList2[0].latitude
                loc2.longitude = addList2[0].longitude
                dist1 = loc1.distanceTo(loc2) / 1000
                result += "Distance between ${startLoc.text} to ${secLoc.text} =$dist1 km"
                var lastLoc = add2
                if (!(add3.isNullOrEmpty() || add3 == " Egypt")) {
                    val addList3 = geoCoder.getFromLocationName(add3, 1)
                    if (!addList3.isNullOrEmpty()) {
                        val loc3 = Location("")
                        loc3.latitude = addList3[0].latitude
                        loc3.longitude = addList3[0].longitude
                        dist2 = loc2.distanceTo(loc3) / 1000
                        result += "\nDistance between ${secLoc.text} to ${add3} =$dist2 km"
                        lastLoc = add3
                        if (!(add4.isNullOrEmpty() || add4 == " Egypt")) {
                            val addList4 = geoCoder.getFromLocationName(add4, 1)
                            if (!addList4.isNullOrEmpty()) {
                                val loc4 = Location("")
                                loc4.latitude = addList4[0].latitude
                                loc4.longitude = addList4[0].longitude
                                dist3 = loc3.distanceTo(loc4) / 1000
                                result += "\nDistance between ${add3} to ${add4} =$dist3 km"
                                lastLoc = add4
                                if (checkReturn.isChecked) {
                                    dist4 = loc4.distanceTo(loc1) / 1000
                                    result += "\nDistance between ${fouLoc.text} to ${startLoc.text} =$dist4 km"
                                }
                            } else {
                                result = "The fourth location is not valid"
                                smthngWrong = true
                            }
                        } else {
                            if (checkReturn.isChecked) {
                                dist4 = loc3.distanceTo(loc1) / 1000
                                result += "\nDistance between ${lastLoc} to ${startLoc.text} =$dist4 km"
                            }
                        }
                    } else {
                        result = "The third location is not valid"
                        smthngWrong = true
                    }
                } else {
                    if (checkReturn.isChecked) {
                        dist4 = loc2.distanceTo(loc1) / 1000
                        result += "\nDistance between ${secLoc.text} to ${startLoc.text} =$dist4 km"
                    }
                }
            } else {
                result = "The second location is not valid"
                smthngWrong = true
            }
        } else {
            result = "The first location is not valid"
            smthngWrong = true
        }

        if (!smthngWrong) {
            resultText.text = result
        } else {
            resultText.text = result
        }
    }


    fun setting(view: View) {
        val intent = Intent(this, com.hamdymohamed.gasapp.cc::class.java)
        startActivity(intent)
    }

    fun showBstRoute(view: View) {
        detailsButton.isEnabled= false
        errorTxt.text = ""

        // Gather addresses with " Egypt" appended.
        val addresses = ArrayList<String>()
        val addr1 = startLoc.text.toString().trim() + " Egypt"
        if (addr1.trim() == "Egypt") {
            errorTxt.text = "Add a valid starting location."
            resultText.text = ""
            return
        }
        addresses.add(addr1)

        val addr2 = secLoc.text.toString().trim()
        if (addr2.isNotEmpty() && addr2 != "Egypt") {
            addresses.add(addr2 + " Egypt")
        }

        val addr3 = thrLoc.text.toString().trim()
        if (addr3.isNotEmpty() && addr3 != "Egypt") {
            addresses.add(addr3 + " Egypt")
        }

        val addr4 = fouLoc.text.toString().trim()
        if (addr4.isNotEmpty() && addr4 != "Egypt") {
            addresses.add(addr4 + " Egypt")
        }

        // Convert addresses to Location objects using Geocoder.
        val geoCoder = Geocoder(this)
        val locations = ArrayList<Location>()
        for (addr in addresses) {
            val results = geoCoder.getFromLocationName(addr, 1)
            if (results == null || results.isEmpty()) {
                errorTxt.text = "Invalid address: $addr"
                resultText.text = ""
                return
            }
            val loc = Location("")
            loc.latitude = results[0].latitude
            loc.longitude = results[0].longitude
            locations.add(loc)
        }

        // Use a simple greedy approach (nearest neighbor) starting at the first location.
        var currentIndex = 0
        var routeDesc = ""
        // Remove the appended " Egypt" when displaying.
        routeDesc += addresses[0].replace(" Egypt", "")
        var totalDistance = 0.0

        // Build a list of unvisited location indices.
        val unvisited = ArrayList<Int>()
        for (i in 1 until locations.size) {
            unvisited.add(i)
        }

        // While there are still unvisited locations...
        while (unvisited.size > 0) {
            var nearestIndex = -1
            var nearestDistance = Double.MAX_VALUE
            val currentLoc = locations[currentIndex]

            // Find the nearest unvisited location.
            for (i in unvisited) {
                val d = currentLoc.distanceTo(locations[i]).toDouble() / 1000.0 // in km
                if (d < nearestDistance) {
                    nearestDistance = d
                    nearestIndex = i
                }
            }

            // Add leg distance and update route description.
            totalDistance += nearestDistance
            routeDesc += " -> (" + String.format("%.2f", nearestDistance) + " km) -> " +
                    addresses[nearestIndex].replace(" Egypt", "")
            currentIndex = nearestIndex
            unvisited.remove(nearestIndex)
        }

        // If the return checkbox is checked, add the leg back to the start.
        if (checkReturn.isChecked) {
            val returnDistance = locations[currentIndex].distanceTo(locations[0]).toDouble() / 1000.0
            totalDistance += returnDistance
            routeDesc += " -> (" + String.format("%.2f", returnDistance) + " km) -> " +
                    addresses[0].replace(" Egypt", "")
        }

        routeDesc += "\nTotal Distance: " + String.format("%.2f", totalDistance) + " km"

        // Estimate fuel consumption and cost similar to the Calculate method.
        if (cc != null && gasType != null) {
            val costPerLiter: Float = when (gasType) {
                80 -> 13.75F
                92 -> 15.25F
                else -> 17F
            }
            val totalDistanceFloat = totalDistance.toFloat()
            var fuelLower = 0F
            var fuelUpper = 0F

            if (cc!! in 50..500) {
                fuelLower = (2 * totalDistanceFloat) / 100
                fuelUpper = (4 * totalDistanceFloat) / 100
            } else if (cc!! in 501..1500) {
                fuelLower = (4 * totalDistanceFloat) / 100
                fuelUpper = (8 * totalDistanceFloat) / 100
            } else if (cc!! in 1501..2000) {
                fuelLower = (8 * totalDistanceFloat) / 100
                fuelUpper = (10 * totalDistanceFloat) / 100
            } else if (cc!! in 2001..4000) {
                fuelLower = (10 * totalDistanceFloat) / 100
                fuelUpper = (14 * totalDistanceFloat) / 100
            } else if (cc!! in 4001..6000) {
                fuelLower = (14 * totalDistanceFloat) / 100
                fuelUpper = (19 * totalDistanceFloat) / 100
            } else if (cc!! in 6001..7000) {
                fuelLower = (19 * totalDistanceFloat) / 100
                fuelUpper = (25 * totalDistanceFloat) / 100
            }
            val costLower = fuelLower * costPerLiter
            val costUpper = fuelUpper * costPerLiter
            routeDesc += "\nEstimated fuel consumption: " + String.format("%.2f", fuelLower) +
                    " - " + String.format("%.2f", fuelUpper) + " L"
            routeDesc += "\nEstimated fuel cost: " + String.format("%.2f", costLower) +
                    " - " + String.format("%.2f", costUpper) + " LE"
        }

        resultText.text = routeDesc

    }
}
