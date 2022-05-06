package dk.itu.moapd.scootersharing.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.Result
import dk.itu.moapd.scootersharing.data.Ride
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.text.SimpleDateFormat
import java.util.*


class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private val TAG = "MyActivity"
    var scannerView: ZXingScannerView? = null
    var id:String? = null
    var name:String? = null
    var lat:String? = null
    var long:String? = null
    var status:String? = null
    var price:Int? = null
    var photo:String? =null
    var battery:Int? = null
    var timestamp:String? = null
    private lateinit var rideViewModel: RideViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rideViewModel = ViewModelProvider(this).get(RideViewModel::class.java)
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)

        setPermission()
    }

    override fun handleResult(p0: Result?) {
            Toast.makeText(this, p0.toString(), Toast.LENGTH_SHORT).show()
//            check if string = id
            val bundle = intent.extras
            //set_text
            Log.d(TAG, p0.toString())

            if (bundle != null) {
                 id = bundle.getString("id")
                 name = bundle.getString("name")
                lat = bundle.getString("lat")
                long = bundle.getString("long")
                 status = bundle.getString("status")
                 price = bundle.getInt("price")
                 photo = bundle.getString("picture")
                 battery = bundle.getInt("battery")
                 timestamp = convertLongToTime(bundle.getLong("timestamp"))
            }
            Log.d(TAG, id.toString())
            if(p0.toString() == id){
//                Create Ride Object
                val ride = Ride()
                ride.name = name
                ride.status = "Running"
                ride.battery = battery
                ride.startLat = lat
                ride.startLong = long
                ride.endLat=""
                ride.endLong = ""
                ride.picture = photo
                ride.price = price
                if (bundle != null) {
                    ride.timestamp = bundle.getLong("timestamp")
                }
                ride.scooterId = id
                //insert to firebase
                rideViewModel.addRide(ride)

                val intent = Intent (this, RideEndActivity::class.java)
                startActivity(intent)

            }else{
                Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show()
            }

    }
    fun convertLongToTime (time:Long) :String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        return format.format(date)
    }
    override fun onResume() {
        super.onResume()

        scannerView?.setResultHandler(this)
        scannerView?.startCamera()
    }

    override fun onStop() {
        super.onStop()
        scannerView?.stopCamera()
        onBackPressed()
    }

    private fun setPermission() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            1
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        applicationContext,
                        "You need camera permission",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }
}