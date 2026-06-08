import auth.repositories.YandexAuthRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform
import platform.Foundation.NSURLComponents
import platform.Foundation.NSURLQueryItem

@Suppress("unused")
fun handleOAuthCallbackUrl(url: String) {
    val components = NSURLComponents(string = url)

    if (components.scheme != "org.nevrzq.intern") return
    if (components.host != "oauth") return
    if (components.path != "/yandex/callback") return


    val parameters = buildMap {
        components.queryItems?.forEach { item ->
            item as NSURLQueryItem
            put(item.name, item.value ?: "")
        }
    }

    val koin = KoinPlatform.getKoin()

    val repository: YandexAuthRepository = koin.get()

    MainScope().launch {
        repository.handleOAuthCallback(parameters)
    }
}