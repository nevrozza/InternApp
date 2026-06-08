package org.nevrzq.intern

import android.content.Intent
import auth.repositories.YandexAuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext.get

fun handleOAuthIntent(lifecycleScope: CoroutineScope, intent: Intent?) {
    val uri = intent?.data ?: return
    if (uri.scheme != "org.nevrzq.intern") return
    if (uri.host != "oauth") return
    if (uri.path != "/yandex/callback") return

    val parameters = uri.queryParameterNames.associateWith { name ->
        uri.getQueryParameter(name).orEmpty()
    }

    lifecycleScope.launch {
        get().get<YandexAuthRepository>().handleOAuthCallback(parameters)
    }
}