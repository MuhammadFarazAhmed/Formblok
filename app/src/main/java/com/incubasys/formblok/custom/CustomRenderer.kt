package com.incubasys.formblok.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.incubasys.formblok.common.extenstions.getMarkerIconFromDrawable
import com.incubasys.formblok.explore.data.model.AddressMapItem
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.view.LayoutInflater
import android.graphics.drawable.Drawable
import android.widget.TextView
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.maps.android.ui.IconGenerator
import com.incubasys.formblok.R
import com.incubasys.formblok.common.ui.addPlusIcon
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers


class CustomRenderer constructor(
    private val context: Context,
    mMap: GoogleMap,
    clusterManager: ClusterManager<AddressMapItem>
) : DefaultClusterRenderer<AddressMapItem>(context, mMap, clusterManager) {

    //private var textMarkerGen = IconGenerator(context)
    //private val compositeDisposible = CompositeDisposable()

    private val BUCKETS = intArrayOf(10, 20, 50, 100, 200, 300, 400, 500, 1000, 3000, 4000, 8000, 9000)

    override fun shouldRenderAsCluster(cluster: Cluster<AddressMapItem>): Boolean {
        return cluster.size > 1
    }

    override fun onBeforeClusterItemRendered(item: AddressMapItem, markerOptions: MarkerOptions) {
        val circleDrawable = ContextCompat.getDrawable(context, R.drawable.ic_location_pin)
        markerOptions.icon(circleDrawable?.let { getMarkerIconFromDrawable(it) })
    }


    override fun getBucket(cluster: Cluster<AddressMapItem>?): Int {
        val size = cluster?.size ?: 0
        if (size <= BUCKETS[0]) {
            return size
        } else {
            for (i in 0 until BUCKETS.size - 1) {
                if (size < BUCKETS[i + 1]) {
                    return BUCKETS[i]
                }
            }

            return BUCKETS[BUCKETS.size - 1]
        }
    }

    override fun getColor(clusterSize: Int): Int {
        return ContextCompat.getColor(context, R.color.colorPrimary)
    }

}