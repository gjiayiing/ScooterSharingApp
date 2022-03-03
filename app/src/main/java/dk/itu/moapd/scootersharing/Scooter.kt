package dk.itu.moapd.scootersharing

class Scooter{
    private var name: String
    private var where:String

    constructor(name: String, where: String){
        this.name = name
        this.where = where
    }

    fun getName():String{
        return name
    }

    fun setName(name: String){
        this.name = name
    }

    fun getWhere(): String{
        return where
    }

    fun setWhere(where: String){
        this.where = where
    }

    override fun toString(): String {
        return "$name is placed at $where"
    }
}