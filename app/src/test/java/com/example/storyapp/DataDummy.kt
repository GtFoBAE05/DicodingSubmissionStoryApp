package com.example.storyapp

import com.example.storyapp.data.local.entity.StoryItem

object DataDummy {
    fun generateDummyResponse() : List<StoryItem> {
        val items : MutableList<StoryItem> = arrayListOf()

        for(i in 0..100){
            val item = StoryItem(
                id = i.toString(),
                photoUrl = "PhotoUrl $i",
                createdAt = "CreatedAt $i",
                name = "Name $i",
                description = "Description $i",
                lat = i.toDouble(),
                lon = i.toDouble(),
            )
            items.add(item)
        }

        return items
    }
}