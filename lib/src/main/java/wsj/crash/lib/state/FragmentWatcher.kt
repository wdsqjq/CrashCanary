package wsj.crash.lib.state

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy
import java.util.*

class FragmentWatcher {

    companion object {

        val fragmentStates: LinkedList<String> by lazy { LinkedList<String>() }

        private val fragmentLifeCycleWatchers = FragmentLifecycleWatcher()

        private val lifecycleCallbacks =
            object : Application.ActivityLifecycleCallbacks by noOpDelegate() {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    fragmentLifeCycleWatchers(activity)
                }
            }

        fun install(app: Application) {
            app.registerActivityLifecycleCallbacks(lifecycleCallbacks)
        }
    }
}


internal inline fun <reified T : Any> noOpDelegate(): T {
    val javaClass = T::class.java
    return Proxy.newProxyInstance(
        javaClass.classLoader, arrayOf(javaClass), NO_OP_HANDLER
    ) as T
}

private val NO_OP_HANDLER = InvocationHandler { _, _, _ ->
    // no op
}