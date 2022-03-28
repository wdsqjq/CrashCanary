package wsj.crash.lib

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.startup.Initializer
import wsj.crash.lib.service.MyService
import wsj.crash.lib.state.FragmentWatcher
import wsj.crash.lib.util.CrashHandler

class CrashCanaryInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        CrashHandler.getInstance().init(context)
        FragmentWatcher.install(context as Application)
        if (Build.VERSION.SDK_INT >= 26)
            context.startForegroundService(Intent(context, MyService::class.java))
        else context.startService(Intent(context, MyService::class.java))
//        return CrashHandler.getInstance()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}