package com.sgc.notdebugging.hook

import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import com.sgc.notdebugging.BuildConfig

@InjectYukiHookWithXposed
class HookEntry : IYukiHookXposedInit {

    override fun onInit() = configs {
        isDebug = BuildConfig.DEBUG
    }

    override fun onHook() = encase {
        YLog.info("SGC: Loading hooks...")
        loadSystem {
            "android.os.Debug".toClass()
                .method{
                    name = "isDebuggerConnected"
                    emptyParam()
                    returnType = BooleanType
                }.hook {
                    replaceToFalse()
                }.result {
                    onAllFailure {
                        YLog.error("SGC: Failed to apply hook")
                    }
                }
        }
    }
}