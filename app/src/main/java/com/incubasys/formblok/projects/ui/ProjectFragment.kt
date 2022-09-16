package com.incubasys.formblok.projects.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.incubasys.formblok.R
import com.incubasys.formblok.common.callback.PaginatedListViewCallback
import com.incubasys.formblok.common.extenstions.hideKeyboard
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.ProjectFragmentBinding
import com.incubasys.formblok.projects.adapter.ProjectAdapter
import com.incubasys.formblok.projects.callback.ProjectFragmentCallback
import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.projects.viewmodel.ProjectViewModel
import kotlinx.android.synthetic.main.project_fragment.*
import java.util.*
import javax.inject.Inject

class ProjectFragment : BaseFragment(), PaginatedListViewCallback {

    private lateinit var binding: ProjectFragmentBinding
    private lateinit var callback: ProjectFragmentCallback
    private lateinit var projectAdapter: ProjectAdapter

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: ProjectViewModel by lazy {
        ViewModelProviders.of(this, factory).get(ProjectViewModel::class.java)
    }

    companion object {
        fun newInstance() = ProjectFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProjectFragmentCallback) callback = context

    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshProjectList()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.project_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        projectAdapter = ProjectAdapter(this)
        rvProjectList.layoutManager = LinearLayoutManager(activity)
        rvProjectList.adapter = projectAdapter

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ivProjectAdd.setOnClickListener {
            callback.onAddProjectButtonClicked()
        }
        binding.vm = viewModel
        binding.callback = this

        initSearchText()
        fetchProjects()
    }

    private fun initSearchText() {
        etProjectFragmentSearchBox.onFocusChangeListener = View.OnFocusChangeListener { _, b ->
            if (!b) {
                hideKeyboard()
            }
        }
        etProjectFragmentSearchBox.addTextChangedListener(object : TextWatcher {
            private val DELAY: Long = 300
            var timer = Timer()

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                timer.cancel()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        activity?.runOnUiThread {
                            val text = etProjectFragmentSearchBox.text.toString()
                            search(text)
                        }
                    }
                }, DELAY)
            }
        })
        etProjectFragmentSearchBox.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun search(query: String?) {
        viewModel.query = query
        fetchProjects()
    }

    private fun performSearch() {
        val text = etProjectFragmentSearchBox.text.toString()
        if (!TextUtils.isEmpty(text)) {
            hideKeyboard()
            etProjectFragmentSearchBox.isCursorVisible = false
            search(text)
        }
    }

    private fun fetchProjects() {
        viewModel.fetchProjects()
        observeProjectsList()
    }

    private fun observeProjectsList() {
        viewModel.getProjects()
            .observe(this,
                Observer<PagedList<Project>> {
                    projectAdapter.submitList(it)
                })

        viewModel.getInitProgress().observe(this, Observer { response ->
            viewModel.networkState.set(response)
            binding.llNetworkStateContainer.executePendingBindings()
        })

        viewModel.getPageProgress().observe(
            this,
            Observer { response -> projectAdapter.setNetworkState(response) }
        )
    }

    override fun onRetryButtonClicked() {
        viewModel.refreshProjectList()
    }

    override fun onListItemClicked(position: Int) {
        viewModel.getProjects().value?.get(position)?.let { callback.onProjectSelected(it) }
    }

    override fun onPageReload() {
        viewModel.retryPage()
    }


}
