//package dk.itu.moapd.scootersharing
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [ScooterSharingFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class ScooterSharingFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_scooter_sharing, container, false)
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment ScooterSharingFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            ScooterSharingFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}

package dk.itu.moapd.scootersharing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import dk.itu.moapd.scootersharing.databinding.FragmentScooterSharingBinding
import java.text.SimpleDateFormat
import java.util.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.core.view.get


class ScooterSharingFragment : Fragment() {

    private var _binding: FragmentScooterSharingBinding? = null
    private val binding get() = _binding!!

    companion object {
        lateinit var ridesDB: RidesDB
        lateinit var adapter: CustomArrayAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScooterSharingBinding.inflate(inflater, container, false)
        ridesDB = RidesDB.get(requireContext())
        return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setContentView(binding.root)

        val startRideButton = binding.startButton
        val editRideButton = binding.editRide
        val listViewButton = binding.listRide
        startRideButton.setOnClickListener {
            openStartRideActivity();
        }
        editRideButton.setOnClickListener {
            openEditRideActivity();
        }
        listViewButton.setOnClickListener {
            refreshListRideView();
        }

        //Set up Adapter
        val listView = binding.listScooter
        ridesDB = RidesDB.get(requireContext())
        val data = ridesDB.getScooters()
        adapter = CustomArrayAdapter(requireContext(), R.layout.list_rides, data)
        listView.adapter= adapter//adapter telling list what to render

        binding.listScooter.setOnItemClickListener { parent, view, position, id ->
            adapter.remove(adapter.getItem(position))
            adapter.notifyDataSetChanged()
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
        adapter.notifyDataSetChanged();
    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        ridesDB = RidesDB.get(this)
//        binding = ActivityScooterSharingBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val startRideButton = binding.startButton
//        val editRideButton = binding.editRide
//        startRideButton.setOnClickListener {
//            openStartRideActivity();
//        }
//        editRideButton.setOnClickListener {
//            openEditRideActivity();
//        }
//
//        //Set up Adapter
//        val listView = binding.listScooter
//        listView.adapter= MyCustomAdapter(this)//adapter telling list what to render
//
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
//
//        if(currentFragment == null){
//            val fragment =
//                supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment)
//                    .commit()
//        }
//    }





}