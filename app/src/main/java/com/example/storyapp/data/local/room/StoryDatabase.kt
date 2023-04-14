package com.example.storyapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.storyapp.data.local.entity.RemoteKeys
import com.example.storyapp.data.local.entity.StoryItem

@Database(
    entities = [StoryItem::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}