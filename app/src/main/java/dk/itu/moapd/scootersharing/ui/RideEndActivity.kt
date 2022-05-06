package dk.itu.moapd.scootersharing.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.google.firebase.database.*
import dk.itu.moapd.scootersharing.data.NODE_RIDE
import dk.itu.moapd.scootersharing.data.NODE_SCOOTER
import dk.itu.moapd.scootersharing.data.Ride
import dk.itu.moapd.scootersharing.data.Scooter
import dk.itu.moapd.scootersharing.databinding.ActivityRideEndBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class RideEndActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRideEndBinding
    private lateinit var database: DatabaseReference
    private var rideId:String? = null
    private var name:String? = null
    private var status:String? = null
    private var battery:Int? = null
    private var startLocation:String? = null
    private var endLocation:String? = null
    private var picture:String? = null
    private var price:Int? = null
    private var timestamp:Long? = null
    private var scooterId:String? = null
    private var currentLocation:String? = null
    private var currentLat:String? = null
    private var currentLong:String? = null
    private val rideViewModel: RideViewModel by lazy {
        ViewModelProvider(this)
            .get(RideViewModel::class.java)
    }
    private val viewModel: ScooterViewModel by lazy {
        ViewModelProvider(this)
            .get(ScooterViewModel::class.java)
    }
    companion object {
        private const val ALL_PERMISSIONS_RESULT = 1011
    }
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var locationCallback: LocationCallback
    var rides = Ride()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityRideEndBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startLocationAware()
        viewModel.locationState.observe(this){
            it?.let {
                currentLocation = it.latitude.toString() +" " + it.longitude.toString()
                currentLat = it.latitude.toString()
                currentLong = it.longitude.toString()
                Toast.makeText(this, it.latitude.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        readData()
    }

    fun convertLongToTime (time:Long) :String {
        val date = Date(time)
        val format = SimpleDateFormat("hh:mm:ss")
        return format.format(date)
    }
    fun convertLongToHours (time:Long) :String {

        val hours = time / (2.77777778 * Math.pow(10.toDouble(),7.toDouble()))

        return String.format("%.2f", hours)
    }
    override fun onResume() {
        super.onResume()
        subscribeToLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        unsubscribeToLocationUpdates()
    }

    /**
     * Start the location-aware instance and defines the callback to be called when the GPS sensor
     * provides a new user's location.
     */
    private fun startLocationAware() {

        // Show a dialog to ask the user to allow the application to access the device's location.
        requestUserPermissions()

        // Start receiving location updates.
        fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(this)

        // Initialize the `LocationCallback`.
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                viewModel.onLocationChanged(locationResult.lastLocation)
            }
        }
    }


    private fun requestUserPermissions() {
        // An array with location-aware permissions.
        val permissions: ArrayList<String> = ArrayList()
        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)


        // Check which permissions is needed to ask to the user.
        val permissionsToRequest = permissionsToRequest(permissions)

        // Show the permissions dialogs to the user.
        if (permissionsToRequest.size > 0)
            requestPermissions(
                permissionsToRequest.toTypedArray(),
                RideEndActivity.ALL_PERMISSIONS_RESULT
            )
    }

    /**
     * Create an array with the permissions to show to the user.
     *
     * @param permissions An array with the permissions needed by this applications.
     *
     * @return An array with the permissions needed to ask to the user.
     */
    private fun permissionsToRequest(permissions: ArrayList<String>): ArrayList<String> {
        val result: ArrayList<String> = ArrayList()
        for (permission in permissions)
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                result.add(permission)
        return result
    }

    /**
     * This method checks if the user allows the application uses all location-aware resources to
     * monitor the user's location.
     *
     * @return A boolean value with the user permission agreement.
     */
    private fun checkPermission() =
        ActivityCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this, android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED

    /**
     * Subscribes this application to get the location changes via the `locationCallback()`.
     */
    @SuppressLint("MissingPermission")
    private fun subscribeToLocationUpdates() {
        // Check if the user allows the application to access the location-aware resources.
        if (checkPermission())
            return

        // Sets the accuracy and desired interval for active location updates.
        val locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(5)
            fastestInterval = TimeUnit.SECONDS.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // Subscribe to location changes.
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    /**
     * Unsubscribes this application of getting the location changes from  the `locationCallback()`.
     */
    private fun unsubscribeToLocationUpdates() {
        // Unsubscribe to location changes.
        fusedLocationProviderClient
            .removeLocationUpdates(locationCallback)
    }
    private fun readData(): Ride {
        database = FirebaseDatabase.getInstance("https://scootersharing-f74c3-default-rtdb.europe-west1.firebasedatabase.app").getReference(NODE_RIDE)
        val lastQuery: Query =
            database.orderByKey().limitToLast(1)
        lastQuery.addListenerForSingleValueEvent (object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (scooterSnapshot in snapshot.children) {
                    rides = Ride(
                        scooterSnapshot.getKey(),
                        scooterSnapshot.child("status").getValue(String::class.java),
                        scooterSnapshot.child("battery").getValue(Int::class.java),
                        scooterSnapshot.child("startLat").getValue(String::class.java),
                        scooterSnapshot.child("startLong").getValue(String::class.java),
                        scooterSnapshot.child("endLat").getValue(String::class.java),
                        scooterSnapshot.child("endLong").getValue(String::class.java),
                        scooterSnapshot.child("name").getValue(String::class.java),
                        scooterSnapshot.child("picture").getValue(String::class.java),
                        scooterSnapshot.child("price").getValue(Int::class.java),

                        scooterSnapshot.child("timestamp").getValue(Long::class.java),
                        scooterSnapshot.child("scooterId").getValue(String::class.java))
                        binding.name.text = "Name: "+ rides.name
                        binding.status.text = "status: "+ rides.status
                        binding.price.text = "Price: "+rides.price.toString() + "/hr "
                        binding.battery.text = "Battery: "+rides.battery.toString()+"%"
                        binding.startLocation.text = "Lat: " + rides.startLat +" Long: " +rides.startLong
                        Glide.with(this@RideEndActivity)
                        .load(rides.picture.toString().toUri())
                        .into(binding.RideImage)
                        Toast.makeText(this@RideEndActivity, rides.id.toString(), Toast.LENGTH_SHORT).show()
                        binding.EndRideBtn.setOnClickListener(){
                            //time
                            val startTime = rides.timestamp

                            val endTime = System.currentTimeMillis()
                            val difference = convertLongToTime(endTime - startTime!!)
                            //price
                            val totalPrice = convertLongToHours(endTime - startTime!!).toDouble() * rides.price!!

                            //status
                            val status = "Ended"
                            basicAlert(binding.root)

                        }
                    }
         }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            fun basicAlert(view: View){

                val builder = AlertDialog.Builder(this@RideEndActivity)

                with(builder)
                {
                    setTitle("End Ride?")
                    setMessage("Do You Want to End Ride?")
                    setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
                    setNegativeButton(android.R.string.no, negativeButtonClick)
                    show()
                }
            }
            val positiveButtonClick = { dialog: DialogInterface, which: Int ->
                val name = rides.name
                //time
                val startTime = rides.timestamp
                val endTime = System.currentTimeMillis()
                val endTimeString = convertLongToTime(System.currentTimeMillis())
                val difference = convertLongToTime(endTime - startTime!!)
                //price
                val totalPrice = (convertLongToHours(endTime - startTime!!).toDouble() * rides.price!!).toInt()

                //status
                val status = "Ended"
                //battery
                if(rides.battery == 0 ){
                    rides.battery = 100
                    battery = rides.battery?.minus(25)
                }else{
                    battery = rides.battery?.minus(25)
                }
                val timestamp = endTimeString
                val endLat = currentLat
                val endLong = currentLong

                var updateRide = Ride(
                    rides.id,
                    status,
                    battery,
                    rides.startLat,
                    rides.startLong,
                    currentLat,
                    currentLong,
                    rides.name,
                    rides.picture,
                    totalPrice,
                    endTime,
                    rides.scooterId)

                rideViewModel.updateRide(updateRide)
                val dbScooter = FirebaseDatabase.getInstance("https://scootersharing-f74c3-default-rtdb.europe-west1.firebasedatabase.app").getReference(NODE_SCOOTER)
                dbScooter.child(rides.scooterId.toString()).child("status").setValue("Locked")
                    .addOnCompleteListener{
                        if (it.isSuccessful) {
                            Toast.makeText(this@RideEndActivity, "Scooter Locked", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@RideEndActivity, "Failed, Please try to lock scooter", Toast.LENGTH_SHORT).show()
                        }
                    }
                Toast.makeText(this@RideEndActivity, "Ride Ended Successfully", Toast.LENGTH_SHORT).show()

                val myIntent = Intent(this@RideEndActivity, ScooterSharingActivity::class.java)

                this@RideEndActivity.startActivity(myIntent)

            }
            val negativeButtonClick = { dialog: DialogInterface, which: Int ->
                Toast.makeText(applicationContext,
                    android.R.string.no, Toast.LENGTH_SHORT).show()
            }
        })
        return rides


    }

}


