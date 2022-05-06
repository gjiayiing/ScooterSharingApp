package dk.itu.moapd.scootersharing.ui


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.databinding.FragmentScooterSharingBinding


class ScooterSharingFragment : Fragment() {
    private lateinit var viewModel: ScooterViewModel
    private val adapter = ScooterAdapter()
    private var currentUser = FirebaseAuth.getInstance().currentUser
    private var _binding: FragmentScooterSharingBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(ScooterViewModel::class.java)
        _binding = FragmentScooterSharingBinding.inflate(inflater, container, false)
        return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentUser?.let{
            user ->
            binding.welcome.text = "Welcome, User "+ user.email
        }


        viewModel.fetchScooter()
        viewModel.scooter.observe(viewLifecycleOwner, Observer {
            adapter.setScooter(it)

        })

        val startRideButton = binding.startButton
        val editRideButton = binding.editRide
        val listViewButton = binding.listRide
        startRideButton.setOnClickListener {
            openStartRideActivity()
        }
        editRideButton.setOnClickListener {
            openEditRideActivity()
        }
        listViewButton.setOnClickListener {
            openListRideActivity()
        }

    }
    private fun requestLocationUpdates(){
        var locationViewModel = ViewModelProvider(this).get(ScooterViewModel::class.java)
        locationViewModel.locationState.observe(this, Observer { Toast.makeText(activity, it.latitude.toString(), Toast.LENGTH_SHORT).show()})
    }
    private fun openStartRideActivity() {
        replaceFragment(TakeScooterPictureFragment())
//        replaceFragment(CreateScooterFragment())
    }

    private fun openEditRideActivity() {
        replaceFragment(EditScooterFragment())
    }
    private fun openListRideActivity() {
        activity?.let{
            val intent = Intent (it, RideActivity::class.java)
            it.startActivity(intent)
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.navhostFragment, fragment)
        transaction.commit()
    }
    fun onBackPressed() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .remove(this)
            .commit()
        requireActivity().supportFragmentManager.popBackStack()
    }

}