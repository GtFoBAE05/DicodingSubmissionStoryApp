package com.example.storyapp.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.example.storyapp.R

internal class StackRemoteViewsFactory(
    private val mContext: Context,
    private val data: ArrayList<String>
) : RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Bitmap>()

    override fun onCreate() {
    }

    override fun onDataSetChanged() {

        data.forEach {
            val image = Glide.with(mContext).asBitmap().load(it).submit().get()
            mWidgetItems.add(image)
        }

    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.widgetItemImageView, mWidgetItems[position])

        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}