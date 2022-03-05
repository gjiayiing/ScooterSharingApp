package dk.itu.moapd.scootersharing

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import dk.itu.moapd.scootersharing.databinding.ActivityScooterSharingBinding
import java.text.SimpleDateFormat
import java.util.*


class ScooterSharingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScooterSharingBinding
    companion object {
        lateinit var ridesDB: RidesDB
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ridesDB = RidesDB.get(this)
        binding = ActivityScooterSharingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val startRideButton = binding.startButton
        val editRideButton = binding.editRide
        startRideButton.setOnClickListener {
            openStartRideActivity();
        }
        editRideButton.setOnClickListener {
            openEditRideActivity();
        }

        //Set up Adapter
        val listView = binding.listScooter
        listView.adapter= MyCustomAdapter(this)//adapter telling list what to render
    }

    private class MyCustomAdapter(context: Context): BaseAdapter() {
        val rides = ridesDB.getScooters()

        private val mContext:Context = context

        // how many rows in the list
        override fun getCount(): Int {
            return rides.size
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
        override fun getItem(position: Int): Any {
            return "Test String"
        }
        //responsible for rendering out each view
        private fun convertLongToTime (time:Long) :String {
            val date = Date(time)
            val format = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
            return format.format(date)
        }
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.list_rides, viewGroup, false)

            val listViewName = rowMain.findViewById<TextView>(R.id.listViewName)
            listViewName.text = rides[position].name
            val listViewWhere = rowMain.findViewById<TextView>(R.id.listViewWhere)
            listViewWhere.text = rides[position].where
            val listViewTimeStamp = rowMain.findViewById<TextView>(R.id.listViewTimeStamp)
            listViewTimeStamp.text = convertLongToTime(rides[position].timestamp)
            return rowMain
        }
    }

    private fun openStartRideActivity() {
        val intent = Intent(this, StartRideActivity::class.java)
        startActivity(intent)
    }
    private fun openEditRideActivity() {
        val intent = Intent(this, EditRideActivity::class.java)
        startActivity(intent)
    }

}