package com.example.storyapp.ui.story.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.DataDummy
import com.example.storyapp.MainDispatcherRule
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.local.entity.StoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListStoryViewModelTestItem {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var userRepository: UserRepository

    /**
     * Memastikan data tidak null.
     * Memastikan jumlah data sesuai dengan yang diharapkan.
     * Memastikan data pertama yang dikembalikan sesuai.
     */
    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummy = DataDummy.generateDummyResponse()
        val data: PagingData<StoryItem> = StoryPagingSource.snapshot(dummy)
        val expectedStory = flowOf(data)

        Mockito.`when`(userRepository.getAllStory("token")).thenReturn(expectedStory)

        userRepository.getAllStory("token").collect {
            val differ = AsyncPagingDataDiffer(
                diffCallback = ListStoryAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main,
            )
            differ.submitData(it)
            assertNotNull(differ.snapshot())
            assertEquals(dummy.size, differ.snapshot().size)
            assertEquals(dummy[0], differ.snapshot()[0])
        }
    }

    /**
     * Memastikan jumlah data yang dikembalikan nol.
     */
    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryItem> = PagingData.from(emptyList())
        val expectedStory = flowOf(data)

        Mockito.`when`(userRepository.getAllStory("token")).thenReturn(expectedStory)

        userRepository.getAllStory("token").collect {
            val differ = AsyncPagingDataDiffer(
                diffCallback = ListStoryAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main,
            )
            differ.submitData(it)
            assertEquals(0, differ.snapshot().size)
        }
    }


}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoryItem>>>() {
    companion object {
        fun snapshot(items: List<StoryItem>): PagingData<StoryItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryItem>>>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}