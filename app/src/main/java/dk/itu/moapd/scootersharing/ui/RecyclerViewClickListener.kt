package dk.itu.moapd.scootersharing.ui

import android.view.View
import dk.itu.moapd.scootersharing.data.Scooter

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClicked(view: View, scooter: Scooter)
}