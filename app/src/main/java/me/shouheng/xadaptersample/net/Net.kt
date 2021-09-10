package me.shouheng.xadaptersample.net

import kotlinx.coroutines.Deferred
import me.shouheng.vmlib.bean.Resources
import retrofit2.Call

/**
 * Error handler
 */
class Net {

    companion object {

        /** Connect to get any type of result. Will return the result wrapped by [Resources]. */
        suspend fun <T> connectResources(deferred: Deferred<T>): Resources<T> {
            return try {
                Resources.success(deferred.await())
            } catch (t: Throwable) {
                return Resources.failed("12", "${t.message}")
            }
        }

        /** Do request by raw retrofit call object, used for block case. */
        fun <T> execute(call: Call<T>): Resources<T> {
            return try {
                val response = call.execute()
                if (response.isSuccessful && response.body() != null) {
                    Resources.success(response.body())
                } else {
                    return Resources.failed("1", "${response.code()} failed")
                }
            } catch (t: Throwable) {
                return Resources.failed("12", "${t.message}")
            }
        }
    }
}
