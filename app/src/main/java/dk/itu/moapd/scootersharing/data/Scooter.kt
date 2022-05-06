package dk.itu.moapd.scootersharing.data

import com.google.firebase.database.Exclude
import java.text.SimpleDateFormat
import java.util.*

data class Scooter(
    @field:Exclude var id:String? = null,
    var status: String? = null,
    var battery: Int? = null,
    val lat : String? = null,
    val long: String? = null,
    var name: String? = null,
    var picture: String?=null,
    var price: Int? = null,
    var timestamp: Long? = null,
    ) {

    override fun toString(): String {
        val timestamp = timestamp?.let { convertLongToTime(it) }
        return "$name is placed at $lat $long : $timestamp "
    }

    fun convertLongToTime (time:Long) :String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        return format.format(date)
    }



}

