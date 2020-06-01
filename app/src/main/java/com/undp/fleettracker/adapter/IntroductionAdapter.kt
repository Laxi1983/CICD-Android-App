package com.undp.fleettracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.rishabhharit.roundedimageview.RoundedImageView
import com.undp.fleettracker.R
import com.undp.fleettracker.adapter.model.IntroductionModel

class IntroductionAdapter (private val mContext: Context) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.introduction_cards, container, false) as ViewGroup
        val imageView = view.findViewById<RoundedImageView>(R.id.img_slider)
        imageView.setImageResource(IntroductionModel.values()[position].imageResId)
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return IntroductionModel.values().size
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }
}