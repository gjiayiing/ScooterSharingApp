package dk.itu.moapd.scootersharing.ui

import android.location.Location
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dk.itu.moapd.scootersharing.data.NODE_SCOOTER
import dk.itu.moapd.scootersharing.data.Scooter
import java.lang.Exception

class ScooterViewModel : ViewModel() {
    private val dbScooter = FirebaseDatabase.getInstance("https://scootersharing-f74c3-default-rtdb.europe-west1.firebasedatabase.app").getReference(NODE_SCOOTER)

    private val _scooter = MutableLiveData<List<Scooter>>()
    val scooter: LiveData<List<Scooter>>
        get() = _scooter

    private val _result = MutableLiveData<Exception?>()
    val result:LiveData<Exception?>
        get() = _result

    private val location = MutableLiveData<Location>()
    val locationState: LiveData<Location>
        get() = location

    fun onLocationChanged(location: Location) {
        this.location.value = location
    }
    fun updateScooter(scooter: Scooter){
        //update status
        dbScooter.child(scooter.id!!).setValue(scooter)
            .addOnCompleteListener{
                if (it.isSuccessful) {
                    _result.value = null
                }else{
                    _result.value= it.exception
                }
            }
    }
    fun addScooter(scooter: Scooter){
//        val dbScooter = FirebaseDatabase.getInstance("https://scootersharing-f74c3-default-rtdb.europe-west1.firebasedatabase.app").getReference(NODE_SCOOTER)
        scooter.id = dbScooter.push().key

        dbScooter.child(scooter.id!!).setValue(scooter)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    _result.value = null
                }else{
                    _result.value = it.exception
                }
            }
    }
    fun fetchScooter(){
        //only for nodes that are change or added
        dbScooter.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val scooters = mutableListOf<Scooter>()
                    for(scooterSnapshot in snapshot.children){
                        val scooter = scooterSnapshot.getValue(Scooter::class.java)
                        //id
                        scooter?.id = scooterSnapshot.key
                        scooter?.let { scooters.add(it) }

                    }
                    _scooter.value = scooters
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


}