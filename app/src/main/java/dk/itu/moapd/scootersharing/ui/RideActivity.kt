package dk.itu.moapd.scootersharing.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.data.Scooter
import dk.itu.moapd.scootersharing.databinding.ActivityRideBinding


class RideActivity : AppCompatActivity(), RecyclerViewClickListener {

    private val TAG = "MyActivity"
    private val viewModel: ScooterViewModel by lazy {
        ViewModelProvider(this)
            .get(ScooterViewModel::class.java)
    }

    private val adapter = ScooterAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRideBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        adapter.listener = this
        binding.recyclerView.adapter = adapter
        viewModel.fetchScooter()
        viewModel.scooter.observe(this, Observer {
            adapter.setScooter(it)
        })

    }

    override fun onRecyclerViewItemClicked(view: View, scooter: Scooter) {
        when(view.id){
            R.id.viewScooterBtn -> {
                var dialog = StartRideFragment(scooter)
                dialog.show(supportFragmentManager,"")
            }
        }
    }
}