package dk.itu.moapd.scootersharing

import android.widget.ImageView
import java.text.SimpleDateFormat
import java.util.*

class Scooter(var name: String, var where: String, var timestamp: Long, /*var photo: ImageView,*/var status: String, var price: Int) {

    override fun toString(): String {
        val timestamp = convertLongToTime(timestamp)
        return "$name is placed at $where : $timestamp "
    }

    fun convertLongToTime (time:Long) :String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        return format.format(date)
    }



}