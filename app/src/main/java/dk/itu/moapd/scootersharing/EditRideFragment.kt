package dk.itu.moapd.scootersharing

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import dk.itu.moapd.scootersharing.databinding.FragmentEditRideBinding
import java.text.SimpleDateFormat
import java.util.*


class EditRideFragment : Fragment() {

    private var _binding: FragmentEditRideBinding? = null
    private val binding get() = _binding!!
    companion object {
        lateinit var ridesDB: RidesDB

    }
    override fun onCreateView(inflater:LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditRideBinding.inflate(inflater, container, false)
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


        ridesDB = RidesDB.get(requireContext())
        //get the last added ride
        val data = ridesDB.getLastScooterInfo()

        lastAddedText.setText(convertLongToTime(data.timestamp))
        nameText.setText(data.name)
        whereText.setText(data.where)
        statusText.setText(data.status)
        priceText.setText(data.price.toString())

        //Buttons
        val addButton = binding.updateButton
        addButton.setOnClickListener {
            if (TextUtils.isEmpty(nameText.text?.toString()) && TextUtils.isEmpty(whereText.text?.toString())) {
                Toast.makeText(activity, "Please enter the details!", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(nameText.text?.toString())) {
                //update the object attributes
                Toast.makeText(activity, "Please enter your name!", Toast.LENGTH_SHORT).show()

            }else if (TextUtils.isEmpty(whereText.text?.toString())) {
                Toast.makeText(activity, "Please enter your destination!", Toast.LENGTH_SHORT).show()

            } else {
                val name = nameText.text.toString().trim()
                val where = whereText.text.toString().trim()
                val status = statusText.text.toString().trim()
                val price = priceText.text.toString().trim().toInt()


                //Reset the text fields and update the UI
                lastAddedText.setText("")
                nameText.setText("")
                whereText.setText("")
                ridesDB.updateScooter(name,where, status, price)
                Toast.makeText(activity, "Ride Updated!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun convertLongToTime (time:Long) :String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        return format.format(date)
    }

}