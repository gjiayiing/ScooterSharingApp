package dk.itu.moapd.scootersharing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dk.itu.moapd.scootersharing.databinding.ActivityStartRideBinding


class StartRideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_ride)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_start_container)

        if (currentFragment == null) {
            val fragment = StartRideFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_start_container, fragment)
                .commit()
        }
    }
}