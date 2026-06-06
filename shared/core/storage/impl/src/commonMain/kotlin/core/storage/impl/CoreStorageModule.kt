package core.storage.impl

import core.storage.impl.keyvalue.settingsModule
import org.koin.dsl.module

val coreStorageModule = module {
    includes(settingsModule)
}