package dk.itu.moapd.scootersharing.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.data.Scooter
import me.dm7.barcodescanner.zxing.ZXingScannerView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
val TAG = "MyActivity"
class StartRideFragment(private val scooter : Scooter) : DialogFragment() {

    private lateinit var viewModel: ScooterViewModel
    private lateinit var rideViewModel: RideViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rideViewModel = ViewModelProvider(this).get(RideViewModel::class.java)
        viewModel = ViewModelProvider(this).get(ScooterViewModel::class.java)

        return inflater.inflate(R.layout.fragment_start_ride, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //observe the result if insert is successful for ridedb
        rideViewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if(it == null){
                getString(R.string.ride_started)
            }else{
                getString(R.string.error, it.message)
            }
        })

        val timestamp: TextView = view.findViewById(R.id.rViewLastAdded)
        val title: TextView = view.findViewById(R.id.rViewName)
        val location: TextView = view.findViewById(R.id.rViewWhere)
        val status: TextView = view.findViewById(R.id.rViewScooterStatus)
        val price: TextView = view.findViewById(R.id.rViewScooterPrice)
        val photo: ImageView = view.findViewById(R.id.imageViewScooter)
        val scooterViewBtn: Button = view.findViewById(R.id.viewScooterBtn)

        //set_text
        title.text = scooter.name
        location.text = scooter.lat + " "+scooter.long

        status.text = scooter.status
        price.text = scooter.price.toString()
        Glide.with(view).load(scooter.picture).into(photo)
        timestamp.text = scooter.timestamp?.let {
            scooter.convertLongToTime(it)
        }
        scooterViewBtn.setOnClickListener{
            scooter.status = "Reserved"
            viewModel.updateScooter(scooter)
            //QR Code
            scooterViewBtn.setOnClickListener {
                activity?.let{
                    val intent = Intent (getActivity(), ScanActivity::class.java)
                    val bundle = Bundle()
                    if (bundle != null) {
                        bundle.putString("name", scooter.name)
                        bundle.putString("id", scooter.id)
                        bundle.putString("picture", scooter.picture)
                        bundle.putString("lat", scooter.lat)
                        bundle.putString("long", scooter.long)
                        scooter.timestamp?.let { it1 -> bundle.putLong("timestamp", it1) }
                        scooter.battery?.let { it1 -> bundle.putInt("battery", it1) }
                        scooter.price?.let { it1 -> bundle.putInt("price", it1) }
                        bundle.putString("price", scooter.status)
                    }
                    intent.putExtras(bundle)
                    startActivity(intent);

                }

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 122 && resultCode == Activity.RESULT_OK) {
            val bundle = this.arguments
            if (bundle != null) {
                val myString = bundle.getString("result")
                Log.d(TAG, myString.toString())
            }
        }
    }




}
