package com.incubasys.formblok.notification.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.incubasys.formblok.R
import com.incubasys.formblok.common.callback.PaginatedListViewCallback
import com.incubasys.formblok.common.data.remote.ApiStatus.*
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.NotificationFragmentBinding
import com.incubasys.formblok.notification.adapter.NotificationAdapter
import com.incubasys.formblok.notification.callback.NotificationFragmentCallback
import com.incubasys.formblok.notification.data.model.NotificationInput
import com.incubasys.formblok.notification.data.model.NotificationOutput
import com.incubasys.formblok.notification.viewmodel.NotificationViewModel
import kotlinx.android.synthetic.main.notification_fragment.*
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class NotificationFragment : BaseFragment(), PaginatedListViewCallback {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: NotificationViewModel by lazy {
        ViewModelProviders.of(this, factory).get(NotificationViewModel::class.java)
    }

    private lateinit var binding: NotificationFragmentBinding
    private lateinit var callback: NotificationFragmentCallback
    private lateinit var adapter: NotificationAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NotificationFragmentCallback) callback = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.notificationInput.value = it.getParcelable("notification_input")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.notification_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NotificationAdapter(this)
        rvNotificationList.layoutManager = LinearLayoutManager(activity)
        rvNotificationList.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.vm = viewModel
        binding.callback = this

        rvNotificationList.layoutManager = LinearLayoutManager(activity)
        rvNotificationList.adapter = adapter

        viewModel.notificationInput.observe(this, Observer {
            if (it.comeFromAppLink) {
                viewModel.createNotification(it)
                it.comeFromAppLink = false
            } else {
                fetchNotification()
            }
        })

        viewModel.apiResponse.observe(this, Observer {
            when (it.status) {
                LOADING -> {
                    showProgress()
                }
                SUCCESS -> {
                    dismissProgress()
                }
                EMPTY -> {
                }
                ERROR -> {
                    dismissProgress()
                    Toast.makeText(activity, it.error?.message, Toast.LENGTH_SHORT).show()
                    fetchNotification()
                }
                COMPLETED -> {
                }
            }
        })

        viewModel.notificationOutput.observe(this, Observer {
            fetchNotification()
        })

        tvNotificationFragmentMarkAsReadLabel.setOnClickListener {
            viewModel.markAllNotifications()
        }
    }

    private fun fetchNotification() {
        viewModel.fetchNotification()
        observeProjectsList()
    }

    private fun observeProjectsList() {
        viewModel.notifications
            .observe(this,
                Observer<PagedList<NotificationOutput>> {
                    adapter.submitList(it)
                    Log.v("list", it.toString())
                })

        viewModel.initProgress.observe(this, Observer { response ->
            viewModel.networkState.set(response)
            binding.llNetworkStateContainer.executePendingBindings()
        })

        viewModel.pageProgress.observe(
            this,
            Observer { response -> adapter.setNetworkState(response) }
        )
    }

    override fun onRetryButtonClicked() {
        viewModel.refreshNotificationList()
    }

    override fun onListItemClicked(position: Int) {
        viewModel.notifications.value?.get(position)?.let {
            it.isRead = true
            adapter.notifyItemChanged(position, it)
            callback.onNotificationClicked(it)
        }
    }

    override fun onPageReload() {
        viewModel.retryPage()
    }

    companion object {
        fun newInstance(
            notificationInput: NotificationInput
        ) = NotificationFragment().apply {
            arguments = Bundle().apply {
                putParcelable("notification_input", notificationInput)
            }
        }
    }

}
