package com.incubasys.formblok.projects.ui


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.incubasys.formblok.R
import com.incubasys.formblok.common.callback.PaginatedListViewCallback
import com.incubasys.formblok.common.data.remote.ApiStatus.*
import com.incubasys.formblok.common.extenstions.hideKeyboard
import com.incubasys.formblok.common.extenstions.spannedHeading
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.FragmentSelectProjectBinding
import com.incubasys.formblok.projects.adapter.SelectProjectAdapter
import com.incubasys.formblok.projects.callback.CreateProjectBottomSheetCallback
import com.incubasys.formblok.projects.callback.SelectProjectFragmentCallback
import com.incubasys.formblok.projects.data.model.Project
import com.incubasys.formblok.projects.data.model.PropertyInput
import com.incubasys.formblok.projects.viewmodel.SelectProjectViewModel
import kotlinx.android.synthetic.main.fragment_select_project.*
import kotlinx.android.synthetic.main.project_fragment.rvProjectList
import java.util.*
import javax.inject.Inject

private const val PROPERTY = "property"

class SelectProjectFragment : BaseFragment(), PaginatedListViewCallback, CreateProjectBottomSheetCallback {

    private val dialogFragment = CreateProjectBottomSheetDialog.newInstance()
    override fun onCancelButtonClicked() {
        dialogFragment.dismiss()
    }

    override fun onConfirmButtonClicked() {
        dialogFragment.dismiss()
        val intent = Intent(activity, CreateProjectActivity::class.java)
        intent.putExtra("property", propertyInput)
        startActivity(intent)
    }

    private var propertyInput: PropertyInput? = null

    private lateinit var projectAdapter: SelectProjectAdapter

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: SelectProjectViewModel by lazy {
        ViewModelProviders.of(this, factory).get(SelectProjectViewModel::class.java)
    }

    private lateinit var binding: FragmentSelectProjectBinding
    private lateinit var callback: SelectProjectFragmentCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SelectProjectFragmentCallback) callback = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            propertyInput = it.getParcelable(PROPERTY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_project, container, false)
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        projectAdapter = SelectProjectAdapter(this)
        rvProjectList.layoutManager = LinearLayoutManager(activity)
        rvProjectList.adapter = projectAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvSelectProjectHeading.text =
            activity?.applicationContext?.let {
                spannedHeading(
                    it,
                    "Select your\nprojects...",
                    "projects..."
                )
            }
        ivSelectProjectBackArrow.setOnClickListener {
            activity?.onBackPressed()
        }
        initSearchText()
        fetchProjects()

        viewModel.apiResponse.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                LOADING -> {
                    showProgress()
                }
                SUCCESS -> {
                    dismissProgress()
                    it.data?.let { project ->
                        project.isSelectableProject = true
                        callback.openPorpertyDetailActivity(project, project.properties?.get(project.properties!!.size - 1))
                    }
                }
                EMPTY -> {
                }
                ERROR -> {
                    dismissProgress()
                    Toast.makeText(activity, it.error?.message, Toast.LENGTH_SHORT).show()
                }
                COMPLETED -> TODO()
            }
        })
    }


    companion object {
        @JvmStatic
        fun newInstance(propertyInput: PropertyInput?) =
            SelectProjectFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PROPERTY, propertyInput)
                }
            }
    }

    private fun initSearchText() {
        etSelectProjectFragmentSearchBox.onFocusChangeListener = View.OnFocusChangeListener { _, b ->
            if (!b) {
                hideKeyboard()
            }
        }
        etSelectProjectFragmentSearchBox.addTextChangedListener(object : TextWatcher {
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
                            val text = etSelectProjectFragmentSearchBox.text.toString()
                            search(text)
                        }
                    }
                }, DELAY)
            }
        })
        etSelectProjectFragmentSearchBox.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
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
        val text = etSelectProjectFragmentSearchBox.text.toString()
        if (!TextUtils.isEmpty(text)) {
            hideKeyboard()
            etSelectProjectFragmentSearchBox.isCursorVisible = false
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
                androidx.lifecycle.Observer<PagedList<Project>> {
                    projectAdapter.submitList(it)
                })

        viewModel.getInitProgress().observe(this, androidx.lifecycle.Observer { response ->
            if (response.apiStatus == EMPTY) {
                dialogFragment.show(childFragmentManager, dialogFragment.tag)
            }
            viewModel.networkState.set(response)
            binding.llNetworkStateContainer.executePendingBindings()
        })

        viewModel.getPageProgress().observe(
            this,
            androidx.lifecycle.Observer { response -> projectAdapter.setNetworkState(response) }
        )
    }

    override fun onRetryButtonClicked() {
        viewModel.refreshProjectList()
    }

    override fun onListItemClicked(position: Int) {
        viewModel.getProjects().value?.get(position)?.id?.let {
            propertyInput?.let { it1 -> viewModel.addAPropertyToProject(it, it1) }
        }
    }

    override fun onPageReload() {
        viewModel.retryPage()
    }
}
