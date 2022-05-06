package dk.itu.moapd.scootersharing.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import dk.itu.moapd.scootersharing.data.NODE_RIDE
import dk.itu.moapd.scootersharing.data.Ride
import dk.itu.moapd.scootersharing.data.Scooter

class RideViewModel: ViewModel() {
    val dbRide = FirebaseDatabase.getInstance("https://scootersharing-f74c3-default-rtdb.europe-west1.firebasedatabase.app").getReference(NODE_RIDE)
    private val _result = MutableLiveData<Exception?>()
    val result:LiveData<Exception?>
        get() = _result
    private val _ride = MutableLiveData<List<Ride>>()
    val ride: LiveData<List<Ride>>
        get() = _ride


    fun addRide(ride: Ride){
        //unique key(id) using push function
        ride.id = dbRide.push().key

        //set key as id
        dbRide.child(ride.id!!).setValue(ride)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    _result.value = null
                }else{
                    _result.value = it.exception
                }
            }
    }
    fun updateRide(ride: Ride){
        //update status
        dbRide.child(ride.id.toString()).child("status").setValue(ride.status)
        dbRide.child(ride.id.toString()).child("timestamp").setValue(ride.timestamp)
        dbRide.child(ride.id.toString()).child("price").setValue(ride.price)
        dbRide.child(ride.id.toString()).child("endLat").setValue(ride.endLat)
        dbRide.child(ride.id.toString()).child("endLong").setValue(ride.endLong)
        dbRide.child(ride.id.toString()).child("battery").setValue(ride.battery)
    }
    fun fetchScooter(){
        //only for nodes that are change or added
        dbRide.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val rides = mutableListOf<Ride>()
                    for(rideSnapshot in snapshot.children){
                        val ride = rideSnapshot.getValue(Ride::class.java)
                        //id
                        ride?.id = rideSnapshot.key
                        ride?.let { rides.add(it) }
                    }
                    _ride.value = rides
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}