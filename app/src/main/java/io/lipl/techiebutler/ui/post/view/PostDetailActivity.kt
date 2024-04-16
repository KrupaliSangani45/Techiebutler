package io.lipl.techiebutler.ui.post.view

import android.app.ActionBar
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import io.lipl.techiebutler.R
import io.lipl.techiebutler.databinding.ActivityPostDetailBinding
import io.lipl.techiebutler.domain.model.PostItem
import io.lipl.techiebutler.ui.BaseActivity
import io.lipl.techiebutler.ui.post.viewmodel.PostViewModel
import io.lipl.techiebutler.utils.Resource


class PostDetailActivity : BaseActivity() {


    // binding
    private var _binding: ActivityPostDetailBinding? = null
    private val binding get() = _binding!!

    // view model
    private val viewModel: PostViewModel by viewModels()
    var id: Int = 0
    var postDetail: PostItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = DataBindingUtil.setContentView(activity, R.layout.activity_post_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (intent != null && intent.extras != null && intent.extras!!.getInt("id") != null)
            id = intent.extras!!.getInt("id")

        if (viewModel.responsePostDetail.value?.data == null) {
            initObserver()
            viewModel.postDetail(id)
        }

        setSupportActionBar(binding.toolbar)
        val actionBar: androidx.appcompat.app.ActionBar? = supportActionBar

        // showing the back button in action bar

        // showing the back button in action bar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initObserver() {

        viewModel.responsePostDetail.observe(this) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let { it1 ->

                        // if match than data change in existing list
                        postDetail = it1
                        setPostData(postDetail)
                    }
                    // loader gone
                }

                Resource.Status.ERROR -> {
                    // loader gone
                }

                Resource.Status.LOADING -> {
                    // loader visible
                }
            }
        }
    }

    private fun setPostData(postDetail: PostItem?) {
        postDetail?.let {
            binding.tvId.text = it.id.toString()
            binding.tvBody.text = it.body
            binding.tvTitle.text = it.title
        }
    }


}