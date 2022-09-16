package com.incubasys.formblok.quotation.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.incubasys.formblok.R
import com.incubasys.formblok.common.data.model.RoomOutput
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.extenstions.spannedHeading
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.FragmentRoomsBinding
import com.incubasys.formblok.quotation.adapter.RoomAdapter
import com.incubasys.formblok.quotation.callback.RoomAdapterCallback
import com.incubasys.formblok.quotation.callback.RoomsFragmentCallback
import com.incubasys.formblok.quotation.viewmodel.CreateQuotationViewModel
import kotlinx.android.synthetic.main.fragment_rooms.*


class RoomsFragment : BaseFragment(), RoomAdapterCallback {

    override fun onItemCheckChanged(room: RoomOutput, position: Int) {
        viewModel.updateItem(position, room.isChecked)
    }

    override fun onItemAddClicked(position: Int, room: RoomOutput) {
        viewModel.addNewItem(position)
    }

    override fun onItemClicked(position: Int) {
        viewModel.roomPosition.value = position
        viewModel.room.value = viewModel.roomList.value?.get(position)
        callback.onNextClicked(CreateQuotationActivity.ACTION.ROOMS_DETAIL)
    }

    private lateinit var viewModel: CreateQuotationViewModel
    private lateinit var binding: FragmentRoomsBinding
    private lateinit var callback: RoomsFragmentCallback
    private lateinit var roomAdapter: RoomAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is RoomsFragmentCallback) callback = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as CreateQuotationActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rooms, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = viewModel
        initFragment()
        settingUpRecyclerView()
        viewModel.getRooms()
        subscribeToUi()
    }

    private fun initFragment() {
        (activity as CreateQuotationActivity).setProgressIncrement(60)
        tvRoomHeading.text =
            spannedHeading(
                (activity as CreateQuotationActivity).applicationContext,
                "Select your\nrooms...",
                "rooms..."
            )
        ivRoomBackArrow.setOnClickListener {
            activity?.onBackPressed()
        }
        bRoomNext.setOnClickListener {
            if (viewModel.calculatedAreaToShow.value!! > 0.0) {
                if (!viewModel.calculatedAreaExceeds.get()) {
                    viewModel.fillRooms()
                    callback.onNextClicked(CreateQuotationActivity.ACTION.MATERIAL)
                } else {
                    Toast
                        .makeText(activity, "Your rooms have more space than your total area.", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast
                    .makeText(activity, "Please select at least one room", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun settingUpRecyclerView() {
        roomAdapter = RoomAdapter(context!!, this)
        rvRooms.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = roomAdapter
        }
    }

    private fun subscribeToUi() {
        viewModel.apiResponse.observe(
            this,
            Observer {
                when (it.status) {
                    ApiStatus.LOADING -> {
                        showProgress()
                    }
                    ApiStatus.SUCCESS -> {
                        dismissProgress()
                    }
                    ApiStatus.ERROR -> {
                        dismissProgress()
                        Toast.makeText(activity, it.error?.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                    }
                }
            })
        viewModel.roomList.observe(this, Observer {
            roomAdapter.submitList(it)
            roomAdapter.notifyDataSetChanged()
        })

        viewModel.calculatedAreaToShow.observe(this, Observer {
            if (it <= viewModel.propertyOutput.total_liveable_area) {
                viewModel.calculatedAreaExceeds.set(false)
            } else {
                viewModel.calculatedAreaExceeds.set(true)
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RoomsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
