package dk.itu.moapd.scootersharing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dk.itu.moapd.scootersharing.databinding.ActivityEditRideBinding
import dk.itu.moapd.scootersharing.databinding.ActivityScooterSharingBinding
import dk.itu.moapd.scootersharing.databinding.ActivityStartRideBinding


class EditRideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRideBinding
    companion object {
        lateinit var ridesDB: RidesDB
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ridesDB = RidesDB.get(this)

        //Edit texts
        val lastAddedText = binding.infoText
        val nameText = binding.nameText
        val whereText = binding.whereText

        //Buttons
//        addButton = findViewById(R.id.)
        val addButton = binding.updateButton
        addButton.setOnClickListener {
            if (nameText.text.isNotEmpty() &&
                whereText.text.isNotEmpty()) {
                //update the object attributes
                val name = nameText.text.toString().trim()
                val where = whereText.text.toString().trim()
                val timestamp = System.currentTimeMillis()
                val formScooter = Scooter(name, where, timestamp)

                //Reset the text fields and update the UI
                lastAddedText.setText("")
                nameText.setText("")
                whereText.setText("")
                ridesDB.updateScooter(name,where)
                updateUI(formScooter)
            }

        }
    }
    private fun updateUI(update:Scooter){
        binding.infoText.setText(ridesDB.getScooters().toString())
    }
}