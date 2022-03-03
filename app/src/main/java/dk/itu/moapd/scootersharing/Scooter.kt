package dk.itu.moapd.scootersharing

class Scooter{
    private var name: String =""
    private var where:String =""

    constructor(name: String, where: String){
        this.name = name
        this.where = where
    }
    //removed getter and setters since they could be auto generated

    override fun toString(): String {
        return "$name is placed at $where"
    }
}