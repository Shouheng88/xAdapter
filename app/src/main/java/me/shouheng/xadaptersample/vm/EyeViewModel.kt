package me.shouheng.xadaptersample.vm

import android.app.Application
import me.shouheng.vmlib.base.BaseViewModel
import me.shouheng.xadaptersample.data.EyepetizerRepo
import me.shouheng.xadaptersample.data.HomeBean

/**
 * @Author wangshouheng
 * @Time 2021/7/27
 */
class EyeViewModel(application: Application) : BaseViewModel(application) {

    private var firstPageRequested: Boolean = false

    private var nextPageUrl: String? = null

    fun firstPage() {
        if (firstPageRequested) return
        firstPageRequested = true
        EyepetizerRepo.instance.firstPage(null, {
            nextPageUrl = it.nextPageUrl
            setSuccess(HomeBean::class.java, it)
        }, { code, msg ->
            setFailed(HomeBean::class.java, code, msg)
        })
    }

    fun nextPage() {
        EyepetizerRepo.instance.morePage(nextPageUrl, {
            nextPageUrl = it.nextPageUrl
            setSuccess(HomeBean::class.java, it)
        }, { code, msg ->
            setFailed(HomeBean::class.java, code, msg)
        })
    }
}
