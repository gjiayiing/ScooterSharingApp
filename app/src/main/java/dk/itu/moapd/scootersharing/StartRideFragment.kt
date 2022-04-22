package dk.itu.moapd.scootersharing

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import dk.itu.moapd.scootersharing.databinding.FragmentStartRideBinding


class StartRideFragment : Fragment() {

    private var _binding: FragmentStartRideBinding? = null
    private val binding get() = _binding!!
    companion object {
        lateinit var ridesDB: RidesDB
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartRideBinding.inflate(inflater, container, false)
        ridesDB = RidesDB.get(requireContext())
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Edit texts
        val lastAddedText = binding.infoText
        val nameText = binding.nameText
        val whereText = binding.whereText
        val statusText = binding.status
        val priceText = binding.price

//        val inputText = binding.textField.editText?.text.toString()
//        binding.textField.editText?.doOnTextChanged { inputText, _, _, _ ->
//            Toast.makeText(activity, inputText, Toast.LENGTH_LONG).show()
//        }

        //Buttons
        val addButton = binding.startButton
        addButton.setOnClickListener {
            if (TextUtils.isEmpty(nameText.text?.toString()) && TextUtils.isEmpty(whereText.text?.toString())) {
                Toast.makeText(activity, "Please enter the details!", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(nameText.text?.toString())) {
                Toast.makeText(activity, "Please enter your name!", Toast.LENGTH_SHORT).show()
            }else if (TextUtils.isEmpty(whereText.text?.toString())) {
                Toast.makeText(activity, "Please enter your destination!", Toast.LENGTH_SHORT).show()
            } else {
                //update the object attributes
                val name = nameText.text.toString().trim()
                val where = whereText.text.toString().trim()
                val timestamp = System.currentTimeMillis()
                val status = statusText.text.toString().trim()
                val price = priceText.text.toString().trim().toInt()
                val formScooter = Scooter(name, where, timestamp, status, price)
                ridesDB.addScooter(name,where,status, price)


                //Reset the text fields and update the UI
                lastAddedText.setText("")
                nameText.setText("")
                whereText.setText("")
                updateUI(formScooter)
                Toast.makeText(activity, "Ride Added!", Toast.LENGTH_LONG).show()
//                val intent = Intent(requireContext(), ScooterSharingActivity::class.java)
//                startActivity(intent)
            }

        }

    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        ridesDB = RidesDB.get(this)
//        binding = ActivityStartRideBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        //Edit texts
//        val lastAddedText = binding.infoText
//        val nameText = binding.nameText
//        val whereText = binding.whereText
//
//        //Buttons
////        addButton = findViewById(R.id.)
//        val addButton = binding.startButton
//        addButton.setOnClickListener {
//            if (nameText.text.isNotEmpty() &&
//                whereText.text.isNotEmpty()) {
//                //update the object attributes
//                val name = nameText.text.toString().trim()
//                val where = whereText.text.toString().trim()
//                val timestamp = System.currentTimeMillis()
//                val formScooter = Scooter(name, where, timestamp)
//                ridesDB.addScooter(name,where)
//
//                //Reset the text fields and update the UI
//                lastAddedText.setText("")
//                nameText.setText("")
//                whereText.setText("")
//                updateUI(formScooter)
//            }
//
//        }
//    }
    private fun updateUI(update:Scooter){
        binding.infoText.setText(update.toString())
    }
}