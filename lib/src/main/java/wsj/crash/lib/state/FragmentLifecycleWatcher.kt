package wsj.crash.lib.state

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import java.util.regex.Pattern

class FragmentLifecycleWatcher : (Activity) -> Unit {
    private val fragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {

        override fun onFragmentCreated(
            fm: FragmentManager,
            f: Fragment,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentCreated(fm, f, savedInstanceState)
            log(f, "onFragmentCreated")
        }

        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            log(f, "onFragmentViewCreated")
        }

        override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
            super.onFragmentViewDestroyed(fm, f)
            log(f, "onFragmentViewDestroyed")
        }

        override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
            super.onFragmentDestroyed(fm, f)
            log(f, "onFragmentDestroyed")
        }
    }

    private val pattern = Pattern.compile(".*\\}")

    private fun log(f: Fragment, state: String) {
        val matcher = pattern.matcher("$f")
        if (matcher.find()) {
            val content = "${matcher.group()} -> $state"
            FragmentWatcher.fragmentStates.apply {
                add(content)
                if(size>100){
                    removeFirst()
                }
            }
//            LogUtil.e(content)
        } else {
//            LogUtil.e("$f -> $state")
        }
    }

    override fun invoke(activity: Activity) {
        if (activity is FragmentActivity) {
            val fragmentManager = activity.supportFragmentManager
            fragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, true)
        } else {
//            LogUtil.e("activity: $activity is not FragmentActivity")
        }
    }
}