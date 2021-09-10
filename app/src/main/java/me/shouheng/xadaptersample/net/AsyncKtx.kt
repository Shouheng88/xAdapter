package me.shouheng.xadaptersample.net

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.shouheng.vmlib.bean.Resources
import kotlin.coroutines.CoroutineContext

/** Run an async task by kotlin dsl. */
inline fun <R> runAsync(
    context: CoroutineContext = Dispatchers.Main,
    asyncContext: CoroutineContext = Dispatchers.IO,
    crossinline onSucceed: (Resources<R>) -> Unit = {},
    crossinline onLoading: (Resources<R>) -> Unit = {},
    crossinline onFailed: (Resources<R>) -> Unit = {},
    crossinline asyncTask: suspend () -> Resources<R>
) {
    GlobalScope.launch(context) {
        val result = withContext(asyncContext) {
            try {
                asyncTask.invoke()
            } catch (e: Throwable) {
                Resources.failed<R>("-1", e.message)
            }
        }
        when {
            result.isSucceed -> { onSucceed.invoke(result) }
            result.isLoading -> { onLoading.invoke(result) }
            result.isFailed -> { onFailed.invoke(result) }
        }
    }
}
