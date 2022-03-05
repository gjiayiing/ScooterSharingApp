package dk.itu.moapd.scootersharing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dk.itu.moapd.scootersharing.databinding.ActivityScooterSharingBinding
import dk.itu.moapd.scootersharing.databinding.ActivityStartRideBinding


class StartRideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartRideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartRideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Edit texts
        val lastAddedText = binding.infoText
        val nameText = binding.nameText
        val whereText = binding.whereText

        //Buttons
//        addButton = findViewById(R.id.)
        val addButton = binding.startButton
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
                updateUI(formScooter)
            }

        }
    }
    private fun updateUI(update:Scooter){
        binding.infoText.setText(update.toString())
    }
}