package me.shouheng.xadaptersample

import android.app.Application
import me.shouheng.vmlib.VMLib
import me.shouheng.xadaptersample.net.Server

/**
 * @Author wangshouheng
 * @Time 2021/7/26
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Server.initServer("http://baobab.kaiyanapp.com/api/")
        VMLib.onCreate(this)
    }
}
