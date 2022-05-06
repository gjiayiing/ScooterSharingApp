package dk.itu.moapd.scootersharing.ui

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.data.Scooter
import dk.itu.moapd.scootersharing.databinding.FragmentCreateScooterBinding

class CreateScooterFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    val STORAGE_URL = "gs://scootersharing-f74c3.appspot.com/"

    private val TAG: String? = "MyActivity"
    private val viewModel: ScooterViewModel by activityViewModels()
    private var _binding: FragmentCreateScooterBinding? = null
    private val binding get() = _binding!!
    private var uri:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateScooterBinding.inflate(inflater, container, false)
        uri = arguments?.getString("uri")
        if (uri != null) {
            binding.imageView.setImageURI(uri!!.toUri())
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        storage = Firebase.storage(STORAGE_URL)
        val storageRef = storage.reference.child("pics/")

        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if(it == null){
                getString(R.string.scooter_added)
            }else{
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        //Edit texts
        val nameText = binding.nameText
        val whereText = binding.whereText
        val latText = binding.latText
        val longText = binding.longText
        val priceText = binding.price

        //Buttons
        val addButton = binding.startButton
        val getLocation = binding.getLocation

        getLocation.setOnClickListener{
            viewModel.locationState.observe(viewLifecycleOwner, Observer { location ->
                whereText.setText(location.latitude.toString() + " " + location.longitude.toString())
                latText.setText(location.latitude.toString())
                longText.setText(location.longitude.toString())
                Toast.makeText(activity,location.latitude.toString() + " " + location.longitude.toString(), Toast.LENGTH_SHORT).show()
            })
        }

        addButton.setOnClickListener {

            if (uri != null) {
                var file = uri!!.toUri()
                val fileRef = storageRef.child("${file.lastPathSegment}")
                Log.i(TAG, fileRef.toString())
                var uploadTask = fileRef.putFile(file)

                // Register observers to listen for when the download is done or if it fails
                uploadTask.continueWith {
                    if (!it.isSuccessful) {
                        it.exception?.let { t ->
                            throw t
                        }
                    }
                    fileRef.downloadUrl
                }.addOnCompleteListener {
                    if (it.isSuccessful) {
                        it.result!!.addOnSuccessListener { task ->
                            var myUri = task.toString()
                            Log.i(TAG, myUri)
                            if (TextUtils.isEmpty(nameText.text?.toString()) && TextUtils.isEmpty(
                                    whereText.text?.toString()
                                )
                            ) {
                                Toast.makeText(
                                    activity,
                                    "Please enter the details!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (TextUtils.isEmpty(nameText.text?.toString())) {
                                Toast.makeText(
                                    activity,
                                    "Please enter your name!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (TextUtils.isEmpty(whereText.text?.toString())) {
                                Toast.makeText(
                                    activity,
                                    "Please enter your destination!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                //update the object attributes
                                val name = nameText.text.toString().trim()
                                val where = whereText.text.toString().trim()
                                val lat = latText.text.toString().trim()
                                val longitude = longText.text.toString().trim()
                                val timestamp = System.currentTimeMillis()
                                val status = "Locked"
                                val price = priceText.text.toString().trim().toInt()
                                val battery = 100
                                val picture = myUri
                                val id = "1"

                                val formScooter = Scooter(
                                    id,
                                    status,
                                    battery,
                                    lat,
                                    longitude,
                                    name,
                                    picture,
                                    price,
                                    timestamp,
                                )
                                viewModel.addScooter(formScooter)

                                //Reset the text fields and update the UI
                                nameText.setText("")
                                whereText.setText("")
                                priceText.setText("")

                                //go back to home screen
                                // Create new fragment
                                var frag = ScooterSharingFragment()
                                val transaction =  parentFragmentManager.beginTransaction()
                                transaction?.replace(R.id.navhostFragment, frag)
                                transaction?.addToBackStack(null)
                                transaction?.commit()
                            }
                        }
                    }

                    }
                }
        }

    }


}