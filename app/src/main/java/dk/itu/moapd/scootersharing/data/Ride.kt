package dk.itu.moapd.scootersharing.data

import android.net.Uri
import com.google.firebase.database.Exclude
import java.text.SimpleDateFormat
import java.util.*

data class Ride(
    @get:Exclude
    var id:String? = null,
    var status: String? = null,
    var battery: Int? = null,
    var startLat: String? = null,
    var startLong: String? = null,
    var endLat: String? = null,
    var endLong:String? = null,
    var name: String? = null,
    var picture: String? =null,
    var price: Int? = null,
    var timestamp: Long? = null,
    var scooterId: String?  = null
    )
{
    override fun toString(): String {
        val timestamp = timestamp?.let { convertLongToTime(it) }
        return "$name is placed at $endLat $endLong : $timestamp "
    }

    fun convertLongToTime (time:Long) :String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        return format.format(date)
    }
}