package dk.itu.moapd.scootersharing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import dk.itu.moapd.scootersharing.databinding.FragmentScooterSharingBinding

class ScooterSharingFragment : Fragment() {

    private var _binding: FragmentScooterSharingBinding? = null
    private val binding get() = _binding!!

    companion object {
        lateinit var ridesDB: RidesDB
        lateinit var adapter: CustomArrayAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScooterSharingBinding.inflate(inflater, container, false)
        ridesDB = RidesDB.get(requireContext())
        return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            refreshListRideView()
        }

        //Set up Adapter
        val listView = binding.listScooter
        ridesDB = RidesDB.get(requireContext())
        val data = ridesDB.getScooters()
        adapter = CustomArrayAdapter(requireContext(), R.layout.list_rides, data)
        listView.adapter= adapter//adapter telling list what to render

        binding.listScooter.setOnItemClickListener { _, _, position, _ ->
            adapter.remove(adapter.getItem(position))
            adapter.notifyDataSetChanged()
            Toast.makeText(activity, "Scooter Removed", Toast.LENGTH_SHORT).show()
        }

    }
    private fun openStartRideActivity() {
        val intent = Intent(requireContext(), StartRideActivity::class.java)
        startActivity(intent)
    }

    private fun openEditRideActivity() {
        val intent = Intent(requireContext(), EditRideActivity::class.java)
        startActivity(intent)
    }

    private fun refreshListRideView() {
        adapter.notifyDataSetChanged()
    }
}