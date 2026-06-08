import KMP
import SwiftUI

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    IosOAuthKt.handleOAuthCallbackUrl(url: url.absoluteString)
                }
        }
    }
}
