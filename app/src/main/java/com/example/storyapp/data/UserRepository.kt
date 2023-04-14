package com.example.storyapp.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.storyapp.data.local.entity.StoryItem
import com.example.storyapp.data.local.room.StoryDatabase
import com.example.storyapp.data.paging.StoryRemoteMediator
import com.example.storyapp.data.remote.api.ApiService
import com.example.storyapp.data.remote.response.*
import com.example.storyapp.utils.Resource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException


@OptIn(ExperimentalPagingApi::class)
class UserRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<BasicResponse>> {
        return flow {
            emit(Resource.Loading)
            try {
                val response = apiService.register(name, email, password)
                emit(Resource.Success(response))
            } catch (e: HttpException) {
                Log.e("UserRepository", "register HttpException: " + e.message)
                emit(Resource.Error(getError(e)))
            } catch (e: IOException) {
                Log.e("UserRepository", "register IOException: " + e.message)
                emit(Resource.Error(e.message.toString()))
            } catch (e: Exception) {
                Log.e("UserRepository", "register Exception: " + e.message)
                emit(Resource.Error(e.message.toString()))
            }

        }.flowOn(Dispatchers.IO)
    }

    suspend fun login(email: String, password: String): Flow<Resource<LoginResponse>> {
        return flow {
            emit(Resource.Loading)
            try {
                val response = apiService.login(email, password)
                emit(Resource.Success(response))
            } catch (e: HttpException) {
                Log.e("UserRepository", "login HttpException: " + e.message)
                emit(Resource.Error(getError(e)))
            } catch (e: IOException) {
                Log.e("UserRepository", "login IOException: " + e.message)
                emit(Resource.Error(e.message.toString()))
            } catch (e: Exception) {
                Log.e("UserRepository", "login Exception: " + e.message)
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    fun getAllStory(token: String): Flow<PagingData<StoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).flow
    }

    suspend fun getDetailStory(token: String, id: String): Flow<Resource<DetailStoryResponse>> {
        return flow {
            emit(Resource.Loading)
            try {
                val response = apiService.getDetailStory(token, id)
                emit(Resource.Success(response))
            } catch (e: HttpException) {
                Log.e("UserRepository", "getDetailStory HttpException: " + e.message)
                emit(Resource.Error(getError(e)))
            } catch (e: IOException) {
                Log.e("UserRepository", "getDetailStory IOException: " + e.message)
                emit(Resource.Error(e.message.toString()))
            } catch (e: Exception) {
                Log.e("UserRepository", "getDetailStory Exception: " + e.message)
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    suspend fun getStoryWithMaps(
        token: String
    ): Flow<Resource<AllStoriesResponse>> {
        return flow {
            emit(Resource.Loading)
            try {
                val response = apiService.getStoryWithMaps(token)
                emit(Resource.Success(response))
            } catch (e: HttpException) {
                Log.e("UserRepository", "getStoryWithMaps HttpException: " + e.message)
                emit(Resource.Error(getError(e)))
            } catch (e: IOException) {
                Log.e("UserRepository", "getStoryWithMaps IOException: " + e.message)
                emit(Resource.Error(e.message.toString()))
            } catch (e: Exception) {
                Log.e("UserRepository", "getStoryWithMaps Exception: " + e.message)
                emit(Resource.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): Flow<Resource<BasicResponse>> {
        return flow {
            emit(Resource.Loading)
            try {
                val response = apiService.addNewStory(token, file, description, lat, lon)
                emit(Resource.Success(response))
            } catch (e: HttpException) {
                Log.e("UserRepository", "addNewStory HttpException: " + e.message)
                emit(Resource.Error(getError(e)))
            } catch (e: IOException) {
                Log.e("UserRepository", "addNewStory IOException: " + e.message)
                emit(Resource.Error(e.message.toString()))
            } catch (e: Exception) {
                Log.e("UserRepository", "addNewStory Exception: " + e.message)
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    fun getId() = userPreferences.getId()
    fun getName() = userPreferences.getName()
    fun getToken() = userPreferences.getToken()

    suspend fun saveId(id: String) = userPreferences.saveId(id)
    suspend fun saveName(name: String) = userPreferences.saveName(name)
    suspend fun saveToken(token: String) = userPreferences.saveToken(token)

    suspend fun removeAll() = userPreferences.removeAll()

    private fun getError(e: HttpException): String {
        val errorMessage = e.response()?.errorBody()?.string()
        val errorResponse = Gson().fromJson(errorMessage, BasicResponse::class.java)
        return errorResponse.message
    }

}