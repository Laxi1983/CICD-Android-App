package com.undp.fleettracker.activity.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.undp.fleettracker.R
import com.undp.fleettracker.activity.base.BaseActivity
import com.undp.fleettracker.adapter.BookingSummaryPagerAdapter
import kotlinx.android.synthetic.main.content_booking_summary.*

class BookingSummaryActivity : BaseActivity() {

    private var bookingSummaryPagerAdapter: BookingSummaryPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_summary)
        initBaseActivityViews()
        supportActionBar?.show()
        supportActionBar?.title = resources.getString(R.string.title_booking_summary)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        initPager()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun getBottomNavigationMenuItemId(): Int {
        return R.id.action_booking
    }

    private fun initPager() {
        bookingSummaryPagerAdapter = BookingSummaryPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = bookingSummaryPagerAdapter

        tabs.setupWithViewPager(view_pager)
    }
}
