package com.incubasys.formblok.explore.adapter


import android.content.Context
import android.graphics.Typeface
import android.text.style.CharacterStyle
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.incubasys.formblok.databinding.PredictionListItemBinding
import com.incubasys.formblok.explore.data.model.PlaceAutocomplete
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.collections.ArrayList


open class PlacesAutoCompleteAdapter(private val context: Context, private val placesClient: PlacesClient) :
    ListAdapter<PlaceAutocomplete, PlacesAutoCompleteAdapter.PredictionHolder>(DiffCallback()), Filterable {

    private var mResultList = ArrayList<PlaceAutocomplete>()
    private var clickListener: ClickListener? = null
    private val STYLE_NORMAL: CharacterStyle = StyleSpan(Typeface.NORMAL)


    fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun click(place: Place)
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    mResultList = getPredictions(constraint)
                    // The API successfully returned results.
                    results.values = mResultList
                    results.count = mResultList.size
                }
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged()
                } else {
                    // The API did not return any results, invalidate the data set.
                    //notifyDataSetInvalidated();
                }
            }
        }
    }

    private fun getPredictions(constraint: CharSequence): ArrayList<PlaceAutocomplete> {

        val resultList = ArrayList<PlaceAutocomplete>()

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        val token = AutocompleteSessionToken.newInstance()


        val bounds = RectangularBounds.newInstance(
            LatLngBounds(LatLng(-38.007265, 141.002957), LatLng(-36.018512, 147.893780))
        )

        // Use the builder to create a FindAutocompletePredictionsRequest.
        val request = FindAutocompletePredictionsRequest.builder()
            //Call either setLocationBias() OR setLocationRestriction().
            .setLocationRestriction(bounds)
           // .setCountry("au")
            .setTypeFilter(TypeFilter.ADDRESS)
            .setSessionToken(token)
            .setQuery(constraint.toString())
            .build()

        val autocompletePredictions = placesClient.findAutocompletePredictions(request)

        // This method should have been called off the main UI thread. Block and wait for at most
        // 60s for a result from the API.
        try {
            Tasks.await(autocompletePredictions, 60, TimeUnit.SECONDS)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: TimeoutException) {
            e.printStackTrace()
        }

        if (autocompletePredictions.isSuccessful) {
            val findAutocompletePredictionsResponse = autocompletePredictions.result
            if (findAutocompletePredictionsResponse != null)
                for (prediction in findAutocompletePredictionsResponse.autocompletePredictions) {
                   // Log.i("prediction", prediction.placeId)
                    resultList.add(
                        PlaceAutocomplete(
                            prediction.placeId,
                            prediction.getFullText(STYLE_NORMAL).toString()
                            //prediction.getSecondaryText(STYLE_NORMAL).toString()
                        )
                    )
                }

            return resultList
        } else {
            return resultList
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PredictionHolder(
            PredictionListItemBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PredictionHolder, position: Int) {
        if (mResultList.size > 0) {
            holder.bind(mResultList[position])
        }
    }

    override fun getItemCount(): Int {
        return mResultList.size
    }

    private fun getRealItem(position: Int) = mResultList[position]

    inner class PredictionHolder internal constructor(itemView: PredictionListItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding = itemView

        init {
            binding.placeItemView.setOnClickListener {
                if (mResultList.size > 0) {
                    val (placeId1) = mResultList[adapterPosition]

                    val placeId = placeId1.toString()

                    val placeFields =
                        Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
                    val request = FetchPlaceRequest.builder(placeId, placeFields).build()
                    placesClient.fetchPlace(request).addOnSuccessListener { response ->
                        val place = response.place
                        clickListener?.click(place)
                    }.addOnFailureListener { exception ->
                        if (exception is ApiException) {
                            Toast.makeText(context, exception.message + "", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(context, "No Address Found.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun bind(placeAutocomplete: PlaceAutocomplete) {
            binding.place = placeAutocomplete
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<PlaceAutocomplete>() {
    override fun areItemsTheSame(oldItem: PlaceAutocomplete, newItem: PlaceAutocomplete): Boolean {
        return oldItem.placeId == newItem.placeId
    }

    override fun areContentsTheSame(oldItem: PlaceAutocomplete, newItem: PlaceAutocomplete): Boolean {
        return oldItem == newItem
    }
}