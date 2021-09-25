package me.shouheng.xadaptersample.data

import me.shouheng.xadaptersample.net.Net
import me.shouheng.xadaptersample.net.Server
import me.shouheng.xadaptersample.net.runAsync
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EyepetizerRepo private constructor() {

    /** Memory cache */
    private var bean: HomeBean? = null

    /** Get home bean data of first page */
    fun firstPage(
        date: Long?,
        success: (bean: HomeBean) -> Unit,
        fail: (code: String, msg: String) -> Unit
    ) {
        if (bean != null) {
            success(bean!!)
            return
        }
        runAsync(
            asyncTask = {
                Net.connectResources(Server.get(EyeService::class.java).getFirstHomeDataAsync(date))
            },
            onSucceed = { success(it.data) },
            onFailed = { fail(it.code, it.message) }
        )
    }

    /** Get home bean data of more page */
    fun morePage(
        url: String?,
        success: (bean: HomeBean) -> Unit,
        fail: (code: String, msg: String) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val res = withContext(Dispatchers.IO) {
                Net.connectResources(
                    Server.get(EyeService::class.java)
                    .getMoreHomeDataAsync(url))
            }
            if (res.isSucceed) {
                success(res.data)
            } else {
                fail(res.code, res.message)
            }
        }
    }

    companion object {
        val instance: EyepetizerRepo by lazy { EyepetizerRepo() }
    }
}
