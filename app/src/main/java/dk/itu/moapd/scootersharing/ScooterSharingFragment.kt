package dk.itu.moapd.scootersharing


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dk.itu.moapd.scootersharing.databinding.FragmentScooterSharingBinding

class ScooterSharingFragment : Fragment() {

    private var _binding: FragmentScooterSharingBinding? = null
    private val binding get() = _binding!!

    companion object {
        lateinit var ridesDB: RidesDB
        lateinit var adapterR: CustomAdapter
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
//        val listViewButton = binding.listRide
        startRideButton.setOnClickListener {
            openStartRideActivity()
        }
        editRideButton.setOnClickListener {
            openEditRideActivity()
        }
//        listViewButton.setOnClickListener {
//            refreshListRideView()
//        }

        //Set up Adapter
        ridesDB = RidesDB.get(requireContext())
        val data = ridesDB.getScooters()
        adapterR = CustomAdapter(data)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapterR

//        binding.listScooter.setOnItemClickListener { _, _, position, _ ->
//            adapter.remove(adapter.getItem(position))
//            adapter.notifyDataSetChanged()
//            Toast.makeText(activity, "Scooter Removed", Toast.LENGTH_SHORT).show()
//        }

    }
    private fun openStartRideActivity() {
        replaceFragment(StartRideFragment())
    }

    private fun openEditRideActivity() {
        replaceFragment(EditRideFragment())
    }
    private fun replaceFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
//    private fun refreshListRideView() {
//        adapter.notifyDataSetChanged()
//    }
}