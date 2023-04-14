package com.example.storyapp.ui.widget

import android.content.Intent
import android.widget.RemoteViewsService

class StackWidgetService : RemoteViewsService() {

    private var list = ArrayList<String>()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_UPDATE_WIDGET) {
            val bundle = intent.getBundleExtra(EXTRA_LIST_STORY)

            if (bundle != null) {
                bundle.getStringArrayList("imageUrlList")?.forEach {
                    list.add(it)
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return StackRemoteViewsFactory(this.applicationContext, list)
    }


    companion object {
        const val ACTION_UPDATE_WIDGET = "com.example.storyapp.UPDATE_WIDGET"
        const val EXTRA_LIST_STORY = "com.example.storyapp.EXTRA_LIST_STORY"
    }
}