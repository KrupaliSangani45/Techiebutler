package io.lipl.techiebutler.ui.post.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.activity.enableEdgeToEdge
import io.lipl.techiebutler.R
import io.lipl.techiebutler.adapter.PostAdapter
import io.lipl.techiebutler.databinding.ActivityPostBinding
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.lipl.techiebutler.ui.BaseActivity
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import io.lipl.techiebutler.ui.post.viewmodel.PostViewModel
import io.lipl.techiebutler.utils.Debug
import io.lipl.techiebutler.utils.Resource

class PostActivity : BaseActivity() {


    // binding
    private var _binding: ActivityPostBinding? = null
    private val binding get() = _binding!!

    // view model
    private val viewModel: PostViewModel by viewModels()

    // adapter
    private lateinit var mAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = DataBindingUtil.setContentView(activity, R.layout.activity_post)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        initAdapter()

        if (viewModel.response.value?.data == null) {
            initObserver()
            viewModel.apiPostApiCall()
        }


    }

    private fun initAdapter() {
        // adapter
        mAdapter =
            PostAdapter(
                viewModel.postList,
            ) { position, _ ->
//                when (view.id) {
                val postDetail = viewModel.postList[position]
                val intent = Intent(this, PostDetailActivity::class.java)
                intent.putExtra("id", postDetail?.id)
                startActivity(intent)

//                }
            }
        binding.rvPost.apply {
            this.adapter = mAdapter
            this.layoutManager = LinearLayoutManager(activity)
            val itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                    activity,
                    R.drawable.bg_divider
                )!!
            )
            addItemDecoration(itemDecoration)
        }

        // pagination
        binding.rvPost.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    viewModel.isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val currentItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (!viewModel.isApiCalling && viewModel.isScrolling && totalItemCount < viewModel.totalItemCount) {
                    if (currentItemCount + lastVisibleItemPosition >= totalItemCount && lastVisibleItemPosition >= 0 && dy >= 0) {
                        viewModel.isScrolling = false
                        viewModel.currentPage += 1
                        binding.progressBarFootBar.visibility = View.VISIBLE
                        viewModel.apiPostApiCall()
                    }
                }
            }
        })
    }

    private fun initObserver() {

        viewModel.response.observe(this) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        it?.let { it1 ->
                            viewModel.postList.addAll(it1)
                        }
                        Debug.e("----", "---postList--" + viewModel.postList.size)
                        mAdapter.notifyDataSetChanged()
                        viewModel.totalItemCount = it?.size ?: 0
                    }
                    binding.progressBarFootBar.visibility = View.GONE
                }

                Resource.Status.ERROR -> {
                    binding.progressBarFootBar.visibility = View.GONE
                }

                Resource.Status.LOADING -> {
                    binding.progressBarFootBar.visibility = View.VISIBLE

                }
            }
        }

    }


}