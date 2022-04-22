package dk.itu.moapd.scootersharing

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(
    private val data: List<Scooter>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.rViewLastAdded)
        val secondaryText: TextView = view.findViewById(R.id.rViewName)
        val supportingText: TextView = view.findViewById(R.id.rViewWhere)
        val status: TextView = view.findViewById(R.id.rViewScooterStatus)
        val price: TextView = view.findViewById(R.id.rViewScooterPrice)
//        val photo:ImageView = view.findViewById(R.id.rViewScooterPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount() = data.size
    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val dummy = data[position]
        holder.apply {
            title.text = dummy?.name
            secondaryText.text = dummy?.where
            supportingText.text = dummy?.convertLongToTime(dummy.timestamp)
            status.text = dummy?.status
            price.text = dummy?.price.toString()

        }
    }
}

