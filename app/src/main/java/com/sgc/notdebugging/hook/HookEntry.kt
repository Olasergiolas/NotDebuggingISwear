package com.sgc.notdebugging.hook

import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.factory.lazyClassOrNull
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import com.sgc.notdebugging.BuildConfig
import com.highcapable.yukihookapi.hook.core.api.priority.YukiHookPriority

@InjectYukiHookWithXposed
class HookEntry : IYukiHookXposedInit {
    private val AndroidOsDebugClass by lazyClassOrNull(
        "android.os.Debug"
    )

    override fun onInit() = configs {
        isDebug = BuildConfig.DEBUG
    }

    override fun onHook() = encase {
        loadApp("com.mobisec.upos") {
            YLog.info("SGC: System loaded, running hooks...")
            "android.os.Debug".toClass()
                .method{
                    name = "isDebuggerConnected"
                    returnType = BooleanType
                }.hook(priority = YukiHookPriority.HIGHEST).replaceToFalse()
        }
    }
}