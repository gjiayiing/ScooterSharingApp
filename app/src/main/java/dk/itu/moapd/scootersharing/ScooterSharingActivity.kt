package dk.itu.moapd.scootersharing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dk.itu.moapd.scootersharing.databinding.ActivityScooterSharingBinding



class ScooterSharingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScooterSharingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScooterSharingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Edit texts
        val lastAddedText = binding.lastAddedText
        val nameText = binding.nameText
        val whereText = binding.whereText

        //Buttons
//        addButton = findViewById(R.id.)
        val addButton = binding.addButton
        addButton.setOnClickListener {
            if (nameText.text.isNotEmpty() &&
                    whereText.text.isNotEmpty()) {
                //update the object attributes
                val name = nameText.text.toString().trim()
                val where = whereText.text.toString().trim()
                val formScooter = Scooter(name, where)

                //Reset the text fields and update the UI
                lastAddedText.setText("")
                nameText.setText("")
                whereText.setText("")
                updateUI(formScooter)
            }

        }
    }
    private fun updateUI(update:Scooter){
        binding.lastAddedText.setText(update.toString())
    }
}