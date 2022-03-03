package com.astro.test.adrian.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astro.test.adrian.databinding.FragmentDialogGithubUserBinding
import com.astro.test.adrian.util.goneView
import com.astro.test.adrian.util.setEndlessScrollListener
import com.astro.test.adrian.util.setTextChangeListener
import com.astro.test.adrian.util.showView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * Created by adrianekafikri on 03/03/22.
 */
@AndroidEntryPoint
class GithubUserDialogFragment : DialogFragment() {

    @Inject
    lateinit var adapter: GithubUserAdapter

    private val viewModel: GithubUserViewModel by viewModels()
    lateinit var viewBinding: FragmentDialogGithubUserBinding

    companion object {
        val TAG = GithubUserDialogFragment::class.java.simpleName
        fun newInstance(): GithubUserDialogFragment =
            GithubUserDialogFragment()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentDialogGithubUserBinding.inflate(LayoutInflater.from(requireContext()))
        super.onCreateView(inflater, container, savedInstanceState)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        initView()
        viewModel.getAllUser()
    }

    private fun setupObserver() {
        with(viewBinding) {
            lifecycleScope.launchWhenStarted {
                viewModel.usersFlow.collect {
                    when (it) {
                        is GithubUserState.Success -> {
                            adapter.submitList(it.list)
                            progressBar.goneView()
                        }
                        GithubUserState.Initial -> {
                            progressBar.goneView()
                            llError.goneView()
                        }
                        is GithubUserState.Error -> {
                            progressBar.goneView()
                            if (it.message.isNotBlank()) {
                                llError.showView()
                                tvError.text = it.message
                            }
                        }
                        GithubUserState.StartLoading -> {
                            progressBar.showView()
                        }
                    }
                }
            }
        }
    }

    private fun initView() {
        with(viewBinding) {
            etSearch.setTextChangeListener(300) {
                viewModel.filter(it.toString())
            }
            with(rvUser) {
                adapter = this@GithubUserDialogFragment.adapter.apply {
                    registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
                        override fun onChanged() {
                            scrollToPosition(0)
                        }
                        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                            scrollToPosition(0)
                        }
                        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                            scrollToPosition(0)
                        }
                        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                            scrollToPosition(0)
                        }
                        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                            scrollToPosition(0)
                        }
                    })
                }
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                setEndlessScrollListener {
                    viewModel.getAllUser()
                }
            }
            ivSort.setOnClickListener {
                viewModel.sort(!viewModel.isAscending)
                tvSort.text = if (viewModel.isAscending) "A" else "D"
            }
            llError.setOnClickListener {
                viewModel.getAllUser()
            }
        }
    }
}