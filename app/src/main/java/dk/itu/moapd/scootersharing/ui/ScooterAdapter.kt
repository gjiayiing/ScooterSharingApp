package dk.itu.moapd.scootersharing.ui
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.data.Scooter


class ScooterAdapter : RecyclerView.Adapter<ScooterAdapter.ScooterViewModel>() {

    private val context: Context? = null

    private var scooter = mutableListOf<Scooter>()
    var listener: RecyclerViewClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ScooterViewModel {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ScooterViewModel(itemView)
    }

    override fun getItemCount() = scooter.size

    override fun onBindViewHolder(holder: ScooterViewModel, position: Int) {
        holder.apply {
            secondaryText.text = scooter[position].name
            supportingText.text = "Lat: "+scooter[position].lat +"Long: "+scooter[position].long

            Glide.with(holder.photo.context).load(scooter[position].picture).into(photo)
            title.text = scooter[position].timestamp?.let {
                scooter[position].convertLongToTime(it)
            }
            status.text = scooter[position].status
            price.text = scooter[position].price.toString()
        }
        holder.scooterViewBtn.setOnClickListener{
            listener?.onRecyclerViewItemClicked(it, scooter[position])
        }

    }
    fun setScooter(scooter: List<Scooter>){
        this.scooter = scooter as MutableList<Scooter>
        notifyDataSetChanged()
    }
    class ScooterViewModel(val view: View) : RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.rViewLastAdded)
        val secondaryText: TextView = view.findViewById(R.id.rViewName)
        val supportingText: TextView = view.findViewById(R.id.rViewWhere)
        val status: TextView = view.findViewById(R.id.rViewScooterStatus)
        val price: TextView = view.findViewById(R.id.rViewScooterPrice)
        val photo: ImageView = view.findViewById(R.id.scooterPic)
        val layout:CardView = view.findViewById(R.id.card)
        val scooterViewBtn: Button = view.findViewById(R.id.viewScooterBtn)

    }

}

