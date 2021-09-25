package me.shouheng.xadaptersample.view

import android.os.Bundle
import me.shouheng.utils.ktx.onDebouncedClick
import me.shouheng.vmlib.base.ViewBindingActivity
import me.shouheng.vmlib.comn.ContainerActivity
import me.shouheng.vmlib.comn.ContainerActivity.KEY_EXTRA_THEME_ID
import me.shouheng.vmlib.comn.EmptyViewModel
import me.shouheng.xadaptersample.R
import me.shouheng.xadaptersample.databinding.ActivityMainBinding

class MainActivity : ViewBindingActivity<EmptyViewModel, ActivityMainBinding>() {

    override fun doCreateView(savedInstanceState: Bundle?) {
        setSupportActionBar(binding.toolbar)
        supportFragmentManager.beginTransaction().replace(R.id.container, EyeFragment()).commit()
        binding.fab.onDebouncedClick {
            ContainerActivity.open(MultiListFragment::class.java)
                .put(KEY_EXTRA_THEME_ID, R.style.Theme_SimpleAdapter_NoActionBar)
                .launch(context)
        }
    }
}