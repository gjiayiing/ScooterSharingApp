package dk.itu.moapd.scootersharing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dk.itu.moapd.scootersharing.databinding.ActivityEditRideBinding


class EditRideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_ride)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_edit_container)

        if (currentFragment == null) {
            val fragment = EditRideFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_edit_container, fragment)
                .commit()
        }
    }
}