package io.lipl.techiebutler.domain.repository

import io.lipl.techiebutler.domain.model.PostItem
import io.lipl.techiebutler.domain.rest.RetrofitService
import io.lipl.techiebutler.utils.BaseApiResponse
import io.lipl.techiebutler.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RetrofitRepository @Inject constructor(
    private val service: RetrofitService
) : BaseApiResponse() {


    suspend fun getPosts(
        reqParam: Map<String, Int>
    ): Flow<Resource<ArrayList<PostItem>>> {
        return flow {
            emit(safeApiCall { service.getPosts(reqParam) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getPostDetail(postId: Int): Flow<Resource<PostItem>> {
        return flow {
            emit(safeApiCall { service.getPostDetail(postId) })
        }.flowOn(Dispatchers.IO)
    }


}