package dk.itu.moapd.scootersharing

import java.text.SimpleDateFormat
import java.util.*

class Scooter{
    var name: String =""
    var where:String =""
    var timestamp:Long=System.currentTimeMillis()

    constructor(name: String, where: String, timestamp: Long){
        this.name = name
        this.where = where
        this.timestamp = timestamp
    }
    //removed getter and setters since they could be auto generated



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