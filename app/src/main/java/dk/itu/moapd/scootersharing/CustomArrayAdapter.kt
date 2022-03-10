package dk.itu.moapd.scootersharing


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.android.material.textview.MaterialTextView

/**
 * A class to customize an adapter with a `ViewHolder` to populate a dummy dataset into a `ListView`.
 */
class CustomArrayAdapter(context: Context, private var resource: Int, data: List<Scooter>) :
    ArrayAdapter<Scooter>(context, R.layout.list_rides, data) {

    /**
     * A set of private constants used in this class.
     */
    companion object {
        private val TAG = CustomArrayAdapter::class.qualifiedName

    }

    /**
     * An internal view holder class used to represent the layout that shows a single `DummyModel`
     * instance in the `ListView`.
     */
    private class ViewHolder(view: View) {
        val viewName: TextView = view.findViewById(R.id.listViewName)
        val viewWhere: TextView = view.findViewById(R.id.listViewWhere)
        val viewTimestamp: TextView = view.findViewById(R.id.listViewTimeStamp)
    }

    /**
     * Get the `View` instance with information about a selected `DummyModel` from the dataset.
     *
     * @param position The position of the specified item.
     * @param convertView The current view holder.
     * @param parent The parent view which will group the view holder.
     *
     * @return A new view holder populated with the selected `DummyModel` data.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder

        // The old view should be reused, if possible. You should check that this view is non-null
        // and of an appropriate type before using.
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
        } else
            viewHolder = view.tag as ViewHolder

        // Get the selected item in the dataset.
        val scooter = getItem(position)
        Log.i(TAG, "Populate an item at position: $position")

        // Populate the view holder with the selected rides data.

        viewHolder.viewName.text = scooter?.name
        viewHolder.viewWhere.text = scooter?.where
        viewHolder.viewTimestamp.text = scooter?.convertLongToTime(scooter?.timestamp)

        // Set the new view holder and return the view object.
        view?.tag = viewHolder
        return view!!
    }



}