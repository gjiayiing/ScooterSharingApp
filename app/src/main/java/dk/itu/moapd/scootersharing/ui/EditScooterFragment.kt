package dk.itu.moapd.scootersharing.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.*
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.data.NODE_SCOOTER
import dk.itu.moapd.scootersharing.data.Scooter
import dk.itu.moapd.scootersharing.databinding.FragmentEditScooterBinding
import java.text.SimpleDateFormat
import java.util.*


class EditScooterFragment : Fragment() {

    private var _binding: FragmentEditScooterBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private val viewModel: ScooterViewModel by lazy {
        ViewModelProvider(this)
            .get(ScooterViewModel::class.java)
    }
    var scooters = Scooter()
    override fun onCreateView(inflater:LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditScooterBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Edit texts
        val lastAddedText = binding.infoText
        val nameText = binding.nameText
        val whereText = binding.whereText
        val statusText = binding.status
        val priceText = binding.price

        database = FirebaseDatabase.getInstance("https://scootersharing-f74c3-default-rtdb.europe-west1.firebasedatabase.app").getReference(NODE_SCOOTER)
        val lastQuery: Query =
            database.orderByKey().limitToLast(1)
        lastQuery.addListenerForSingleValueEvent (object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (scooterSnapshot in snapshot.children) {
                    scooters = Scooter(
                        scooterSnapshot.getKey(),
                        scooterSnapshot.child("status").getValue(String::class.java),
                        scooterSnapshot.child("battery").getValue(Int::class.java),
                        scooterSnapshot.child("lat").getValue(String::class.java),
                        scooterSnapshot.child("long").getValue(String::class.java),
                        scooterSnapshot.child("name").getValue(String::class.java),
                        scooterSnapshot.child("picture").getValue(String::class.java),
                        scooterSnapshot.child("price").getValue(Int::class.java),
                        scooterSnapshot.child("timestamp").getValue(Long::class.java))

                        whereText.text = "Lat: " + scooters.lat + " Long: "+ scooters.long
                        lastAddedText.text = scooters.timestamp?.let { convertLongToTime(it) }
                        nameText.setText(scooters.name)
                        priceText.setText(scooters.price.toString())
                        statusText.setText(scooters.status)

                }
                //Buttons
                val addButton = binding.updateButton
                addButton.setOnClickListener {

                    if (TextUtils.isEmpty(nameText.text?.toString())) {
                        Toast.makeText(activity, "Please enter the details!", Toast.LENGTH_SHORT)
                            .show()
                    } else if (TextUtils.isEmpty(nameText.text?.toString())) {
                        //update the object attributes
                        Toast.makeText(activity, "Please enter your name!", Toast.LENGTH_SHORT)
                            .show()
                    } else if (TextUtils.isEmpty(statusText.text?.toString())) {
                        Toast.makeText(activity, "Please update status", Toast.LENGTH_SHORT).show()
                    } else if (TextUtils.isEmpty(priceText.text?.toString())) {
                        Toast.makeText(activity, "Please update price field!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        val name = nameText.text.toString().trim()
                        val status = statusText.text.toString().trim()
                        val price = priceText.text.toString().trim().toInt()
                        val battery = 100

                        //Reset the text fields and update the UI
                        binding.nameText.setText("")
                        binding.status.setText("")
                        binding.price.setText("")
                        var updateScoot = Scooter(
                            scooters.id,
                            status,
                            battery,
                            scooters.lat,
                            scooters.long,
                            name,
                            scooters.picture,
                            price,
                            System.currentTimeMillis()
                        )
                        viewModel.updateScooter(updateScoot)
                        binding.resultTxt.visibility = View.VISIBLE
//                        val frag = ScooterSharingFragment()
//                        val transaction =  parentFragmentManager.beginTransaction()
//                        transaction?.replace(R.id.navhostFragment, frag)
//                        transaction?.addToBackStack(null)
//                        transaction?.commit()
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    fun onBackPressed() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .remove(this)
            .commit()
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun convertLongToTime (time:Long) :String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        return format.format(date)
    }

}