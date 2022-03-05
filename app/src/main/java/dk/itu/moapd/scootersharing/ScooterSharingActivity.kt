package dk.itu.moapd.scootersharing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dk.itu.moapd.scootersharing.databinding.ActivityScooterSharingBinding



class ScooterSharingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScooterSharingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScooterSharingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val startRideButton = binding.startButton
        val editRideButton = binding.editRide
        startRideButton.setOnClickListener {
            openStartRideActivity();
        }
        editRideButton.setOnClickListener {
            openEditRideActivity();
        }
    }
    private fun openStartRideActivity() {
        val intent = Intent(this, StartRideActivity::class.java)
        startActivity(intent)
    }
    private fun openEditRideActivity() {
        val intent = Intent(this, EditRideActivity::class.java)
        startActivity(intent)
    }

}