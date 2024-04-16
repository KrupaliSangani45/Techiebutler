package io.lipl.techiebutler.ui.post.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.lipl.techiebutler.domain.repository.RetrofitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lipl.techiebutler.domain.model.PostItem
import io.lipl.techiebutler.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: RetrofitRepository
) : ViewModel() {

    private val _response: MutableLiveData<Resource<ArrayList<PostItem>>> = MutableLiveData()
    val response: LiveData<Resource<ArrayList<PostItem>>> = _response

    private val _responsePostDetail: MutableLiveData<Resource<PostItem>> = MutableLiveData()
    val responsePostDetail: LiveData<Resource<PostItem>> = _responsePostDetail


    val postList: MutableList<PostItem?> = mutableListOf()

    var currentPage: Int = 1
    var totalItemCount: Int = 0
    var isScrolling = false
    var isApiCalling = false
    private val pageSize: Int = 10


    fun apiPostApiCall() = viewModelScope.launch {
        _response.postValue(Resource.loading())
        isApiCalling = true


        val reqParam = HashMap<String, Int>()
        reqParam["page"] = currentPage
        reqParam["page_size"] = pageSize

        repository.getPosts(reqParam).collect { values ->
            isApiCalling = false
            _response.value = values
        }
    }

    fun postDetail(postId: Int) = viewModelScope.launch {
        repository.getPostDetail(postId).collect { values ->
            _responsePostDetail.value = values
        }
    }


}