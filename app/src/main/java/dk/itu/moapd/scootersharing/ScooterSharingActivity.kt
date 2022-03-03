package dk.itu.moapd.scootersharing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import java.text.StringCharacterIterator


class ScooterSharingActivity : AppCompatActivity() {

    //GUI variables
    private lateinit var lastAddedText: EditText
    private lateinit var nameText: EditText
    private lateinit var whereText: EditText

    private val scooter: Scooter = Scooter("","")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scooter_sharing)

        //Edit texts
        lastAddedText = findViewById(R.id.last_added_text)
        nameText = findViewById(R.id.name_text)
        whereText = findViewById(R.id.where_text)

        //Buttons
//        addButton = findViewById(R.id.)
        val addButton = findViewById<Button>(R.id.add_button)
        addButton.setOnClickListener {
            if (nameText.text.isNotEmpty() &&
                    whereText.text.isNotEmpty()) {
                //update the object attributes
                val name = nameText.text.toString().trim()
                val where = whereText.text.toString().trim()

                scooter.setName(name)
                scooter.setWhere(where)

                //Reset the text fields and update the UI
                lastAddedText.setText("")
                nameText.setText("")
                whereText.setText("")
                updateUI()

            }

        }
    }

    private fun updateUI(){
        lastAddedText.setText(scooter.toString())
    }
}